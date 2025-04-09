package com.example.Campus.Connect.services;


import com.example.Campus.Connect.models.User;
import com.example.Campus.Connect.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class UserService {

    @Autowired
    UserRepository userRepository;

    public User RegisterUser(User user) {
        User newUser=new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        User savedUser=userRepository.save(newUser);
        return savedUser;
    }

}
