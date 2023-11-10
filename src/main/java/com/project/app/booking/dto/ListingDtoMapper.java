package com.project.app.booking.dto;

import com.project.app.booking.models.ListingEntity;
import com.project.app.booking.models.PropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class ListingDtoMapper implements Function<ListingEntity, ListingDTO> {

    @Autowired
    private PropertyDtoMapper propertyDtoMapper;
    @Override
    public ListingDTO apply(ListingEntity listing) {
        return new ListingDTO(
                listing.getId(),
                Optional.of(listing.getProperty()).map(propertyDtoMapper).get(),
                listing.getDateListed(),
                listing.getStatus(),
                listing.getUser()
        );
    }
}
