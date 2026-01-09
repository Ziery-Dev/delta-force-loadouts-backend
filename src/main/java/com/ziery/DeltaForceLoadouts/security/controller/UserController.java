package com.ziery.DeltaForceLoadouts.security.controller;

import com.ziery.DeltaForceLoadouts.dto.response.BuildDtoResponse;
import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoRequest;
import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoResponse;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import com.ziery.DeltaForceLoadouts.security.service.UserService;
import com.ziery.DeltaForceLoadouts.security.userDetails.UserDetailsImpl;
import com.ziery.DeltaForceLoadouts.security.userDetails.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<UserDtoResponse> post(@RequestBody @Valid UserDtoRequest userDto) {
        var userDtoSave = userService.salvar(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoSave);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> buscarPorId(@PathVariable Long id) {
        var userDtoResponse = userService.buscarPorId(id);
        return ResponseEntity.ok(userDtoResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> buscarTodosUsuarios() {
        List<UserDtoResponse> lista = userService.buscarTodosUsuarios();
        return ResponseEntity.ok(lista);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPorId(@PathVariable Long id) {
        userService.removerPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDtoResponse> atualizarPorID(@PathVariable Long id, @RequestBody @Valid UserDtoRequest userDto   ) {
        var dtoResponse = userService.atualizarPorId(id, userDto);
        return ResponseEntity.ok(dtoResponse);
    }


    //Obtem o nome do usuario logado
    @GetMapping("/me")
    public ResponseEntity<UserDtoResponse> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new UserDtoResponse(userDetails.getUsername()));
    }


    //Lista todas as builds favoritas do usuário
    @GetMapping("/favoritos")
    public ResponseEntity<Page<BuildDtoResponse>> getFavorites(Authentication authentication,  @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable) {

        User authenticatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));
        Page<BuildDtoResponse> favoritos = userService.getFavorites(authenticatedUser.getId(), pageable);
        return ResponseEntity.ok(favoritos);
    }


    //adiciona uma build a lista de favoritos do usuario
    @PostMapping("/favoritos/{id}")
    public ResponseEntity<BuildDtoResponse> addFavorite(@PathVariable Long id, Authentication authentication) {
        User authenticatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));
        BuildDtoResponse buildAdd = userService.addFavorite(id, authenticatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(buildAdd);
    }


    @DeleteMapping("/favoritos/{id}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id, Authentication authentication) {
        User authenticatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));
        userService.removeFavorite(id, authenticatedUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/bloquear-usuario/{id}")
    public ResponseEntity<Void> bloquearUsuario(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/desbloquear-usuario/{id}")
    public ResponseEntity<Void> desbloquearUsuario(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
