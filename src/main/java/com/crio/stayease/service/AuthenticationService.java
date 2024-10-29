package com.crio.stayease.service;

import com.crio.stayease.Role;
import com.crio.stayease.exception.InvalidCredentialsException;
import com.crio.stayease.exception.UserAlreadyExistsException;
import com.crio.stayease.exception.UserNotFoundException;
import com.crio.stayease.model.User;
import com.crio.stayease.repo.UserRepository;
import com.crio.stayease.request.UserLoginDTO;
import com.crio.stayease.request.UserRegistrationDTO;
import com.crio.stayease.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String register(UserRegistrationDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole() == null ? Role.Customer : userDTO.getRole());

        userRepository.save(user);
        return jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
    }

    public String login(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials.");
        }
        return jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
    }
}
