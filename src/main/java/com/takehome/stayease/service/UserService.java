package com.takehome.stayease.service;

import java.util.Optional;
import com.takehome.stayease.dto.UserDto;
import com.takehome.stayease.enums.Role;
import com.takehome.stayease.model.User;
import com.takehome.stayease.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserDto userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        String rol = userDTO.getRole() != null ? userDTO.getRole().toUpperCase(): "CUSTOMER";

        if(rol.equals("CUSTOMER") || rol.equals("HOTEL_MANAGER") || rol.equals("ADMIN")) {
            user.setRole(Role.valueOf(rol));
        }else user.setRole(Role.valueOf("CUSTOMER"));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
