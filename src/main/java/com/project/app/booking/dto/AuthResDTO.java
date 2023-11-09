package com.project.app.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthResDTO {
    private String token;
    private String type="Bearer";

    public AuthResDTO(String token){
        this.token=token;

    }
}
