package com.ziery.DeltaForceLoadouts.controller;

import com.ziery.DeltaForceLoadouts.dto.request.OperatorDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.OperatorDtoResponse;
import com.ziery.DeltaForceLoadouts.service.OperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operador")
@RequiredArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;

    @GetMapping
    public ResponseEntity <List<OperatorDtoResponse>> listAllOperators() {
        return ResponseEntity.ok(operatorService.listAllOperators());
    }

    @PostMapping
    public ResponseEntity<OperatorDtoResponse> createOperator(@RequestBody OperatorDtoRequest operatorDtoRequest) {
        var operatorDtoResponse = operatorService.createOperator(operatorDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(operatorDtoResponse);

    }


    @GetMapping("/{id}")
    public ResponseEntity<OperatorDtoResponse> getOperatorById(@PathVariable Integer id) {
        var operatorFind = operatorService.getOperatorbyId(id);
        return ResponseEntity.ok(operatorFind);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeOperatorById(@PathVariable Integer id) {
        operatorService.removeOperatorById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperatorDtoResponse> updateOperator(@RequestBody OperatorDtoRequest operatorDtoRequest, @PathVariable Integer id) {
        var response = operatorService.updateOperator(operatorDtoRequest, id);
        return ResponseEntity.ok().body(response);
    }



}
