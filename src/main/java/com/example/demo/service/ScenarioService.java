package com.example.demo.service;

import com.example.demo.domain.Scenario;
import com.example.demo.domain.ScenarioPlacedItem;
import com.example.demo.dto.ScenarioDto;
import com.example.demo.dto.ScenarioDto.PlacedItemPayload;
import com.example.demo.dto.ScenarioDto.Response;
import com.example.demo.dto.ScenarioDto.SaveRequest;
import com.example.demo.dto.ScenarioDto.Summary;
import com.example.demo.repository.ScenarioPlacedItemRepository;
import com.example.demo.repository.ScenarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final ScenarioPlacedItemRepository placedItemRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Response create(SaveRequest req) {
        String metricsJson = serializeMetrics(req.getMetrics());
        Scenario scenario = Scenario.builder()
                .name(safeName(req.getName()))
                .nickname(safeStr(req.getNickname()))
                .metricsJson(metricsJson)
                .build();
        scenario = scenarioRepository.save(scenario);

        savePlacedItems(scenario.getId(), req.getItems());
        return toResponse(scenario);
    }

    @Transactional
    public Response update(Long id, SaveRequest req) {
        Scenario scenario = scenarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SCENARIO_NOT_FOUND", "시나리오를 찾을 수 없습니다."));

        scenario.rename(req.getName());
        scenario.replaceMetrics(serializeMetrics(req.getMetrics()));
        // updated_at은 @PreUpdate에서 자동 갱신

        // 배치 아이템은 전체 교체 (단순·안전)
        placedItemRepository.deleteByScenarioId(id);
        savePlacedItems(id, req.getItems());

        return toResponse(scenario);
    }

    @Transactional(readOnly = true)
    public Response get(Long id) {
        Scenario scenario = scenarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SCENARIO_NOT_FOUND", "시나리오를 찾을 수 없습니다."));
        return toResponse(scenario);
    }

    @Transactional(readOnly = true)
    public List<Summary> list(String nickname) {
        List<Scenario> scenarios = (nickname != null && !nickname.isBlank())
                ? scenarioRepository.findByNicknameOrderByUpdatedAtDesc(nickname.trim())
                : scenarioRepository.findAllByOrderByUpdatedAtDesc();
        return scenarios.stream().map(ScenarioDto::toSummary).toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!scenarioRepository.existsById(id)) {
            throw new NotFoundException("SCENARIO_NOT_FOUND", "시나리오를 찾을 수 없습니다.");
        }
        placedItemRepository.deleteByScenarioId(id);
        scenarioRepository.deleteById(id);
    }

    // ── 헬퍼 ──

    private void savePlacedItems(Long scenarioId, List<PlacedItemPayload> items) {
        if (items == null) return;
        for (PlacedItemPayload p : items) {
            placedItemRepository.save(ScenarioPlacedItem.builder()
                    .scenarioId(scenarioId)
                    .clientId(p.getId())
                    .itemType(p.getType())
                    .lng(p.getLng())
                    .lat(p.getLat())
                    .qty(p.getQty() != null ? p.getQty() : 1)
                    .effectiveCoeff(p.getEffectiveCoeff())
                    .locationName(p.getLocationName())
                    .zoneId(p.getZoneId())
                    .zoneType(p.getZoneType())
                    .zoneName(p.getZoneName())
                    .zoneYear(p.getZoneYear())
                    .zoneFloors(p.getZoneFloors())
                    .zoneNote(p.getZoneNote())
                    .zoneReason(p.getZoneReason())
                    .build());
        }
    }

    private Response toResponse(Scenario s) {
        List<PlacedItemPayload> items = placedItemRepository.findByScenarioId(s.getId()).stream()
                .map(ScenarioDto::toPayload)
                .toList();
        return Response.builder()
                .id(s.getId())
                .name(s.getName())
                .nickname(s.getNickname())
                .metrics(parseMetrics(s.getMetricsJson()))
                .items(items)
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    private String serializeMetrics(JsonNode metrics) {
        try {
            return metrics == null ? null : objectMapper.writeValueAsString(metrics);
        } catch (Exception e) {
            return null;
        }
    }

    private JsonNode parseMetrics(String json) {
        try {
            return (json == null || json.isBlank()) ? null : objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }

    private String safeName(String name) {
        return (name == null || name.isBlank()) ? "이름 없는 시나리오" : name.trim();
    }

    private String safeStr(String s) {
        return s == null ? null : s.trim();
    }
}
