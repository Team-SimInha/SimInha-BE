package com.example.demo.repository;

import com.example.demo.domain.ScenarioPlacedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ScenarioPlacedItemRepository extends JpaRepository<ScenarioPlacedItem, Long> {
    List<ScenarioPlacedItem> findByScenarioId(Long scenarioId);

    @Transactional
    void deleteByScenarioId(Long scenarioId);
}
