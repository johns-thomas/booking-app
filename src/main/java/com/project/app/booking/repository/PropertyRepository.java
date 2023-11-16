package com.project.app.booking.repository;

import com.project.app.booking.models.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyEntity,Integer> {

    @Modifying
    @Query("UPDATE PropertyEntity p SET p.img = :image WHERE p.id = :pid")
    void updatePropertyImageId(@Param("image") String propertyImageId, @Param("pid")long propertyId);

    Optional<PropertyEntity> getPropertyById(long propertyId);
}
