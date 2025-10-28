package com.ziery.DeltaForceLoadouts.service;


import com.ziery.DeltaForceLoadouts.dto.request.OperatorDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.OperatorDtoResponse;
import com.ziery.DeltaForceLoadouts.entity.Operator;
import com.ziery.DeltaForceLoadouts.exception.DadoDuplicadoException;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.repository.OperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OperatorService {

    private final OperatorRepository operatorRepository;

    //Lista todos operadores
    public List<OperatorDtoResponse> listAllOperators() {
        List<Operator> operators = operatorRepository.findAll();
        return operators.stream()
                .map(op -> new OperatorDtoResponse(op.getId(), op.getName(), op.getCategory()))
                .toList();

    }

    //salvar operador
    public OperatorDtoResponse createOperator(OperatorDtoRequest operatorDtoRequest) {
        var verifiOperator = operatorRepository.findByName(operatorDtoRequest.name());
        if (verifiOperator.isPresent()) {
            throw new DadoDuplicadoException("Operador já cadastrado");
        }
        Operator operator = new Operator();
        operator.setName(operatorDtoRequest.name());
        operator.setCategory(operatorDtoRequest.category());
        var operatorSave = operatorRepository.save(operator);
        return new OperatorDtoResponse(operatorSave.getId(), operatorSave.getName(), operatorDtoRequest.category());
    }

    //Buscar operador por id
    public OperatorDtoResponse getOperatorbyId(Integer id) {
        var operator = operatorRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Operador não econtrado na base de dados") );
        return new OperatorDtoResponse(operator.getId(), operator.getName(), operator.getCategory());
    }


    //remover operador por id
    public void removeOperatorById(Integer id) {
        var operator = operatorRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Operador não econtrado na base de dados par remoção") );
        operatorRepository.delete(operator);
    }


    //Atualizar operador
    public OperatorDtoResponse updateOperator(OperatorDtoRequest operatorDtoRequest, Integer id) {
        var veryOperatorId = operatorRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Operador não econtrado para atualização"));
        var verifyOperatorName = operatorRepository.findByName(operatorDtoRequest.name());
        if (verifyOperatorName.isPresent() && !verifyOperatorName.get().getId().equals(id)) {
            throw new DadoDuplicadoException("Operador com esse nome já foi cadastrado");
        }
        veryOperatorId.setName(operatorDtoRequest.name());
        veryOperatorId.setCategory(operatorDtoRequest.category());
        var operatorSave = operatorRepository.save(veryOperatorId);
        return new OperatorDtoResponse(operatorSave.getId(), operatorSave.getName(), operatorDtoRequest.category());

    }
}
