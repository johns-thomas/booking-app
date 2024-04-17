package com.project.app.booking.awsutility.dynamodb;


//import com.project.app.booking.dto.Review;
import com.project.app.booking.dto.NotificationDTO;
import com.project.app.booking.dto.ReviewItem;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.util.*;

public class DynamoDBUtility {

    @Value("aws.dynamodb.tablename")
    private String tableName;

    @Value("aws.accessKey")
    private static String accessKey;

    @Value("aws.secretKey")
    private static String secretKey;


    private DynamoDbEnhancedClient enhancedClient;

    private static final DynamoDBUtility dbUtilityUtility= new DynamoDBUtility();
    public static DynamoDBUtility build(String region){

        //final AwsCredentialsProvider credentialsProvider= StaticCredentialsProvider.create(accessKey,secretKey);
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(Region.of(region))
                .build();
        dbUtilityUtility.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
        return dbUtilityUtility;
    }

    public String putRecord(ReviewItem item) {

        try {
            DynamoDbTable<Review> workTable = enhancedClient.table("x22203389sc-review", TableSchema.fromBean(Review.class));
            String myGuid = java.util.UUID.randomUUID().toString();
            Review record = new Review();
            record.setTitle(item.getTitle());
            record.setReviewId(myGuid);
            record.setContent(item.getContent());
            record.setRating(item.getRating()); ;
            record.setUserId(item.getUser());
            record.setLocation(item.getLocation());
            workTable.putItem(record);
            return myGuid;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());

        }
        return null;
    }



    public ArrayList<ReviewItem> getReviewsByCity(String city) {


        try{
            DynamoDbTable<Review> table = enhancedClient.table("x22203389sc-review", TableSchema.fromBean(Review.class));
            AttributeValue attr = AttributeValue.builder()
                    .s(city)
                    .build();

            Map<String, AttributeValue> myMap = new HashMap<>();
            myMap.put(":val1",attr);

            Map<String, String> myExMap = new HashMap<>();
            myExMap.put("#location", "location");

            // Set the Expression so only active items are queried from the Work table.
            Expression expression = Expression.builder()
                    .expressionValues(myMap)
                    .expressionNames(myExMap)
                    .expression("#location = :val1")
                    .build();

            ScanEnhancedRequest enhancedRequest = ScanEnhancedRequest.builder()
                    .filterExpression(expression)
                    .limit(15)
                    .build();

            // Scan items.
            Iterator<Review> results = table.scan(enhancedRequest).items().iterator();
            ReviewItem workItem ;
            ArrayList<ReviewItem> itemList = new ArrayList<>();

            while (results.hasNext()) {
                workItem = new ReviewItem();
                Review work = results.next();
                workItem.setTitle(work.getTitle());
                workItem.setContent(work.getContent());
                workItem.setRating(work.getRating());
                workItem.setId(work.getReviewId());
                workItem.setUser(work.getUserId());


                // Push the workItem to the list.
                itemList.add(workItem);
            }
            return itemList;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());

        }
        return null;
    }


    @DynamoDbBean
    public static class Review {



        private String reviewId;

        private String userId;
        private String title;
        private String content;

        private int rating;
        private String location;


        public Review() {
        }

        public Review(String reviewId, String userId, String title, String content, int rating, String location) {
            this.reviewId = reviewId;
            this.userId = userId;
            this.title = title;
            this.content = content;
            this.rating = rating;
            this.location = location;
        }


        public void setReviewId(String id) {
            this.reviewId = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setUserId(String user) {
            this.userId = user;
        }
        @DynamoDbPartitionKey
        public String getReviewId() {
            return this.reviewId;
        }
        @DynamoDbSortKey
        public String getUserId() {
            return this.userId;
        }

        public String getTitle() {
            return this.title;
        }

        public String getContent() {
            return this.content;
        }

        public int getRating() {
            return this.rating;
        }

        public String getLocation() {
            return this.location;
        }


    }


    public List<NotificationDTO> getNotificationsForUser(String username){

        try{
        DynamoDbTable<Notification> table = enhancedClient.table("x22203389sc-notification", TableSchema.fromBean(Notification.class));

        AttributeValue attr = AttributeValue.builder()
                .s(username)
                .build();

        Map<String, AttributeValue> myMap = new HashMap<>();
        myMap.put(":val1",attr);

        Map<String, String> myExMap = new HashMap<>();
        myExMap.put("#userId", "userId");

        // Set the Expression so only active items are queried from the Work table.
        Expression expression = Expression.builder()
                .expressionValues(myMap)
                .expressionNames(myExMap)
                .expression("#userId <> :val1")
                .build();

        ScanEnhancedRequest enhancedRequest = ScanEnhancedRequest.builder()
                .filterExpression(expression)
                .limit(15)
                .build();

        // Scan items.
        Iterator<Notification> results = table.scan(enhancedRequest).items().iterator();
        NotificationDTO workItem ;
        ArrayList<NotificationDTO> itemList = new ArrayList<>();

        while (results.hasNext()) {
            workItem = new NotificationDTO();
            Notification work = results.next();
            workItem.setMessage(work.getMessage());
            workItem.setReviewId(work.getReviewId());
            workItem.setUserId(work.getUserId());


            // Push the workItem to the list.
            itemList.add(workItem);
        }
        return itemList;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());

        }
        return null;
    }


    @DynamoDbBean
    public static class Notification{
        private String reviewId;

        private String userId;

        private String message;


        public Notification(){}

        public Notification(String reviewId,String userId,String message){
            this.message=message;
            this.userId=userId;
            this.reviewId=reviewId;
        }
        @DynamoDbPartitionKey
        public String getReviewId(){
            return reviewId;
        }
        @DynamoDbSortKey
        public String getUserId(){
            return userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setReviewId(String reviewId) {
            this.reviewId = reviewId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


}
