package com.project.app.booking.dto;

import com.project.app.booking.enums.Type;
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
    String eircode;
    String address;
    int bedrooms;
    int bathrooms;
    String img;
    Type type;
}
