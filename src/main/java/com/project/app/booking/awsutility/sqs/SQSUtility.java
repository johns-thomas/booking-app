package com.project.app.booking.awsutility.sqs;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SQSUtility {

    private static SqsClient sqsClient ;
    private static final SQSUtility sqsUtility= new SQSUtility();

    private SQSUtility(){
    }
    public static SQSUtility build(String region){
        sqsUtility.sqsClient = SqsClient.builder()
                .region(Region.of(region))
                .build();
        return sqsUtility;
    }


    public  String createSQSQueue(String queueName ) {

        try {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();
            sqsClient.createQueue(createQueueRequest);
            GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
            return getQueueUrlResponse.queueUrl();

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return "";
    }

    public void enqueueMessage(String message,String queueUrl ){
        enqueueMessage( message,queueUrl,10 );
    }
    public void enqueueMessage(String message,String queueUrl,int delaySeconds ){
        try {
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .delaySeconds(delaySeconds)
                    .build());

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
    public  List<Message> dequeMessages(String queueUrl,int maxNumberOfMessages) {
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(maxNumberOfMessages)
                    .build();
            return sqsClient.receiveMessage(receiveMessageRequest).messages();

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return null;
    }
    public void changeMessagesVisibiltyTimeout(String queueUrl, List<Message> messages, int timeout) {
        try {
            for (Message message : messages) {
                ChangeMessageVisibilityRequest request = ChangeMessageVisibilityRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .visibilityTimeout(timeout)
                        .build();
                sqsClient.changeMessageVisibility(request);
            }

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }

    public static void deleteMessages(String queueUrl, List<Message> messages) {
        try {
            for (Message message : messages) {
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build();
                sqsClient.deleteMessage(deleteMessageRequest);
            }

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
    public List<String> listQueues(String queNamePrefix) {
        List<String> queUrls;
        try {
            ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix(queNamePrefix).build();
            ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);
            return listQueuesResponse.queueUrls();

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }

    public static List<String> listQueuesFilter(String queueNamePrefix , String queueUrl ) {
        try {
        ListQueuesRequest filterListRequest = ListQueuesRequest.builder()
                .queueNamePrefix(queueNamePrefix)
                .build();

        ListQueuesResponse listQueuesFilteredResponse = sqsClient.listQueues(filterListRequest);
        return listQueuesFilteredResponse.queueUrls();
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }

    public static void sendBatchMessages(String queueUrl, HashMap<String,String> idMessageMap) {
        List<SendMessageBatchRequestEntry> messageBatchRequests=new ArrayList<>();
        for (Map.Entry entry:idMessageMap.entrySet()) {
            messageBatchRequests.add(SendMessageBatchRequestEntry.builder().id(entry.getKey().toString()).messageBody(entry.getValue().toString()).build());
        }
        try {
            SendMessageBatchRequest sendMessageBatchRequest = SendMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(messageBatchRequests)
                    .build();
            sqsClient.sendMessageBatch(sendMessageBatchRequest);

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
    public String getQueueUrl(String queueName){
            try{
                GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
                return getQueueUrlResponse.queueUrl();

            } catch (SqsException e) {
                System.err.println(e.awsErrorDetails().errorMessage());
                throw e;
            }
    }

    public void addNotificationEvent(String json){

    }
}
