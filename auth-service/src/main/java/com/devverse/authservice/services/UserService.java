package com.devverse.authservice.services;


import com.devverse.authservice.jwt.JwtUtil;
import com.devverse.authservice.models.User;
import com.devverse.authservice.repositories.UserRepository;
import com.devverse.authservice.response.AuthResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, CustomUserDetailService customUserDetailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailService = customUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse RegisterUser(User user){
        User newUser=new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        User savedUser=userRepository.save(newUser);
        Authentication authentication=new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        String jwt=jwtUtil.generateToken(authentication);
        return new AuthResponse(jwt,savedUser);
    }

    public AuthResponse LoginUser(String email, String password) throws Exception{
        Authentication authentication=authenticate(email, password);
        String jwt=jwtUtil.generateToken(authentication);
        User user=userRepository.findByEmail(email).orElse(null);
        return new AuthResponse(jwt, user);
    }

    private UsernamePasswordAuthenticationToken authenticate(String email, String password) {
        UserDetails userDetails= customUserDetailService.loadUserByUsername(email);
        if(userDetails==null){
            throw new UsernameNotFoundException("User not found");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

}
