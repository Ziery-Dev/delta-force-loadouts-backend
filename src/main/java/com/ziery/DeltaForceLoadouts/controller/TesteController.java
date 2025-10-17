package com.ziery.DeltaForceLoadouts.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @RequestMapping
    public String testeGet(){
        return "Passou no teste simples";
    }
}
