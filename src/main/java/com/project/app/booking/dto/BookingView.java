package com.project.app.booking.dto;

import com.project.app.booking.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingView {
    Long id;
    String buyerName;
    String buyerEmail;
    PropertyDTO property;
    String bookingDate;

}
