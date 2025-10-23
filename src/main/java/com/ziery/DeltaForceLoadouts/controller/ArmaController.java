package com.ziery.DeltaForceLoadouts.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/armas")
public class ArmaController {

    @GetMapping
    public String armas(){
        return "Este é apenas um teste de armas";
    }
}
