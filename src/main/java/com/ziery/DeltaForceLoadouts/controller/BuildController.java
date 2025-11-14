package com.ziery.DeltaForceLoadouts.controller;


import com.ziery.DeltaForceLoadouts.dto.request.BuildDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.BuildDtoResponse;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import com.ziery.DeltaForceLoadouts.security.service.UserService;
import com.ziery.DeltaForceLoadouts.security.userDetails.UserDetailsImpl;
import com.ziery.DeltaForceLoadouts.service.BuildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/build")
public class BuildController {

    private final BuildService buildService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<BuildDtoResponse> createBuild( @RequestBody @Valid BuildDtoRequest request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User authenticatedUser = userDetails.getUser(); // método que retorna a entidade User
        BuildDtoResponse response = buildService.createBuild(request, authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BuildDtoResponse>> getAllBuilds() {
        List <BuildDtoResponse> builds = buildService.getAllBuilds();
        return ResponseEntity.status(HttpStatus.OK).body(builds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildDtoResponse> getBuildById(@PathVariable Long id) {
        var build = buildService.getBuildById(id);
        return ResponseEntity.status(HttpStatus.OK).body(build);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBuildById(@PathVariable Long id, Authentication authentication) {
        User authenticatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));

        buildService.removeBuildById(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildDtoResponse> removeBuildById(@PathVariable Long id,
                                                        @RequestBody BuildDtoRequest request,
                                                        Authentication authentication) {
        User authenticatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));

        BuildDtoResponse response = buildService.updateBuild(request, id, authenticatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/minhas-builds")
    public ResponseEntity<List<BuildDtoResponse>> getBuildsByCreatorId(Authentication authentication) {
        User authenticatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado"));

        List<BuildDtoResponse> builds = buildService.getBuildsByCreatorId(authenticatedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(builds);

    }




}
