package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 설비 마스터 테이블.
 * 프론트의 items.js와 1:1 대응 (item_id가 프론트의 문자열 ID).
 */
@Entity
@Table(name = "equipment", uniqueConstraints = @UniqueConstraint(columnNames = "item_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false, unique = true)
    private String itemId;          // 예: 'solar_self'

    @Column(nullable = false)
    private String label;            // 예: '자가소비 태양광'

    @Column(nullable = false)
    private Long cost;               // 설치 단위당 비용(원)

    @Column(name = "energy_kwh", nullable = false)
    private Double energyKwh;        // 단위당 연간 생산/절감 전력량(kWh)

    @Column(nullable = false)
    private Double coeff;            // 단위당 탄소 절감 계수

    @Column(nullable = false)
    private String unit;             // 'kW', '개', 'RT', '동', '기', '㎡', '그루'

    @Column(name = "group_name")
    private String groupName;        // '에너지 생산', '에너지 절감', '친환경 인프라', '자연 기반'

    private String icon;             // 이모지 (선택)

    @Column(columnDefinition = "TEXT")
    private String description;      // 설명문
}
