package com.project.app.booking.controllers;

import com.awsutility.sns.SnsUtility;
import com.project.app.booking.config.SecurityTokenGenerator;
import com.project.app.booking.dto.AuthResDTO;
import com.project.app.booking.dto.RegisterDTO;
import com.project.app.booking.dto.SignInDTO;
import com.project.app.booking.dto.UserDTO;
import com.project.app.booking.models.UserEntity;
import com.project.app.booking.service.CustomUserDetailsService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class AuthenticationController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private SecurityTokenGenerator securityTokenGenerator;

    @Autowired
    private SnsUtility snsUtility;

    @Value("${aws.sns.topicARN}")
    private String topicARN;

    @PostMapping("/authenticate/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterDTO registerDTO){
        try {
            UserEntity user=customUserDetailsService.createUser(registerDTO);
            snsUtility.subEmail(topicARN, user.getEmail());
        } catch (Exception e) {
           return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("User successfully created", HttpStatus.OK);
    }

    @PostMapping("/authenticate/signin")
    public ResponseEntity<AuthResDTO> signIn(@RequestBody SignInDTO signInDTO){
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getUsername(),signInDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var token=securityTokenGenerator.generateToken(authentication);

        return new ResponseEntity<AuthResDTO>(new AuthResDTO(token), HttpStatus.OK);
    }

    @GetMapping("/")
    public UserDTO getLoggedInUser(@AuthenticationPrincipal UserDetails user){
        UserEntity userEntity=customUserDetailsService.getByUsername(user.getUsername());
        UserDTO userDTO=new UserDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setFname(userEntity.getFname());
        userDTO.setLname(userEntity.getLname());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setBookings(userEntity.getBookings());
        userDTO.setListings(userEntity.getListings());
        return userDTO;
    }
}
