package com.ziery.DeltaForceLoadouts.controller;


import com.ziery.DeltaForceLoadouts.dto.request.BuildDtoRequest;
import com.ziery.DeltaForceLoadouts.dto.response.BuildDtoResponse;
import com.ziery.DeltaForceLoadouts.service.BuildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/build")
public class BuildController {

    private final BuildService buildService;

    @PostMapping
    public ResponseEntity<BuildDtoResponse> createBuild(@RequestBody  @Valid BuildDtoRequest request) {
        var response = buildService.createBuild(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BuildDtoResponse>> getAllBuilds() {
        List <BuildDtoResponse> builds = buildService.getAllBuilds();
        return ResponseEntity.status(HttpStatus.OK).body(builds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildDtoResponse> getBuildById(@PathVariable Long id) {
        var build = buildService.getBuildById(id);
        return ResponseEntity.status(HttpStatus.OK).body(build);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBuildById(@PathVariable Long id) {
        buildService.removeBuildById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildDtoResponse> updateBuildById(@RequestBody @Valid BuildDtoRequest request, @PathVariable Long id) {
        var build = buildService.updateBuild(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(build);
    }







}
