package com.project.app.booking.dto;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;



@DynamoDbBean
public class Review {



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

    ;
}
