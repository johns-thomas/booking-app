package com.project.app.booking.service;

import com.project.app.booking.dto.ListingDTO;
import com.project.app.booking.dto.ListingDtoMapper;
import com.project.app.booking.dto.PropertyDTO;
import com.project.app.booking.models.ListingEntity;
import com.project.app.booking.models.UserEntity;
import com.project.app.booking.repository.ListingRepository;
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
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ListingDtoMapper listingDtoMapper;

    public void addListing(ListingDTO listing, UserDetails user) {
        UserEntity userEntity=customUserDetailsService.getByUsername(user.getUsername());
        ListingEntity entity=new ListingEntity();
        entity.setDateListed(listing.getDateListed());
        entity.setStatus(listing.getStatus());
        entity.setUser(userEntity);
        listingRepository.save(entity);

    }

    public ListingDTO getListingById(long id){
        return listingRepository.getListingById(id)
                .map(listingDtoMapper)
                .orElseThrow(() -> new RuntimeException(
                        "Listing not found"
                ));
    }

    public List<ListingDTO> getAllListings(){
        return listingRepository.findAll().stream().map(listingDtoMapper).collect(Collectors.toList());
    }
}
