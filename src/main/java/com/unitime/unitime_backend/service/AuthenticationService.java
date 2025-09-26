package com.unitime.unitime_backend.service;

import com.unitime.unitime_backend.dto.jwt.JwtResponseDTO;
import com.unitime.unitime_backend.dto.user.UserLoginDTO;
import com.unitime.unitime_backend.dto.user.UserRegisterDTO;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.entity.UserRole;
import com.unitime.unitime_backend.exception.ResourceNotFoundException;
import com.unitime.unitime_backend.repository.UserRepository;
import lombok.Data;
import lombok.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.unitime.unitime_backend.mapper.UserMapper;

@Service
@Data
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtService jwtService;


    //todo : implement the userMapper.toEntity
    public JwtResponseDTO register(UserRegisterDTO input) {
        User newUser = User
                .builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(UserRole.STUDENT)
                .build();

        userRepository.save(newUser);
        String jwtToken = jwtService.generateToken(newUser);

        return JwtResponseDTO.builder()
                .token(jwtToken)
                .expiration(jwtService.getJwtExpiration())
                .build();
    }

    public JwtResponseDTO login(UserLoginDTO input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email : "+ input.getEmail() +" not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        String jwtToken = jwtService.generateToken(user);
        return JwtResponseDTO.builder()
                .token(jwtToken)
                .expiration(jwtService.getJwtExpiration())
                .build();
    }
}
