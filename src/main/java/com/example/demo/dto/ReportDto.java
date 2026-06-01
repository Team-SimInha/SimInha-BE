package com.example.demo.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

public class ReportDto {

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class SaveRequest {
        private String formatVersion;
        private String summary;
        private List<String> strengths;
        private List<String> warnings;
        private List<String> recommendations;
        private List<String> notes;
        private LocalDateTime createdAt; // 프론트의 AI 생성 시각
        private String model;
    }

    @Getter @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private Long scenarioId;
        private String formatVersion;
        private String summary;
        private List<String> strengths;
        private List<String> warnings;
        private List<String> recommendations;
        private List<String> notes;
        private String model;
        private LocalDateTime reportCreatedAt;
        private LocalDateTime savedAt;
    }
}
