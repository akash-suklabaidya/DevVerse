package com.example.Campus.Connect.Controller;

import com.example.Campus.Connect.Response.ApiResponse;
import com.example.Campus.Connect.models.User;
import com.example.Campus.Connect.repositories.UserRepository;
import com.example.Campus.Connect.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("auth/signup")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody User user)throws Exception {
        try{
            if(userRepository.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>("error","email already present",null));
            }

            userService.RegisterUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>("success","Registration successful",null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("error","Something went wrong",null));
        }
    }

}
