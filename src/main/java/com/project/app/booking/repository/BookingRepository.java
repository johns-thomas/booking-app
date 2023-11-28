package com.project.app.booking.repository;

import com.project.app.booking.enums.Status;
import com.project.app.booking.models.BookingEntity;

import com.project.app.booking.models.ListingEntity;
import com.project.app.booking.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByUser(UserEntity user);
}
