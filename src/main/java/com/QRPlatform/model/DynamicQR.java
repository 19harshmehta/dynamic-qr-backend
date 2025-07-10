package com.QRPlatform.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "dynamic_qrs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DynamicQR {

    
	@Id
    private String id;

    private String userId; 
    private String qrName;
    private String destinationUrl;
    private DesignData designData;
    private Date createdAt;
    private Integer scanCount = 0;
    private Date lastUpdated;
    public DynamicQR(String userId, String qrName, String destinationUrl) {
        this.userId = userId;
        this.qrName = qrName;
        this.destinationUrl = destinationUrl;
        this.createdAt = new Date();
        this.lastUpdated = new Date();
       
    }
    // Getters, setters, constructors
    
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
