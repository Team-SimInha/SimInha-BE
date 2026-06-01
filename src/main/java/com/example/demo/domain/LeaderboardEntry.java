package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 리더보드 제출 기록.
 * 프론트 storage.js의 saveLeaderboardEntry 구조 그대로 (snake_case 필드명 유지).
 */
@Entity
@Table(name = "leaderboard")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_id")
    private Long scenarioId;         // Nullable: 시나리오 없이 제출도 허용

    /** 소유자 — 추후 확장용 (A 방식: 지금은 null) */
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String nickname;         // 비어오면 서비스에서 '익명'으로 치환

    @Column(name = "total_saving", nullable = false)
    private Double totalSaving;      // metrics.user.netSaving

    @Column(name = "energy_kwh", nullable = false)
    private Double energyKwh;        // metrics.user.energyKwh

    @Column(name = "efficiency_score", nullable = false)
    private Double efficiencyScore;  // metrics.user.efficiencyScore

    @Column(name = "total_cost", nullable = false)
    private Long totalCost;          // metrics.user.totalCost

    @Column(name = "item_count", nullable = false)
    private Integer itemCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
