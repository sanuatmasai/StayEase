package com.takehome.stayease.controller;

import com.takehome.stayease.config.JwtUtil;
import com.takehome.stayease.dto.AuthenticationResponse;
import com.takehome.stayease.dto.UserDto;
import com.takehome.stayease.exception.InvalidCredentialsException;
import com.takehome.stayease.model.User;
import com.takehome.stayease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDTO) {
        User user = userService.register(userDTO);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDTO) {
        User user = userService.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}