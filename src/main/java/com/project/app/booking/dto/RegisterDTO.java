package com.project.app.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RegisterDTO {
 private String username;
 private String password;
 private String fname;
 private String lname;
 private String email;
}
