package com.example.Campus.Connect.services;


import com.example.Campus.Connect.jwt.JwtUtil;
import com.example.Campus.Connect.models.User;
import com.example.Campus.Connect.repositories.UserRepository;
import com.example.Campus.Connect.response.AuthResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        String jwt=jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(jwt,savedUser);
    }

    public AuthResponse LoginUser(String email, String password) throws Exception{
        Optional<User> user=userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new Exception("User not found");
        }
        if(!passwordEncoder.matches(password,user.get().getPassword())){
            throw new Exception("Wrong password");
        }
        String jwt=jwtUtil.generateToken(user.get().getEmail());
        return new AuthResponse(jwt, user.get());

    }

}
