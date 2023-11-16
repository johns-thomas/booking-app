package com.project.app.booking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.booking.dto.FormDTO;
import com.project.app.booking.dto.ListingDTO;
import com.project.app.booking.dto.PropertyDTO;
import com.project.app.booking.models.UserEntity;
import com.project.app.booking.service.ListingService;
import com.project.app.booking.service.PropertyService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/listings/")
public class ListingsController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private PropertyService propertyService;



    @PostMapping(value="create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createListing(@AuthenticationPrincipal UserDetails user, @RequestParam("json") String json,
                              @RequestParam("file") MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ListingDTO listing=mapper.readValue(json, ListingDTO.class);
        long propertyId=propertyService.createProperty(listing.getProperty());
        listing.getProperty().setId(propertyId);
        propertyService.uploadPropertyImage(propertyId,file);
        listingService.addListing(listing,user);


    }

    @GetMapping(value="{id}")
    public ListingDTO getListingById(@PathVariable long id){
        return listingService.getListingById(id);
    }



    @GetMapping()
    public List<ListingDTO> getListings(@RequestParam String location, @RequestParam String status){
        return listingService.getAllListings();
    }


}
