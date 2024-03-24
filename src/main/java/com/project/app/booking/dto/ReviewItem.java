package com.project.app.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewItem {

    private String title;
    private String content;

    private int rating;
    private String location;

    private String user;
    private String id;
}
