package com.project.app.booking.service;

import com.awsutility.S3Utility;
import com.project.app.booking.dto.BookingDTO;
import com.project.app.booking.dto.PropertyDTO;
import com.project.app.booking.dto.PropertyDtoMapper;
import com.project.app.booking.models.BookingEntity;
import com.project.app.booking.models.PropertyEntity;
import com.project.app.booking.models.UserEntity;
import com.project.app.booking.repository.BookingRepository;
import com.project.app.booking.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private S3Utility s3Utility;

    @Autowired
    private PropertyDtoMapper propertyDtoMapper;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public byte[] getPropertyImage(Long propertyId) {
        PropertyDTO property = getPropertyById(propertyId);
        byte[] img = s3Utility.getObject(
                bucketName,
                "propertyImages/%s/%s".formatted(propertyId, property.getImg())
        );
        return img;
    }

    public void uploadPropertyImage(long propertyId, MultipartFile file) {
        var propertyImageId = UUID.randomUUID().toString();
        try {
            s3Utility.uploadObject(
                    bucketName,
                    "propertyImages/%s/%s".formatted(propertyId, propertyImageId),
                    file.getBytes()
            );
        }  catch (IOException e) {
            e.printStackTrace();
        }
        propertyRepository.updatePropertyImageId(propertyImageId,propertyId);
    }

    public long createProperty(PropertyDTO property) {
        PropertyEntity propertyEntity=new PropertyEntity();
        propertyEntity.setTitle(property.getTitle());
        propertyEntity.setDescription(property.getDescription());
        propertyEntity.setArea(property.getAddress());
        propertyEntity.setBathrooms(property.getBathrooms());
        propertyEntity.setBedrooms(property.getBedrooms());
        propertyEntity.setPrice(property.getPrice());
        propertyEntity.setType(property.getType());
        propertyEntity.setLocation(property.getEircode());
        return propertyRepository.save(propertyEntity).getId();

    }
    public PropertyDTO getPropertyById(long id){
        PropertyDTO property = propertyRepository.getPropertyById(id)
                .map(propertyDtoMapper)
                .orElseThrow(() -> new RuntimeException(
                        "Property not found"
                ));
        return property;
    }


    public BookingEntity buyProperty(BookingDTO bookingDTO, UserDetails user) {
        UserEntity userEntity=customUserDetailsService.getByUsername(user.getUsername());
        PropertyEntity property=propertyRepository.getPropertyById(bookingDTO.getPropertyId()).orElseThrow(() -> new RuntimeException(
                "Property not found"
        ));
        BookingEntity booking=new BookingEntity();
        booking.setProperty(property);
        booking.setUser(userEntity);
        booking.setDateBooked(bookingDTO.getBookingDate());
        return bookingRepository.save(booking);
    }
}
