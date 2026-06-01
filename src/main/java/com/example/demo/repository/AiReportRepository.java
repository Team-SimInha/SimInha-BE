package com.example.demo.repository;

import com.example.demo.domain.AiReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AiReportRepository extends JpaRepository<AiReport, Long> {
    Optional<AiReport> findByScenarioId(Long scenarioId);
}
