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
public class BookingDTO {
    private long propertyId;
    private long listingId;
    private Date bookingDate;
    private Date bookedTill;

}
