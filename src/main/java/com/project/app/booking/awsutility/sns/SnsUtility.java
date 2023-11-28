package com.project.app.booking.awsutility.sns;


import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;


public class SnsUtility {

    private static SnsClient snsClient ;
    private static final SnsUtility snsUtility= new SnsUtility();

    private SnsUtility(){
    }
    public static SnsUtility build(String region){
        snsUtility.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .build();
        return snsUtility;
    }

    public  void pubTopic( String message, String topicArn) {

        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            throw new RuntimeException(e.awsErrorDetails().errorMessage());
        }
    }

    public SubscribeResponse subEmail(String topicArn, String email) {

        try {
            SubscribeRequest request = SubscribeRequest.builder()
                    .protocol("email")
                    .endpoint(email)
                    .returnSubscriptionArn(true)
                    .topicArn(topicArn)
                    .build();

           return snsClient.subscribe(request);

        } catch (SnsException e) {
            throw new RuntimeException(e.awsErrorDetails().errorMessage());
        }
    }





}
