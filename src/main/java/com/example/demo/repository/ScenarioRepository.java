package com.example.demo.repository;

import com.example.demo.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findAllByOrderByUpdatedAtDesc();
    List<Scenario> findByNicknameOrderByUpdatedAtDesc(String nickname);
}
