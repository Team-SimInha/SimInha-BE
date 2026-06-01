package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.ReportDto.*;
import com.example.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scenarios/{scenarioId}/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ApiResponse<Response> save(@PathVariable Long scenarioId,
                                      @RequestBody SaveRequest req) {
        return ApiResponse.ok(reportService.save(scenarioId, req));
    }

    @GetMapping
    public ApiResponse<Response> get(@PathVariable Long scenarioId) {
        return ApiResponse.ok(reportService.get(scenarioId));
    }
}
