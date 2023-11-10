package com.project.app.booking.repository;

import com.project.app.booking.models.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyEntity,Integer> {

    @Query("UPDATE property p SET p.img = ?1 WHERE p.id = ?2")
    void updatePropertyImageId( String propertyImageId,long propertyId);

    Optional<PropertyEntity> getPropertyById(long propertyId);
}
