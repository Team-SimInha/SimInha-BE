package com.example.demo.repository;

import com.example.demo.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByItemId(String itemId);
    boolean existsByItemId(String itemId);
    List<Equipment> findAllByOrderByGroupNameAscIdAsc();
}
