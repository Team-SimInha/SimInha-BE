package com.example.demo.dto;

import com.example.demo.domain.PresetPlacement;
import lombok.*;

public class PresetPlacementDto {

    @Getter @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String presetId;
        private String itemType;     // 프론트가 type으로 매칭
        private Double lng;
        private Double lat;
        private Integer qty;
        private String buildingId;
        private String locationName;
        private Integer installedYear;
        private String dataQuality;

        public static Response from(PresetPlacement p) {
            return Response.builder()
                    .id(p.getId())
                    .presetId(p.getPresetId())
                    .itemType(p.getItemType())
                    .lng(p.getLng())
                    .lat(p.getLat())
                    .qty(p.getQty())
                    .buildingId(p.getBuildingId())
                    .locationName(p.getLocationName())
                    .installedYear(p.getInstalledYear())
                    .dataQuality(p.getDataQuality())
                    .build();
        }
    }

    @Getter @AllArgsConstructor @Builder
    public static class BaselineResponse {
        private Double carbonReductionBaseline;
        private Double energyEffectBaseline;
        private Integer presetCount;
    }
}
