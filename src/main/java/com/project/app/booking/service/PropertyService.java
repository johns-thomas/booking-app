package com.project.app.booking.service;


import com.awsutility.s3.S3Utility;
import com.awsutility.sqs.SQSUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.booking.dto.*;
import com.project.app.booking.enums.Status;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private BookingDTOMapper bookingDTOMapper;

    @Autowired
    private ListingService listingService;

    @Autowired
    private SQSUtility sqsUtility;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${aws.queue.booking.name}")
    private String queueName;

    @Value("${aws.queue.booking.url}")
    private String queueUrl;

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
        ListingView listingView=listingService.getListingById(bookingDTO.getListingId());
        if(listingView.getStatus().equals(Status.SOLD)){
            throw new RuntimeException("Not  Available");
        }
        BookingEntity booking=new BookingEntity();
        booking.setProperty(property);
        booking.setUser(userEntity);

        booking.setDateBooked(bookingDTO.getBookingDate());
        var bookingObj= bookingRepository.save(booking);
        listingService.updateStatus(listingView.getId(),Status.SOLD );
        createNotifyEvent(createNotifyMessage(bookingObj,listingView));
        return bookingObj;
    }

    private void createNotifyEvent(String message){
        sqsUtility.enqueueMessage(message,queueUrl);
    }

    private String createNotifyMessage(BookingEntity bookingEntity,ListingView listingView) {
        BookingMessage message=new BookingMessage();
        message.setBuyerName(bookingEntity.getUser().getFname()+' '+bookingEntity.getUser().getLname());
        message.setBuyerEmail(bookingEntity.getUser().getEmail());

        message.setSellerName(listingView.getOwnerName());
        message.setSellerEmail(listingView.getOwnerEmail());
        message.setProperty(Optional.of(bookingEntity.getProperty()).map(propertyDtoMapper).get());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(bookingEntity.getDateBooked());
        message.setBookingDate(strDate);
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<BookingView> getUserBookings(UserDetails user){
        UserEntity userEntity=customUserDetailsService.getByUsername(user.getUsername());
          return   bookingRepository.findByUser(userEntity).stream().map(bookingDTOMapper).collect(Collectors.toList());
    }



}
