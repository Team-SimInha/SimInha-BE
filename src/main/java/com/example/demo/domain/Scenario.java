package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 시나리오 저장 테이블.
 * metrics는 JSON 컬럼으로 통째 저장 (A 방식 - 프론트 metrics 구조 변경에 유연).
 */
@Entity
@Table(name = "scenario")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String nickname;

    /** 점수 스냅샷 전체 (프론트 metrics 객체 그대로) */
    @Column(name = "metrics_json", columnDefinition = "TEXT")
    private String metricsJson;

    /** 소유자 — 추후 확장용 (A 방식: 지금은 null) */
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void rename(String newName) {
        if (newName != null && !newName.isBlank()) this.name = newName.trim();
    }

    public void replaceMetrics(String newMetricsJson) {
        this.metricsJson = newMetricsJson;
    }
}
