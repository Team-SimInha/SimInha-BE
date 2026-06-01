package com.example.demo.repository;

import com.example.demo.domain.PresetPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PresetPlacementRepository extends JpaRepository<PresetPlacement, Long> {
    boolean existsByPresetId(String presetId);
    List<PresetPlacement> findAllByOrderByIdAsc();
}
