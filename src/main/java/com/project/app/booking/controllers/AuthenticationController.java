package com.project.app.booking.controllers;

import com.project.app.booking.config.SecurityTokenGenerator;
import com.project.app.booking.dto.AuthResDTO;
import com.project.app.booking.dto.RegisterDTO;
import com.project.app.booking.dto.SignInDTO;
import com.project.app.booking.service.CustomUserDetailsService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authenticate/")
public class AuthenticationController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private SecurityTokenGenerator securityTokenGenerator;

    @PostMapping("signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterDTO registerDTO){
        try {
            customUserDetailsService.createUser(registerDTO);

        } catch (Exception e) {
           return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("User successfully created", HttpStatus.OK);
    }

    @PostMapping("signin")
    public ResponseEntity<AuthResDTO> signIn(@RequestBody SignInDTO signInDTO){
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getUsername(),signInDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var token=securityTokenGenerator.generateToken(authentication);

        return new ResponseEntity<AuthResDTO>(new AuthResDTO(token), HttpStatus.OK);
    }
}
