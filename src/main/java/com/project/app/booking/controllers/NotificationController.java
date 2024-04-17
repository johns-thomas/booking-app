package com.project.app.booking.controllers;

import com.project.app.booking.awsutility.dynamodb.DynamoDBUtility;
import com.project.app.booking.dto.NotificationDTO;
import com.project.app.booking.dto.ReviewDTO;
import com.project.app.booking.dto.ReviewItem;
import com.project.app.booking.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/notification")
public class NotificationController {



    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@AuthenticationPrincipal UserDetails user){
        List<NotificationDTO> items=notificationService.getNotifications(user.getUsername());
        if(items!=null){
            return ResponseEntity.ok(items);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
