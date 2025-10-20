package com.unitime.unitime_backend.service;

import com.unitime.unitime_backend.dto.user.UserRegisterDTO;
import com.unitime.unitime_backend.dto.user.UserResponseDTO;
import com.unitime.unitime_backend.dto.user.UserUpdateDTO;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.entity.UserRole;
import com.unitime.unitime_backend.exception.DuplicateResourceException;
import com.unitime.unitime_backend.exception.ResourceNotFoundException;
import com.unitime.unitime_backend.mapper.UserMapper;
import com.unitime.unitime_backend.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            UserMapper mapper,
            PasswordEncoder encoder ) {
       this.userRepository = repository;
       this.userMapper = mapper;
       this.passwordEncoder = encoder;
    }

//    public getUser();

    public UserResponseDTO updateUser(long id, UserUpdateDTO requestBody) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id : "+ id));

            user.setFirstName(requestBody.getFirstName());
            user.setLastName(requestBody.getLastName());


        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO createUser(UserRegisterDTO requestBody) {
       if(userRepository.existsByEmail(requestBody.getEmail()).isPresent()) {
           throw new DuplicateResourceException("User with email : " + requestBody.getEmail() + " already exists");
       }
       User newUser = userMapper.toEntity(requestBody);

       newUser.setPassword(passwordEncoder.encode(requestBody.getPassword()));

       newUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
       newUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
       newUser.setRole(UserRole.STUDENT);

       User savedUser = userRepository.save(newUser);
       return userMapper.toResponseDTO(savedUser);
    }


}



