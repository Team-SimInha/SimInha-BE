package com.example.demo.dto;

import com.example.demo.domain.LeaderboardEntry;
import lombok.*;
import java.time.LocalDateTime;

public class LeaderboardDto {

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class SubmitRequest {
        private Long scenarioId;       // Nullable: 시나리오 직접 참조 시
        private String nickname;
        // 직접 점수 입력 (시나리오 없는 경우)
        private Double totalSaving;
        private Double energyKwh;
        private Double efficiencyScore;
        private Long totalCost;
        private Integer itemCount;
    }

    @Getter @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private Integer rank;
        private String nickname;
        private Double totalSaving;
        private Double energyKwh;
        private Double efficiencyScore;
        private Long totalCost;
        private Integer itemCount;
        private LocalDateTime createdAt;

        public static Response from(LeaderboardEntry e, int rank) {
            return Response.builder()
                    .id(e.getId())
                    .rank(rank)
                    .nickname(e.getNickname())
                    .totalSaving(e.getTotalSaving())
                    .energyKwh(e.getEnergyKwh())
                    .efficiencyScore(e.getEfficiencyScore())
                    .totalCost(e.getTotalCost())
                    .itemCount(e.getItemCount())
                    .createdAt(e.getCreatedAt())
                    .build();
        }
    }
}
