package com.ziery.DeltaForceLoadouts.service;


import com.ziery.DeltaForceLoadouts.domain.specification.BuildSpec;
import com.ziery.DeltaForceLoadouts.dto.request.BuildDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.BuildDtoResponse;
import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRange;
import com.ziery.DeltaForceLoadouts.entity.Weapon;
import com.ziery.DeltaForceLoadouts.exception.AcessoNegadoException;
import com.ziery.DeltaForceLoadouts.exception.DadoDuplicadoException;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.repository.BuildRatingRepository;
import com.ziery.DeltaForceLoadouts.repository.BuildRepository;
import com.ziery.DeltaForceLoadouts.repository.WeaponRepository;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildService {

    private final BuildRepository buildRepository;
    private final WeaponRepository weaponRepository;
    private final UserRepository userRepository;
    private final BuildRatingRepository ratingRepository;



    public BuildDtoResponse createBuild(BuildDtoRequest request, User authenticatedUser) {
        var verifyCode = buildRepository.findByCode(request.code());
        if (verifyCode.isPresent()) {
            throw new DadoDuplicadoException("Já existe uma build com esse código");
        }

        Weapon weapon = weaponRepository.findById(request.weaponId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Arma não encontrada na base de dados"));

        //  Busca o usuário completo pelo nome de usuário do token
        User userFromDb = userRepository.findByUsername(authenticatedUser.getUsername())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado na base de dados"));

        Build build = new Build();
        build.setCode(request.code());
        build.setDescription(request.description());
        build.setDistance_range(request.distance_range());
        build.setWeapon(weapon);
        build.setCreator(userFromDb); // associa o criador corretamente

        Build buildSave = buildRepository.save(build);
        return new BuildDtoResponse(buildSave);
    }


    //Lista todas as builds atraves de alguma ordenação (default: mais recente)
    public List<BuildDtoResponse> getBuildsSorted(
            String sort,
            String order,
            Long currentUserId,
            BuildRange distanceRange
    ) {
        // 1. Aplica filtro
        Specification<Build> spec = BuildSpec.byDistanceRange(distanceRange);

        // 2. Busca filtrada (antes de ordenar)
        List<Build> filtered = buildRepository.findAll(spec);

        List<Build> ordered;

        // 3. Ordenação
        if (sort.equalsIgnoreCase("date")) {

            if (order.equalsIgnoreCase("asc")) {
                ordered = filtered.stream()
                        .sorted(Comparator.comparing(Build::getCreatedAt))
                        .toList();

            } else if (order.equalsIgnoreCase("desc")) {
                ordered = filtered.stream()
                        .sorted(Comparator.comparing(Build::getCreatedAt).reversed())
                        .toList();

            } else {
                throw new IllegalArgumentException("Order inválido: 'asc' ou 'desc'");
            }

        }

        else if (sort.equalsIgnoreCase("likes")) {

            if (order.equalsIgnoreCase("asc")) {
                ordered = filtered.stream()
                        .sorted(Comparator.comparing(Build::getLikeCount))
                        .toList();

            } else if (order.equalsIgnoreCase("desc")) {
                ordered = filtered.stream()
                        .sorted(Comparator.comparing(Build::getLikeCount).reversed())
                        .toList();

            } else {
                throw new IllegalArgumentException("Order inválido: 'asc' ou 'desc'");
            }
        }
        else if (sort.equalsIgnoreCase("dislikes")) {

            if (order.equalsIgnoreCase("asc")) {
                ordered = filtered.stream()
                        .sorted(Comparator.comparing(Build::getDislikeCount))
                        .toList();

            } else if (order.equalsIgnoreCase("desc")) {
                ordered = filtered.stream()
                        .sorted(Comparator.comparing(Build::getDislikeCount).reversed())
                        .toList();

            } else {
                throw new IllegalArgumentException("Order inválido: 'asc' ou 'desc'");
            }
        }

        else {
            throw new IllegalArgumentException("Sort inválido: use 'date', 'likes' ou 'dislikes'");
        }

        // 4. Montagem DTO
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ordered.stream()
                .map(b -> new BuildDtoResponse(b, currentUser))
                .toList();
    }


    //Busca uma build pelo id
    public BuildDtoResponse getBuildById(Long id) {
        var verifyBuild = buildRepository.findById(id).orElseThrow(()-> new DadoNaoEncontradoException("Build não econtrada na base de dados"));
        return new BuildDtoResponse(verifyBuild);

    }

    //Remove uma build por ID
    public void removeBuildById(Long id,  User authenticatedUser) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Build não encontrada na base de dados"));

        boolean isCreator = build.getCreator().getId().equals(authenticatedUser.getId());
        boolean isAdmin = authenticatedUser.getRole().toString().equals("ADMIN");

        if (!isCreator && !isAdmin) {
            throw new AcessoNegadoException("Você não tem permissão para remover esta build");
        }

        buildRepository.delete(build);

    }

    //Atualiza build se for criador da build ou se for usuário admin
    public BuildDtoResponse updateBuild(BuildDtoRequest request, Long id, User authenticatedUser) {
        // Busca build existente
        var existingBuild = buildRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Build não encontrada na base de dados"));

        // Verifica permissão: criador ou admin
        boolean isCreator = existingBuild.getCreator().getId().equals(authenticatedUser.getId());
        boolean isAdmin = authenticatedUser.getRole().toString().equals("ROLE_ADMIN");

        if (!isCreator && !isAdmin) {
            throw new AcessoNegadoException("Você não tem permissão para editar esta build");
        }

        // Verifica duplicidade de código
        var verifyCode = buildRepository.findByCode(request.code());
        if (verifyCode.isPresent() && !verifyCode.get().getId().equals(id)) {
            throw new DadoDuplicadoException("Já existe uma build com esse código");
        }

        // Busca a arma
        Weapon weapon = weaponRepository.findById(request.weaponId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Arma não encontrada na base de dados"));

        // Atualiza os dados
        existingBuild.setCode(request.code());
        existingBuild.setDescription(request.description());
        existingBuild.setDistance_range(request.distance_range());
        existingBuild.setWeapon(weapon);

        var buildSave = buildRepository.save(existingBuild);
        return new BuildDtoResponse(buildSave);
    }

    public List<BuildDtoResponse> getBuildsByCreatorId(Long creatorId) {
        List<Build> builds = buildRepository.findByCreatorId(creatorId);
        return builds.stream().map(BuildDtoResponse::new).toList();
    }








}

