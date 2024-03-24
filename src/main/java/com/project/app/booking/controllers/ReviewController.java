package com.project.app.booking.controllers;


import com.project.app.booking.awsutility.dynamodb.DynamoDBUtility;
import com.project.app.booking.dto.ReviewDTO;
import com.project.app.booking.dto.ReviewItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    @Autowired
    private DynamoDBUtility dbUtility;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal UserDetails user){
        ReviewItem item=new ReviewItem();
        item.setUser(user.getUsername());
        item.setContent(reviewDTO.getContent());
        item.setTitle(reviewDTO.getTitle());
        item.setRating(reviewDTO.getRating());
        item.setLocation(reviewDTO.getLocation());
        String res=dbUtility.putRecord(item);
        if(res!=null){
           return new ResponseEntity<String>("Successfully created", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Failed", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getReviews(@RequestParam String city, @AuthenticationPrincipal UserDetails user){
        ArrayList<ReviewItem> items=dbUtility.getReviewsByCity(city);
        List<ReviewDTO> list=items.stream()
                .map(item -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setTitle(item.getTitle()); // Example: Set field1
                    dto.setContent(item.getContent()); // Example: Set field2
                    dto.setRating(item.getRating());
                    return dto;
                })
                .collect(Collectors.toList());
        if (items!=null) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
