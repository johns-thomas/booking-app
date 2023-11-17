package com.project.app.booking.service;

import com.project.app.booking.dto.ListingDTO;
import com.project.app.booking.dto.ListingDtoMapper;
import com.project.app.booking.dto.ListingView;
import com.project.app.booking.dto.PropertyDTO;
import com.project.app.booking.enums.Status;
import com.project.app.booking.models.ListingEntity;
import com.project.app.booking.models.PropertyEntity;
import com.project.app.booking.models.UserEntity;
import com.project.app.booking.repository.ListingRepository;
import com.project.app.booking.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListingService {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ListingDtoMapper listingDtoMapper;

    public void addListing(ListingDTO listing, UserDetails user) {
        UserEntity userEntity=customUserDetailsService.getByUsername(user.getUsername());
        PropertyEntity propertyEntity= propertyRepository.getPropertyById(listing.getProperty().getId()).get();
        ListingEntity entity=new ListingEntity();
        entity.setDateListed(listing.getDateListed());
        entity.setStatus(listing.getStatus());
        entity.setUser(userEntity);
        entity.setProperty(propertyEntity);
        listingRepository.save(entity);

    }

    public ListingView getListingById(long id){
        return listingRepository.getListingById(id)
                .map(listingDtoMapper)
                .orElseThrow(() -> new RuntimeException(
                        "Listing not found"
                ));
    }

    public List<ListingView> getAllListings(){
        return listingRepository.findAll().stream().map(listingDtoMapper).collect(Collectors.toList());
    }

    public List<ListingView> getListings(Status status){
        return listingRepository.findByStatus(status).stream().map(listingDtoMapper).collect(Collectors.toList());
    }

    public void updateStatus(long id,Status status){
        var listing=listingRepository.getListingById(id).get();
        listing.setStatus(status);
        listingRepository.save(listing);
    }
}
