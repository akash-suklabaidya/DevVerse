package com.devverse.authservice.controller;

import com.devverse.authservice.response.ApiResponse;
import com.devverse.authservice.models.User;
import com.devverse.authservice.repositories.UserRepository;
import com.devverse.authservice.response.AuthResponse;
import com.devverse.authservice.response.LoginRequest;
import com.devverse.authservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("auth/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> createUser(@RequestBody User user) {
        try{
            if(userRepository.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>("error","email already present",null));
            }
            AuthResponse authResponse=userService.RegisterUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>("success","Registration successful",authResponse)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("error",e.getMessage(),null));
        }
    }

    @PostMapping("auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(@RequestBody LoginRequest loginRequest) {
        try{
            AuthResponse authResponse=userService.LoginUser(loginRequest.getEmail(),loginRequest.getPassword());
            return ResponseEntity.ok(new ApiResponse<>("success","Login successful",authResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("error",e.getMessage(),null));
        }
    }

}
