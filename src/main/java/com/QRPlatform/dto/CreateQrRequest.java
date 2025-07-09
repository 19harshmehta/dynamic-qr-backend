package com.QRPlatform.dto;

import lombok.Data;

@Data
public class CreateQrRequest {
    private String qrName;
    
    private String destinationUrl;
    private DesignData designData;

    @Data
    public static class DesignData {
        private String logo;
        private String frameStyle;
        private String dotsStyle;
        private String backgroundColor;
        private String dotsColor;
        private String markerBorderStyle;
        private String markerCenterStyle;
    }
}