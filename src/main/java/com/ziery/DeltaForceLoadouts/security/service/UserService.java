package com.ziery.DeltaForceLoadouts.security.service;

import com.ziery.DeltaForceLoadouts.dto.response.BuildDtoResponse;
import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.exception.DadoDuplicadoException;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.repository.BuildRepository;
import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoRequest;
import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoResponse;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.entity.UserRoles;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import com.ziery.DeltaForceLoadouts.service.BuildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BuildRepository buildRepository;

    //Método para salvar novo usuário
    public UserDtoResponse salvar(UserDtoRequest userDto) {
        var verifiUser = userRepository.findByUsername(userDto.getUsername());
        if (verifiUser.isPresent()) {
            throw new DadoDuplicadoException("Esse nome de usuário já foi cadastrado");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(UserRoles.USUARIO);
        var userSave = userRepository.save(user);
        return new UserDtoResponse(userSave.getId(), userSave.getUsername(), userSave.getEmail(), userSave.getRole());
    }

    //Buscar usuário por id
    public UserDtoResponse buscarPorId(Long id) {
        var verifiUser = userRepository.findById(id).orElseThrow( () -> new DadoNaoEncontradoException("Usuário não econtrado"));
        return new UserDtoResponse(verifiUser.getId(), verifiUser.getUsername(), verifiUser.getEmail(), verifiUser.getRole());
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
                    userDto.setEnabled(user.isEnabled());
                    return userDto;

                }).collect(Collectors.toList());
    }

    //Remover usuário através do Id
    public void removerPorId(Long id) {
        long total = buildRepository.countByCreatorId(id);
        if(total > 0){
            throw new DataIntegrityViolationException("Usuário não pode ser removido porque possui: " + total + " builds cadastrada(s)");
        }
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
        verifId.setRole(UserRoles.USUARIO);
        var userSave = userRepository.save(verifId);
        return new UserDtoResponse(userSave.getId(), userSave.getUsername(), userSave.getEmail(), userSave.getRole());

    }

    //Listar builds favoritas
    public Page<BuildDtoResponse> getFavorites(Long userId, Pageable pageable ) {
        Page<Build> page = buildRepository.findFavoritesByUserId(userId, pageable);
        return page.map(BuildDtoResponse::new);
    }

    //adicionar build a favoritos
    @Transactional
    public BuildDtoResponse addFavorite( Long buildId, User authenticatedUser) {
        var build = buildRepository.findById(buildId).orElseThrow(()-> new DadoNaoEncontradoException("build não encontrada!"));

        if (authenticatedUser.getFavoriteBuilds().contains(build)) {
            return null; // já está nos favoritos
        }

        authenticatedUser.addFavoriteBuild(build);
        userRepository.save(authenticatedUser);
        return new BuildDtoResponse(build);

    }


    //Remover build de favoritos
    public void removeFavorite(Long buildId, User authenticatedUser) {
        var build = buildRepository.findById(buildId).orElseThrow(()-> new DadoNaoEncontradoException("build não encontrada!"));

        if (!authenticatedUser.getFavoriteBuilds().contains(build)) {
            return; // já não estava nos favoritos
        }
        authenticatedUser.removeFavoriteBuild(build);
        userRepository.save(authenticatedUser);
    }



    //bloqueia usuário
    public void blockUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));
            if (user.getRole() == UserRoles.ADMIN) {
                throw new IllegalStateException ("Usuário admin não pode ser bloqueado"); //Criar uma exception pra isso aqui
            }
        user.setEnabled(false);
        userRepository.save(user);
    }

    //debloqueia usuário
    public void unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));

        user.setEnabled(true);
        userRepository.save(user);

    }





}





