package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.LeaderboardDto.Response;
import com.example.demo.dto.LeaderboardDto.SubmitRequest;
import com.example.demo.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @PostMapping
    public ApiResponse<Response> submit(@RequestBody SubmitRequest req) {
        return ApiResponse.ok(leaderboardService.submit(req));
    }

    @GetMapping
    public ApiResponse<List<Response>> list(
            @RequestParam(required = false, defaultValue = "total_saving") String sortBy,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ApiResponse.ok(leaderboardService.list(sortBy, page, size));
    }
}
