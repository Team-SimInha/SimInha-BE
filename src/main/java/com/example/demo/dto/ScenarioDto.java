package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.demo.domain.Scenario;
import com.example.demo.domain.ScenarioPlacedItem;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

public class ScenarioDto {

    /** 저장/수정 요청. 프론트 storage.js의 saveScenario 입력 구조와 동일. */
    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class SaveRequest {
        private String name;
        private String nickname;
        private List<PlacedItemPayload> items;
        /** 프론트 metrics 객체를 그대로 전달 (구조 자유) */
        private JsonNode metrics;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class PlacedItemPayload {
        private String id;               // 클라이언트 id
        private String type;             // itemType
        private Double lng;
        private Double lat;
        private Integer qty;
        private Double effectiveCoeff;
        private String locationName;
        private String zoneId;
        private String zoneType;
        private String zoneName;
        private Integer zoneYear;
        private Integer zoneFloors;
        private String zoneNote;
        private String zoneReason;
    }

    @Getter @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String name;
        private String nickname;
        private JsonNode metrics;
        private List<PlacedItemPayload> items;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter @AllArgsConstructor @Builder
    public static class Summary {
        private Long id;
        private String name;
        private String nickname;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    public static PlacedItemPayload toPayload(ScenarioPlacedItem it) {
        return new PlacedItemPayload(
                it.getClientId(),
                it.getItemType(),
                it.getLng(),
                it.getLat(),
                it.getQty(),
                it.getEffectiveCoeff(),
                it.getLocationName(),
                it.getZoneId(),
                it.getZoneType(),
                it.getZoneName(),
                it.getZoneYear(),
                it.getZoneFloors(),
                it.getZoneNote(),
                it.getZoneReason()
        );
    }

    public static Summary toSummary(Scenario s) {
        return Summary.builder()
                .id(s.getId())
                .name(s.getName())
                .nickname(s.getNickname())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
