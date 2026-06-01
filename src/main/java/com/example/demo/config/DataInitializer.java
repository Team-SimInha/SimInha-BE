package com.example.demo.config;

import com.example.demo.domain.Equipment;
import com.example.demo.domain.PresetPlacement;
import com.example.demo.repository.EquipmentRepository;
import com.example.demo.repository.PresetPlacementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 애플리케이션 시작 시 설비 마스터(items.js) 및 기존 설비(preinstalled.js) 시드 데이터 주입.
 * 이미 데이터가 있으면 스킵 — 안전하게 반복 실행 가능.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EquipmentRepository equipmentRepository;
    private final PresetPlacementRepository presetRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, Long> idMap = seedEquipments();   // itemId → equipment.id
        seedPresetPlacements(idMap);
    }

    /** items.js의 ITEM_TYPES와 1:1 대응 */
    private Map<String, Long> seedEquipments() {
        Map<String, Long> idMap = new HashMap<>();
        List<Equipment> seeds = List.of(
            // ── 에너지 생산 ──
            eq("solar_self",   "자가소비 태양광",      1_500_000L,  1200.0, 460.0,  "kW",  "에너지 생산", "☀️",
               "옥상 설치, 자가소비 → Scope2 직접 감소"),
            eq("solar_bipv",   "BIPV (건물일체형)",    3_000_000L,   990.0, 380.0,  "kW",  "에너지 생산", "🏗️",
               "벽면·유리 통합, 효율 85%"),
            eq("solar_lease",  "부지대여 태양광",              0L,     0.0,   0.0,  "kW",  "에너지 생산", "⚠️",
               "감축 실적 대학 귀속 불가 (현재 인하대 방식)"),

            // ── 에너지 절감 ──
            eq("led",          "LED 조명 교체",            50_000L,    70.0,  30.0,  "개",  "에너지 절감", "💡",
               "40W→18W, 연 3,200시간 기준"),
            eq("geothermal",   "지열 히트펌프",         5_000_000L,  3300.0,1500.0,  "RT",  "에너지 절감", "🌡️",
               "냉난방 동시, 기존 보일러 대비 60% 절감"),
            eq("bems",         "BEMS (에너지관리)",   80_000_000L, 32000.0,15000.0, "동",   "에너지 절감", "📊",
               "건물 1동 기준 ~10-15% 에너지 절감"),

            // ── 친환경 인프라 ──
            eq("ev",           "EV 충전소",             7_000_000L,  2600.0,1200.0,  "기",  "친환경 인프라", "🔌",
               "7kW급, 태양광 연계 시 화석연료 대체"),
            eq("rainwater",    "빗물 저류 시스템",    12_000_000L,   110.0,  50.0,  "기",  "친환경 인프라", "💧",
               "수자원 재활용, 간접 절감"),

            // ── 자연 기반 ──
            eq("greenroof",    "그린루프 (옥상녹화)",    180_000L,    11.0,   5.0,  "㎡",  "자연 기반", "🌿",
               "단열효과 + 탄소흡수"),
            eq("tree",         "수목 식재",                300_000L,     0.0,  22.0,  "그루","자연 기반", "🌳",
               "국립산림과학원 흡수계수 기준")
        );

        for (Equipment seed : seeds) {
            Equipment saved = equipmentRepository.findByItemId(seed.getItemId())
                    .orElseGet(() -> equipmentRepository.save(seed));
            idMap.put(saved.getItemId(), saved.getId());
        }
        return idMap;
    }

    /** preinstalled.js의 PREINSTALLED_ITEMS와 1:1 대응 */
    private void seedPresetPlacements(Map<String, Long> equipmentIdMap) {
        List<PresetPlacement> seeds = List.of(
            preset("pre_solar_60th",     "solar_self", 126.65435, 37.45088,   8,
                   "bldg_60th",   "60주년기념관 옥상",      2023, equipmentIdMap),
            preset("pre_solar_hitech",   "solar_self", 126.65725, 37.45065,   8,
                   "bldg_hitech", "하이테크센터 옥상",      2022, equipmentIdMap),
            preset("pre_solar_5",        "solar_self", 126.65725, 37.44850,   4,
                   "bldg_5",      "5호관 옥상",            2024, equipmentIdMap),
            preset("pre_led_library",    "led",        126.65252, 37.44935,  80,
                   "bldg_lib",    "정석학술정보관",        2021, equipmentIdMap),
            preset("pre_ev_gate",        "ev",         126.65445, 37.44805,   4,
                   null,          "정문 주차 구역",        2023, equipmentIdMap),
            preset("pre_greenroof_60th", "greenroof",  126.65443, 37.45082, 180,
                   "bldg_60th",   "60주년기념관 옥상녹화", 2020, equipmentIdMap)
        );

        for (PresetPlacement seed : seeds) {
            if (!presetRepository.existsByPresetId(seed.getPresetId())) {
                presetRepository.save(seed);
            }
        }
    }

    // ── 빌더 헬퍼 ──

    private Equipment eq(String itemId, String label, long cost, double energyKwh, double coeff,
                          String unit, String groupName, String icon, String description) {
        return Equipment.builder()
                .itemId(itemId)
                .label(label)
                .cost(cost)
                .energyKwh(energyKwh)
                .coeff(coeff)
                .unit(unit)
                .groupName(groupName)
                .icon(icon)
                .description(description)
                .build();
    }

    private PresetPlacement preset(String presetId, String itemType, double lng, double lat, int qty,
                                    String buildingId, String locationName, int installedYear,
                                    Map<String, Long> equipmentIdMap) {
        Long equipmentId = equipmentIdMap.get(itemType);
        if (equipmentId == null) {
            throw new IllegalStateException("Preset 시드 실패 - 알 수 없는 itemType: " + itemType);
        }
        return PresetPlacement.builder()
                .presetId(presetId)
                .equipmentId(equipmentId)
                .itemType(itemType)
                .lng(lng)
                .lat(lat)
                .qty(qty)
                .buildingId(buildingId)
                .locationName(locationName)
                .installedYear(installedYear)
                .dataQuality("estimated")
                .build();
    }
}
