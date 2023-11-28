package com.project.app.booking.dto;

import com.project.app.booking.models.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class BookingDTOMapper implements Function<BookingEntity, BookingView> {

    @Autowired
    private PropertyDtoMapper propertyDtoMapper;

    @Override
    public BookingView apply(BookingEntity bookingEntity) {
        BookingView view =new BookingView();
        view.setId(bookingEntity.getId());
        view.setProperty(Optional.of(bookingEntity.getProperty()).map(propertyDtoMapper).get());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(bookingEntity.getDateBooked());
        view.setBookingDate(strDate);
        return view;
    }
}
