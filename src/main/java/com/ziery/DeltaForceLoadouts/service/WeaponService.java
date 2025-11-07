package com.ziery.DeltaForceLoadouts.service;


import com.ziery.DeltaForceLoadouts.dto.request.WeaponDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.OperatorDtoResponse;
import com.ziery.DeltaForceLoadouts.dto.response.WeaponDtoResponse;
import com.ziery.DeltaForceLoadouts.entity.Operator;
import com.ziery.DeltaForceLoadouts.entity.Weapon;
import com.ziery.DeltaForceLoadouts.exception.DadoDuplicadoException;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.repository.OperatorRepository;
import com.ziery.DeltaForceLoadouts.repository.WeaponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeaponService{

    private final WeaponRepository weaponRepository;
    private final OperatorRepository operatorRepository;



    //Listar todas as armas
    public List<WeaponDtoResponse> getAllWeapons() {
        return weaponRepository.findAll()
                .stream()
                .map(WeaponDtoResponse::new)
                .toList();
    }


    //Salvar nova arma
    public WeaponDtoResponse createWeapon(WeaponDtoRequest weaponDtoRequest){
        var verfyWeapon = weaponRepository.findByName(weaponDtoRequest.name());
        if (verfyWeapon.isPresent()){
            throw new DadoDuplicadoException("Arma com esse nome já cadastrada");
        }

        //converte o set de ids de weaponRequest em set de operators
        Set<Operator> operators = weaponDtoRequest.operatorIds().stream()
                .map(id -> operatorRepository.findById(id)
                        .orElseThrow(() -> new DadoNaoEncontradoException("Operador com id " + id + " Não encontrado")))
                .collect(Collectors.toSet());

        Weapon weapon = new Weapon();
        weapon.setName(weaponDtoRequest.name());
        weapon.setCategory(weaponDtoRequest.category());
        weapon.setImgUrl(weaponDtoRequest.imgUrl());
        weapon.setCompatibleOperators(operators);

        Weapon weaponSave = weaponRepository.save(weapon);

        return new WeaponDtoResponse(weaponSave);
    }

    //Remover arma
    public void removeWeapon (Integer id){
        weaponRepository.findById(id).orElseThrow(()-> new DadoDuplicadoException("Arma não encontrada na base de dados para remoção"));
        weaponRepository.deleteById(id);
    }


    //Buscar arma por id
    public WeaponDtoResponse getWeaponById(Integer id) {
        var weapon = weaponRepository.findById(id).orElseThrow(()-> new DadoDuplicadoException("Arma não encontrada na base de dados"));
        return new WeaponDtoResponse(weapon);

    }

    public WeaponDtoResponse updateWeapon (Integer id, WeaponDtoRequest request){
        var verifyWeaponId = weaponRepository.findById(id).orElseThrow(()-> new DadoDuplicadoException("Arma não encontrada na base de dados"));
        var verifyName = weaponRepository.findByName(request.name());
        if (verifyName.isPresent() && !verifyName.get().getId().equals(id)){
            throw new DadoDuplicadoException("Nome de arma já cadastrada");
        }

        //converte o set de ids de weaponRequest em set de operators
        Set<Operator> operators = request.operatorIds().stream()
                .map(idOp -> operatorRepository.findById(idOp)
                        .orElseThrow(() -> new DadoNaoEncontradoException("Operador com id " + id + " Não encontrado")))
                .collect(Collectors.toSet());


        verifyWeaponId.setName(request.name());
        verifyWeaponId.setCategory(request.category());
        verifyWeaponId.setImgUrl(request.imgUrl());
        verifyWeaponId.setCompatibleOperators(operators);
        Weapon weaponSave = weaponRepository.save(verifyWeaponId);
        return new WeaponDtoResponse(weaponSave);



    }


}
