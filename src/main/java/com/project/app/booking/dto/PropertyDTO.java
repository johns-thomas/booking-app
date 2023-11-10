package com.project.app.booking.dto;

import com.project.app.booking.enums.Type;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {
    private Long id;
    String title;
    String description;
    Float price;
    String location;
    String area;
    int bedrooms;
    int bathrooms;
    String img;
    Type type;
}
