package com.project.app.booking.dto;

import com.project.app.booking.models.BookingEntity;
import com.project.app.booking.models.ListingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fname;
    private String lname;
    private String email;
//    private Set<ListingEntity> listings;
//    private Set<BookingEntity> bookings;
}
