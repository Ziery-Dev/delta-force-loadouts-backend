package com.ziery.DeltaForceLoadouts.security.controller;

import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoRequest;
import com.ziery.DeltaForceLoadouts.security.dto.user.UserDtoResponse;
import com.ziery.DeltaForceLoadouts.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}
