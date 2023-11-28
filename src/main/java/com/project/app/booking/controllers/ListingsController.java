package com.project.app.booking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.booking.dto.ListingDTO;
import com.project.app.booking.dto.ListingView;
import com.project.app.booking.dto.PropertyDTO;
import com.project.app.booking.enums.Status;
import com.project.app.booking.service.ListingService;
import com.project.app.booking.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
public class ListingsController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private PropertyService propertyService;

    @PostMapping(value="create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createListing(@AuthenticationPrincipal UserDetails user, @RequestParam("json") String json,
                                                @RequestParam("file") MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PropertyDTO prop=mapper.readValue(json, PropertyDTO.class);
        long propertyId=propertyService.createProperty(prop);
        prop.setId(propertyId);
        propertyService.uploadPropertyImage(propertyId,file);
        ListingDTO listing=new ListingDTO();
        listing.setDateListed(new Date());
        listing.setStatus(Status.AVAILABLE);
        listing.setProperty(prop);
        listingService.addListing(listing,user);

        return new ResponseEntity<String>("Successfully created", HttpStatus.OK);
    }

    @GetMapping(value="{id}")
    public ResponseEntity<ListingView> getListingById(@PathVariable long id){
        ListingView  listingView=listingService.getListingById(id);

        if (listingView!=null) {
            return ResponseEntity.ok(listingView);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/")
    public ResponseEntity<List<ListingView>> getListings(@RequestParam("status") Status status){

        List<ListingView> list= listingService.getListings(status);
        if (list!=null) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.notFound().build();
        }

    }


}
