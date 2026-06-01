package com.example.demo.service;

import com.example.demo.dto.EquipmentDto.Response;
import com.example.demo.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Transactional(readOnly = true)
    public List<Response> findAll() {
        return equipmentRepository.findAllByOrderByGroupNameAscIdAsc().stream()
                .map(Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Response findById(Long id) {
        return equipmentRepository.findById(id)
                .map(Response::from)
                .orElseThrow(() -> new NotFoundException("EQUIPMENT_NOT_FOUND", "설비를 찾을 수 없습니다."));
    }
}
