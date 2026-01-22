package com.project.fitness.service;

import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse register(RegisterRequest registerRequest) {
         User user=User.builder()
                 .email(registerRequest.getEmail())
                 .password(registerRequest.getPassword())
                 .firstName(registerRequest.getFirstName())
                 .lastName(registerRequest.getLastName())
                 .build();

        User savedUser=userRepository.save(user);
         return mapToResponse(savedUser); // return user object with generated id
    }

    //map the Saved User object to UserResponse
    private UserResponse mapToResponse(User savedUser) {
        UserResponse response=new UserResponse();

        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setPassword(savedUser.getPassword());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());

        return response;
    }
}
