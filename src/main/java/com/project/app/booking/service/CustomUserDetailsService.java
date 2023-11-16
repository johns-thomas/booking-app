package com.project.app.booking.service;

import com.project.app.booking.dto.RegisterDTO;
import com.project.app.booking.models.UserEntity;
import com.project.app.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    //CustomUserDetailsService()
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("No user found"));

        return new User(userEntity.getUsername(),userEntity.getPassword(), Arrays.asList( new SimpleGrantedAuthority("User")));
    }

    public UserEntity getByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("No user found"));
    }
    public UserEntity createUser(RegisterDTO regDto) throws Exception{

        if(userRepository.existsByUsername(regDto.getUsername())){
            throw new Exception("User exists!");
        }

        if (userRepository.existsByEmail(regDto.getEmail())) {
            throw new RuntimeException(
                    "email already taken"
            );
        }
        UserEntity userEntity=new UserEntity();
        userEntity.setUsername(regDto.getUsername());
        userEntity.setEmail(regDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(regDto.getPassword()));
        userEntity.setFname(regDto.getFname());
        userEntity.setLname(regDto.getLname());

        return  userRepository.save(userEntity);
    }
}
