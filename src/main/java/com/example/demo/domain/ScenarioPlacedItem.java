package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 시나리오 내 배치된 신규 아이템.
 * 프론트 storage.js의 stripItem 출력 구조와 일치.
 */
@Entity
@Table(name = "scenario_placed_item",
       indexes = {
         @Index(name = "idx_placed_scenario", columnList = "scenario_id"),
         @Index(name = "idx_placed_coords", columnList = "lng, lat")
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScenarioPlacedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_id", nullable = false)
    private Long scenarioId;

    /** 프론트가 부여한 클라이언트측 id (재배치 시 유지) */
    @Column(name = "client_id")
    private String clientId;

    /** 프론트의 item type (예: 'solar_self') */
    @Column(name = "item_type", nullable = false)
    private String itemType;

    @Column(nullable = false)
    private Double lng;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Integer qty;

    /** 위치 보정이 반영된 계수 (프론트의 effectiveCoeff) */
    @Column(name = "effective_coeff")
    private Double effectiveCoeff;

    @Column(name = "location_name")
    private String locationName;

    // ── 구역 정보 (zones.js 메타) ──
    @Column(name = "zone_id")
    private String zoneId;

    @Column(name = "zone_type")
    private String zoneType;

    @Column(name = "zone_name")
    private String zoneName;

    @Column(name = "zone_year")
    private Integer zoneYear;

    @Column(name = "zone_floors")
    private Integer zoneFloors;

    @Column(name = "zone_note", columnDefinition = "TEXT")
    private String zoneNote;

    @Column(name = "zone_reason", columnDefinition = "TEXT")
    private String zoneReason;
}
