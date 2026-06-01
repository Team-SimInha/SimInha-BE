package com.example.demo.repository;

import com.example.demo.domain.LeaderboardEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    List<LeaderboardEntry> findAllByOrderByTotalSavingDescCreatedAtDesc(Pageable pageable);
    List<LeaderboardEntry> findAllByOrderByEfficiencyScoreDescCreatedAtDesc(Pageable pageable);
    List<LeaderboardEntry> findAllByOrderByEnergyKwhDescCreatedAtDesc(Pageable pageable);
}
