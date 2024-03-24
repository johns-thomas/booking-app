package com.project.app.booking.awsutility.dynamodb;


import com.project.app.booking.dto.Review;
import com.project.app.booking.dto.ReviewItem;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
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


    private DynamoDbEnhancedClient enhancedClient;

    private static final DynamoDBUtility dbUtilityUtility= new DynamoDBUtility();
    public static DynamoDBUtility build(String region){
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
            DynamoDbTable<Review> workTable = enhancedClient.table("Review", TableSchema.fromBean(Review.class));
            String myGuid = java.util.UUID.randomUUID().toString();
            Review record = new Review();
            record.setTitle(item.getTitle());
            record.setId(myGuid);
            record.setContent(item.getContent());
            record.setRating(item.getRating()); ;
            record.setUser(item.getUser());
            workTable.putItem(record);
            return "success";
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());

        }
        return null;
    }



    public ArrayList<ReviewItem> getReviewsByCity(String city) {


        try{
            DynamoDbTable<Review> table = enhancedClient.table("Review", TableSchema.fromBean(Review.class));
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
                workItem.setId(work.getId());
                workItem.setUser(work.getUser());


                // Push the workItem to the list.
                itemList.add(workItem);
            }
            return itemList;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());

        }
        return null;
    }
}
