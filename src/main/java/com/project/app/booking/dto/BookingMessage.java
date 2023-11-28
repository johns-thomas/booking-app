package com.project.app.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingMessage {
    String buyerName;
    String sellerName;
    String buyerEmail;
    String sellerEmail;
    PropertyDTO property;
    String bookingDate;
}
