package com.project.app.booking.models;

import com.project.app.booking.enums.BookingType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="bookings")
public class BookingEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id", referencedColumnName = "id",nullable=false)
    private PropertyEntity property;

    @Column
    Date dateBooked;

    @Column
    Date dateTillBooked;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;



}
