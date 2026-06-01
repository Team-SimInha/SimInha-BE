package com.example.demo.service;

import com.example.demo.domain.AiReport;
import com.example.demo.dto.ReportDto.*;
import com.example.demo.repository.AiReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AiReportRepository reportRepository;
    // private final ScenarioRepository scenarioRepository; // 시나리오 존재 검증용 (시나리오 엔티티 추가 후 연결)

    @Transactional
    public Response save(Long scenarioId, SaveRequest req) {
        // TODO: 시나리오 엔티티 추가 후 scenarioRepository.existsById(scenarioId) 체크, 없으면 예외

        AiReport report = reportRepository.findByScenarioId(scenarioId)
                .orElseGet(() -> AiReport.builder().scenarioId(scenarioId).build());

        AiReport updated = AiReport.builder()
                .id(report.getId()) // 기존 있으면 갱신, 없으면 null → 신규
                .scenarioId(scenarioId)
                .formatVersion(req.getFormatVersion())
                .summary(req.getSummary())
                .strengths(req.getStrengths())
                .warnings(req.getWarnings())
                .recommendations(req.getRecommendations())
                .notes(req.getNotes())
                .model(req.getModel())
                .reportCreatedAt(req.getCreatedAt())
                .savedAt(LocalDateTime.now())
                .build();

        return toResponse(reportRepository.save(updated));
    }

    @Transactional(readOnly = true)
    public Response get(Long scenarioId) {
        AiReport report = reportRepository.findByScenarioId(scenarioId)
                .orElseThrow(() -> new ReportNotFoundException(scenarioId));
        return toResponse(report);
    }

    private Response toResponse(AiReport r) {
        return Response.builder()
                .id(r.getId())
                .scenarioId(r.getScenarioId())
                .formatVersion(r.getFormatVersion())
                .summary(r.getSummary())
                .strengths(r.getStrengths())
                .warnings(r.getWarnings())
                .recommendations(r.getRecommendations())
                .notes(r.getNotes())
                .model(r.getModel())
                .reportCreatedAt(r.getReportCreatedAt())
                .savedAt(r.getSavedAt())
                .build();
    }
}
