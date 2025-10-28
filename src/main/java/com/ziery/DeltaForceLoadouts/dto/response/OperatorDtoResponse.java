package com.ziery.DeltaForceLoadouts.dto.response;

import com.ziery.DeltaForceLoadouts.entity.Operator;
import com.ziery.DeltaForceLoadouts.entity.OperatorCategory;
import lombok.Data;
import lombok.RequiredArgsConstructor;


public record OperatorDtoResponse (Integer id, String name, OperatorCategory category) {

}
