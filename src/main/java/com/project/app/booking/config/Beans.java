package com.project.app.booking.config;

import com.awsutility.S3Utility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class Beans {
    @Bean
    JwtSecurityCustomFilter getJwtSecurityFilter(){
        return new JwtSecurityCustomFilter();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration ) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public S3Utility s3Operations() {
        return S3Utility.build(awsRegion);
    }
}
