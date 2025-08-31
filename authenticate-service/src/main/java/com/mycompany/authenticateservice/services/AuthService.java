package com.mycompany.authenticateservice.services;

import com.mycompany.authenticateservice.Repository.UserRepository;
import com.mycompany.authenticateservice.dto.AuthRequest;
import com.mycompany.authenticateservice.dto.RegisterRequest;
import com.mycompany.authenticateservice.model.User;
import com.mycompany.authenticateservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authmanager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public void register (RegisterRequest request){
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
    }

    public String authenticate(AuthRequest request) {
        authmanager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails user = (UserDetails) userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        return jwtUtil.generateToken(user);
    }
}
