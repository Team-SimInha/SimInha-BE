package com.example.demo.service;

import com.example.demo.domain.LeaderboardEntry;
import com.example.demo.domain.Scenario;
import com.example.demo.dto.LeaderboardDto.Response;
import com.example.demo.dto.LeaderboardDto.SubmitRequest;
import com.example.demo.repository.LeaderboardRepository;
import com.example.demo.repository.ScenarioPlacedItemRepository;
import com.example.demo.repository.ScenarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioPlacedItemRepository placedItemRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Response submit(SubmitRequest req) {
        LeaderboardEntry entry;
        if (req.getScenarioId() != null) {
            entry = buildFromScenario(req);
        } else {
            entry = buildFromDirect(req);
        }
        LeaderboardEntry saved = leaderboardRepository.save(entry);
        return Response.from(saved, 0);  // rank는 단일 제출 시 의미 없음
    }

    @Transactional(readOnly = true)
    public List<Response> list(String sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        List<LeaderboardEntry> entries = switch (sortBy == null ? "" : sortBy) {
            case "efficiency_score" ->
                leaderboardRepository.findAllByOrderByEfficiencyScoreDescCreatedAtDesc(pageable);
            case "energy_kwh" ->
                leaderboardRepository.findAllByOrderByEnergyKwhDescCreatedAtDesc(pageable);
            default ->
                leaderboardRepository.findAllByOrderByTotalSavingDescCreatedAtDesc(pageable);
        };

        List<Response> result = new ArrayList<>(entries.size());
        int baseRank = page * size + 1;
        for (int i = 0; i < entries.size(); i++) {
            result.add(Response.from(entries.get(i), baseRank + i));
        }
        return result;
    }

    /** 시나리오 스냅샷을 신뢰원으로 사용 (점수 조작 방지) */
    private LeaderboardEntry buildFromScenario(SubmitRequest req) {
        Scenario scenario = scenarioRepository.findById(req.getScenarioId())
                .orElseThrow(() -> new NotFoundException("SCENARIO_NOT_FOUND", "시나리오를 찾을 수 없습니다."));

        JsonNode metrics = readMetrics(scenario.getMetricsJson());
        JsonNode user = metrics == null ? null : metrics.path("user");
        int itemCount = placedItemRepository.findByScenarioId(scenario.getId()).size();

        return LeaderboardEntry.builder()
                .scenarioId(scenario.getId())
                .nickname(safeNickname(req.getNickname(), scenario.getNickname()))
                .totalSaving(d(user, "netSaving"))
                .energyKwh(d(user, "energyKwh"))
                .efficiencyScore(d(user, "efficiencyScore"))
                .totalCost(l(user, "totalCost"))
                .itemCount(itemCount)
                .build();
    }

    /** 시나리오 없이 직접 제출 (storage.js의 saveLeaderboardEntry와 동등) */
    private LeaderboardEntry buildFromDirect(SubmitRequest req) {
        return LeaderboardEntry.builder()
                .scenarioId(null)
                .nickname(safeNickname(req.getNickname(), null))
                .totalSaving(req.getTotalSaving() != null ? req.getTotalSaving() : 0.0)
                .energyKwh(req.getEnergyKwh() != null ? req.getEnergyKwh() : 0.0)
                .efficiencyScore(req.getEfficiencyScore() != null ? req.getEfficiencyScore() : 0.0)
                .totalCost(req.getTotalCost() != null ? req.getTotalCost() : 0L)
                .itemCount(req.getItemCount() != null ? req.getItemCount() : 0)
                .build();
    }

    private String safeNickname(String requestNick, String fallback) {
        if (requestNick != null && !requestNick.isBlank()) return requestNick.trim();
        if (fallback != null && !fallback.isBlank()) return fallback.trim();
        return "익명";
    }

    private JsonNode readMetrics(String json) {
        try {
            return (json == null || json.isBlank()) ? null : objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }

    private double d(JsonNode node, String field) {
        if (node == null || node.isMissingNode() || node.isNull()) return 0.0;
        JsonNode v = node.path(field);
        return v.isNumber() ? v.asDouble() : 0.0;
    }

    private long l(JsonNode node, String field) {
        if (node == null || node.isMissingNode() || node.isNull()) return 0L;
        JsonNode v = node.path(field);
        return v.isNumber() ? v.asLong() : 0L;
    }
}
