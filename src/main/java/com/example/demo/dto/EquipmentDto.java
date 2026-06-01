package com.example.demo.dto;

import com.example.demo.domain.Equipment;
import lombok.*;

public class EquipmentDto {

    @Getter @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String itemId;
        private String label;
        private Long cost;
        private Double energyKwh;
        private Double coeff;
        private String unit;
        private String groupName;
        private String icon;
        private String description;

        public static Response from(Equipment e) {
            return Response.builder()
                    .id(e.getId())
                    .itemId(e.getItemId())
                    .label(e.getLabel())
                    .cost(e.getCost())
                    .energyKwh(e.getEnergyKwh())
                    .coeff(e.getCoeff())
                    .unit(e.getUnit())
                    .groupName(e.getGroupName())
                    .icon(e.getIcon())
                    .description(e.getDescription())
                    .build();
        }
    }
}
