package com.example.demo.service;

import com.example.demo.domain.Equipment;
import com.example.demo.domain.PresetPlacement;
import com.example.demo.dto.PresetPlacementDto.BaselineResponse;
import com.example.demo.dto.PresetPlacementDto.Response;
import com.example.demo.repository.EquipmentRepository;
import com.example.demo.repository.PresetPlacementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PresetPlacementService {

    private final PresetPlacementRepository presetRepository;
    private final EquipmentRepository equipmentRepository;

    @Transactional(readOnly = true)
    public List<Response> findAll() {
        return presetRepository.findAllByOrderByIdAsc().stream()
                .map(Response::from)
                .toList();
    }

    /**
     * 베이스라인 집계 (기존 설비의 탄소·에너지 절감량 총합).
     * 보정률은 미적용 (preset은 고정값).
     */
    @Transactional(readOnly = true)
    public BaselineResponse calculateBaseline() {
        Map<Long, Equipment> equipmentCache = new HashMap<>();
        equipmentRepository.findAll().forEach(e -> equipmentCache.put(e.getId(), e));

        double carbon = 0.0;
        double energy = 0.0;
        List<PresetPlacement> all = presetRepository.findAll();
        for (PresetPlacement p : all) {
            Equipment eq = equipmentCache.get(p.getEquipmentId());
            if (eq == null) continue;
            int qty = p.getQty() != null ? p.getQty() : 0;
            carbon += eq.getCoeff() * qty;
            energy += eq.getEnergyKwh() * qty;
        }
        return BaselineResponse.builder()
                .carbonReductionBaseline(carbon)
                .energyEffectBaseline(energy)
                .presetCount(all.size())
                .build();
    }
}
