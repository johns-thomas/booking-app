package com.project.app.booking.dto;

import com.project.app.booking.enums.Status;
import com.project.app.booking.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListingDTO {
    private long id;
    private PropertyDTO property;
    private Date dateListed;
    private Status status;
    private UserEntity user;
}
