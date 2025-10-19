package com.ziery.DeltaForceLoadouts.security.service;

import com.ziery.DeltaForceLoadouts.exception.DadoDuplicadoException;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoRequest;
import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoResponse;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Método para salvar novo usuário
    public UserDtoResponse salvar(UserDtoRequest userDto) {
        var verifiUser = userRepository.findByUsername(userDto.getUsername());
        if (verifiUser.isPresent()) {
            throw new DadoDuplicadoException("Esse nome de usuário já foi cadastrado");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        var userSave = userRepository.save(user);
        return new UserDtoResponse(userSave.getId(), userSave.getUsername(), userSave.getRole());
    }

    //Buscar usuário por id
    public UserDtoResponse buscarPorId(Long id) {
        var verifiUser = userRepository.findById(id).orElseThrow( () -> new DadoNaoEncontradoException("Usuário não econtrado"));
        return new UserDtoResponse(verifiUser.getId(), verifiUser.getUsername(), verifiUser.getRole());
    }

    //Buscar todos usuários
    public List<UserDtoResponse> buscarTodosUsuarios() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDtoResponse userDto = new UserDtoResponse();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setRole(user.getRole());
                    return userDto;

                }).collect(Collectors.toList());
    }

    //Remover usuário através do Id
    public void removerPorId(Long id) {
        var verifiUser = userRepository.findById(id).orElseThrow( () -> new DadoNaoEncontradoException("Usuário não econtrado para remoção"));
        userRepository.delete(verifiUser);
    }

    //Atualizar usuário através do id
    public UserDtoResponse atualizarPorId(Long id,  UserDtoRequest userDto) {
        var verifId = userRepository.findById(id).orElseThrow( () -> new DadoNaoEncontradoException("Usuário não econtrado"));
        var verifiUser = userRepository.findByUsername(userDto.getUsername());
        if (verifiUser.isPresent() && !verifiUser.get().getId().equals(id)) {
            throw new DadoDuplicadoException("Esse nome de usuário já foi cadastrado");
        }
        verifId.setUsername(userDto.getUsername());
        verifId.setPassword(passwordEncoder.encode(userDto.getPassword()));
        verifId.setRole(userDto.getRole());
        var userSave = userRepository.save(verifId);
        return new UserDtoResponse(userSave.getId(), userSave.getUsername(), userSave.getRole());

    }
}





