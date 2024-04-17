package com.project.app.booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.booking.awsutility.dynamodb.DynamoDBUtility;
import com.project.app.booking.awsutility.sqs.SQSUtility;
import com.project.app.booking.dto.BookingMessage;
import com.project.app.booking.dto.ListingView;
import com.project.app.booking.dto.NotificationDTO;
import com.project.app.booking.dto.ReviewDTO;
import com.project.app.booking.models.BookingEntity;
import com.project.app.booking.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NotificationService {

    @Value("${aws.queue.booking.name}")
    private String queueName;

    @Value("${aws.queue.booking.url}")
    private String queueUrl;

    @Autowired
    private SQSUtility sqsUtility;

    @Autowired
    private DynamoDBUtility dbUtility;

    private String createNotifyMessage(ReviewDTO review, UserEntity user) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("review",review.getCity());
        map.put("id",review.getId());
        map.put("user",user.getFname()+" "+user.getLname());
        map.put("user_id",user.getUsername());
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void createNotifyEvent(ReviewDTO review, UserEntity user){
        String message=createNotifyMessage(review,user);
        sqsUtility.enqueueMessage(message,queueUrl);
    }

    public List<NotificationDTO> getNotifications(String username) {
        return dbUtility.getNotificationsForUser(username);
    }
}
