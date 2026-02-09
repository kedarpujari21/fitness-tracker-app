package com.project.fitness.service;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.model.UserRole;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest registerRequest) {
        UserRole role=registerRequest.getRole()!= null ? registerRequest.getRole(): UserRole.USER;

         User user=User.builder()
                 .email(registerRequest.getEmail())
                 .password(passwordEncoder.encode(registerRequest.getPassword()))
                 .firstName(registerRequest.getFirstName())
                 .lastName(registerRequest.getLastName())
                 .role(role)
                 .build();

        User savedUser=userRepository.save(user);
         return mapToResponse(savedUser); // return user object with generated id
    }

    //map the Saved User object to UserResponse
    public UserResponse mapToResponse(User savedUser) {
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

    public User authenticateUser(LoginRequest loginRequest) {
        User user=userRepository.findByEmail(loginRequest.getEmail());
        if(user==null) throw new RuntimeException("Invalid Credentials....");
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Credentials....");
        }
        return user;
    }
}
