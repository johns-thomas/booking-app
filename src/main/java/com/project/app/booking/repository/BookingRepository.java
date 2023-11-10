package com.project.app.booking.repository;

import com.project.app.booking.models.BookingEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
