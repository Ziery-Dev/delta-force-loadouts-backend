package com.ziery.DeltaForceLoadouts.security.service;

import com.ziery.DeltaForceLoadouts.security.dto.UserDto;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto salvar(UserDto userDto) {
        var verifiUser = userRepository.findByUsername(userDto.getUsername());
        if (verifiUser.isPresent()) {
            throw new DataIntegrityViolationException("Usuário já cadastrado");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var userSave = userRepository.save(user);
        return new UserDto(userSave.getId(), userDto.getUsername());
    }
    }

