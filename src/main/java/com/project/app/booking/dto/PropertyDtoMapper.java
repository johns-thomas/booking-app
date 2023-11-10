package com.project.app.booking.dto;

import com.project.app.booking.models.PropertyEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PropertyDtoMapper implements Function<PropertyEntity, PropertyDTO> {
    @Override
    public PropertyDTO apply(PropertyEntity property) {
        return new PropertyDTO(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getPrice(),
                property.getLocation(),
                property.getArea(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getImg(),
                property.getType()
        );
    }
}
