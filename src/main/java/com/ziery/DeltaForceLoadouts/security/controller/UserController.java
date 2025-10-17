package com.ziery.DeltaForceLoadouts.security.controller;

import com.ziery.DeltaForceLoadouts.security.dto.UserDto;
import com.ziery.DeltaForceLoadouts.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> post(@RequestBody UserDto userDto) {
        UserDto userDtoSave = userService.salvar(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoSave);
    }

}
