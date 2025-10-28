package com.ziery.DeltaForceLoadouts.service;


import com.ziery.DeltaForceLoadouts.dto.request.BuildDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.BuildDtoResponse;
import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.Weapon;
import com.ziery.DeltaForceLoadouts.exception.DadoDuplicadoException;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.repository.BuildRepository;
import com.ziery.DeltaForceLoadouts.repository.OperatorRepository;
import com.ziery.DeltaForceLoadouts.repository.WeaponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildService {

    private final BuildRepository buildRepository;
    private final WeaponRepository weaponRepository;

    public BuildDtoResponse createBuild(BuildDtoRequest request) {
        var verifyCode = buildRepository.findByCode(request.code());
        if(verifyCode.isPresent()) {
             throw new DadoDuplicadoException("Já existe uma build com esse código");
        }
        Weapon weapon = weaponRepository.findById(request.weaponId()).orElseThrow(()-> new DadoNaoEncontradoException("Arma não econtrada na base de dados"));

        Build build = new Build();
        build.setCode(request.code());
        build.setDescription(request.description());
        build.setDistance_range(request.distance_range());
        build.setWeapon(weapon);

        System.out.println("id arma econtrada: " + weapon.getId()+ " " + weapon.getName());
        var buildSave = buildRepository.save(build);

        return new BuildDtoResponse(buildSave);

    }
    public List<BuildDtoResponse> getAllBuilds() {
        List<Build> builds = buildRepository.findAll();
        return builds.stream().map(BuildDtoResponse::new).toList();
    }


    public BuildDtoResponse getBuildById(Long id) {
        var verifyBuild = buildRepository.findById(id).orElseThrow(()-> new DadoNaoEncontradoException("Build não econtrada na base de dados"));
        return new BuildDtoResponse(verifyBuild);

    }
    public void removeBuildById(Long id) {
        buildRepository.findById(id).orElseThrow(()-> new DadoNaoEncontradoException("Build não econtrada na base de dados"));
        buildRepository.deleteById(id);

    }

    public BuildDtoResponse updateBuild(BuildDtoRequest request, Long id) {
        var verifyCode = buildRepository.findByCode(request.code());
        var verifyId = buildRepository.findById(id).orElseThrow(()-> new DadoNaoEncontradoException("Build não encontrada na base de dados"));
        if(verifyCode.isPresent() && !verifyCode.get().getId().equals(id)) {
            throw new DadoDuplicadoException("Já existe uma build com esse código");
        }

        Weapon weapon = weaponRepository.findById(request.weaponId()).orElseThrow(()-> new DadoNaoEncontradoException("Arma não econtrada na base de dados"));

        verifyId.setCode(request.code());
        verifyId.setDescription(request.description());
        verifyId.setDistance_range(request.distance_range());
        verifyId.setWeapon(weapon);

        var buildSave = buildRepository.save(verifyId);

        return new BuildDtoResponse(buildSave);

    }
}
