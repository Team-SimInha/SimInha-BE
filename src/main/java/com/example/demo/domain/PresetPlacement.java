package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 기존 설치 설비 (사용자 조작 불가, 베이스라인 계산용).
 * 프론트의 preinstalled.js와 1:1 대응.
 */
@Entity
@Table(name = "preset_placement",
       uniqueConstraints = @UniqueConstraint(columnNames = "preset_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PresetPlacement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preset_id", nullable = false, unique = true)
    private String presetId;         // 예: 'pre_solar_60th'

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;        // FK → equipment.id

    @Column(name = "item_type", nullable = false)
    private String itemType;         // 프론트가 쓰는 type 문자열 (예: 'solar_self')

    @Column(nullable = false)
    private Double lng;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "building_id")
    private String buildingId;       // 예: 'bldg_60th'

    @Column(name = "location_name")
    private String locationName;     // 예: '60주년기념관 옥상'

    @Column(name = "installed_year")
    private Integer installedYear;

    @Column(name = "data_quality")
    private String dataQuality;      // 'estimated' 등
}
