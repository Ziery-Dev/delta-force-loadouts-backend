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
import com.ziery.DeltaForceLoadouts.exception.RateLimitException;
import com.ziery.DeltaForceLoadouts.rateLimit.SimpleRateLimiter;
import com.ziery.DeltaForceLoadouts.repository.BuildRatingRepository;
import com.ziery.DeltaForceLoadouts.repository.BuildRepository;
import com.ziery.DeltaForceLoadouts.repository.WeaponRepository;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final SimpleRateLimiter rateLimiter;



    public BuildDtoResponse createBuild(BuildDtoRequest request, User authenticatedUser) {
        //Limitação de criação de build por usuário
        String userKey = authenticatedUser.getUsername(); // geralmente username

        if (!rateLimiter.allow(userKey)) {
            throw new RateLimitException("Muitas tentativas de cadastro de builds em pouco tempo, tente novamente em alguns segundos");
        }


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
    public Page<BuildDtoResponse> getBuildsSorted(
            String sort,
            String order,
            Long currentUserId,
            BuildRange distanceRange,
            String search,
            Pageable pageable
    ) {

        Specification<Build> spec = null;

        if (distanceRange != null) {
            spec = BuildSpec.byDistanceRange(distanceRange);
        }

        if (search != null && !search.isBlank()) {
            spec = spec == null
                    ? BuildSpec.search(search)
                    : spec.and(BuildSpec.search(search));
        }

        // define ordenação
        Sort sortConfig = order.equalsIgnoreCase("asc")
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageableSorted = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortConfig
        );

        Page<Build> page = buildRepository.findAll(spec, pageableSorted);

        final User currentUser =
                currentUserId != null
                        ? userRepository.findById(currentUserId).orElse(null)
                        : null;

        return page.map(build -> new BuildDtoResponse(build, currentUser));
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

    //Busca build pelo id do criador

    public Page<BuildDtoResponse> getBuildsByCreatorId(Long creatorId, Pageable pageable) {
        Page <Build> page = buildRepository.findByCreatorId(creatorId, pageable);
        return page.map(BuildDtoResponse::new);
    }


    //busca as builds pelo nome do criador ou nome da arma
    public Page<BuildDtoResponse> search(
            String search,
            Pageable pageable
    ) {
        return buildRepository
                .findAll(BuildSpec.search(search), pageable)
                .map(BuildDtoResponse::new);
    }









}

