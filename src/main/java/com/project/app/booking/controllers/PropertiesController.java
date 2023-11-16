package com.project.app.booking.controllers;

import com.project.app.booking.dto.BookingDTO;
import com.project.app.booking.dto.PropertyDTO;
import com.project.app.booking.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties/")
public class PropertiesController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("{id}")
    public PropertyDTO getPropertyById(@PathVariable int id){
        return propertyService.getPropertyById(id);
    }

    @GetMapping(
            value = "{propertyId}/image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getPropertyImage(
            @PathVariable("propertyId") Long propertyId) {
        return propertyService.getPropertyImage(propertyId);
    }

    @PostMapping("book_viewing")
    public ResponseEntity<Object> bookViewing(@RequestBody BookingDTO bookingDTO, @AuthenticationPrincipal UserDetails user){
        var obj=propertyService.buyProperty(bookingDTO,user);
            if(obj!=null){
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }

    }


    @PostMapping("rent/{id}")
    public void rentProperty(@RequestPart("property_id") int id,@AuthenticationPrincipal UserDetails user){

    }
}
