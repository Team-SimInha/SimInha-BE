package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.EquipmentDto.Response;
import com.example.demo.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ApiResponse<List<Response>> list() {
        return ApiResponse.ok(equipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Response> get(@PathVariable Long id) {
        return ApiResponse.ok(equipmentService.findById(id));
    }
}
