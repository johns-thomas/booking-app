package com.project.app.booking.repository;

import com.project.app.booking.models.ListingEntity;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListingRepository extends JpaRepository<ListingEntity, Long> {

    Optional<ListingEntity> getListingById(long id);
}
