package com.ziery.DeltaForceLoadouts.controller;


import com.ziery.DeltaForceLoadouts.dto.request.WeaponDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.WeaponDtoResponse;
import com.ziery.DeltaForceLoadouts.service.WeaponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arma")
@RequiredArgsConstructor
public class WeaponController {

    private  final WeaponService service;

    @GetMapping
    public ResponseEntity<List<WeaponDtoResponse>> getAllWeapons() {
        List<WeaponDtoResponse> list = service.getAllWeapons();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<WeaponDtoResponse> createWeapon( @RequestBody @Valid  WeaponDtoRequest request) {
        var weaponSave = service.createWeapon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(weaponSave);

    }

    @GetMapping("/{id}")
    public ResponseEntity<WeaponDtoResponse> getWeaponById (@PathVariable Integer id) {
        var weapon = service.getWeaponById(id);
        return ResponseEntity.ok().body(weapon);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeWeapon (@PathVariable Integer id) {
        service.removeWeapon(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeaponDtoResponse> updateWeapon ( @PathVariable Integer id, @RequestBody  @Valid WeaponDtoRequest request) {
        var weapon = service.updateWeapon(id, request);
        return ResponseEntity.ok().body(weapon);
    }
}
