package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.PresetPlacementDto.BaselineResponse;
import com.example.demo.dto.PresetPlacementDto.Response;
import com.example.demo.service.PresetPlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/preset-placements")
@RequiredArgsConstructor
public class PresetPlacementController {

    private final PresetPlacementService presetService;

    @GetMapping
    public ApiResponse<List<Response>> list() {
        return ApiResponse.ok(presetService.findAll());
    }

    @GetMapping("/baseline")
    public ApiResponse<BaselineResponse> baseline() {
        return ApiResponse.ok(presetService.calculateBaseline());
    }
}
