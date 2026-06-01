package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.ScenarioDto.Response;
import com.example.demo.dto.ScenarioDto.SaveRequest;
import com.example.demo.dto.ScenarioDto.Summary;
import com.example.demo.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/scenarios")
@RequiredArgsConstructor
public class ScenarioController {

    private final ScenarioService scenarioService;

    @PostMapping
    public ApiResponse<Response> create(@RequestBody SaveRequest req) {
        return ApiResponse.ok(scenarioService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Response> update(@PathVariable Long id, @RequestBody SaveRequest req) {
        return ApiResponse.ok(scenarioService.update(id, req));
    }

    @GetMapping("/{id}")
    public ApiResponse<Response> get(@PathVariable Long id) {
        return ApiResponse.ok(scenarioService.get(id));
    }

    @GetMapping
    public ApiResponse<List<Summary>> list(@RequestParam(required = false) String nickname) {
        return ApiResponse.ok(scenarioService.list(nickname));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        scenarioService.delete(id);
        return ApiResponse.ok(null);
    }
}
