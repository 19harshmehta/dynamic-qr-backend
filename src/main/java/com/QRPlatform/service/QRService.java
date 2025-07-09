package com.QRPlatform.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QRPlatform.model.DynamicQR;
import com.QRPlatform.repository.DynamicQRRepository;

@Service
public class QRService {
	
    @Autowired
	DynamicQRRepository qrRepo;
	
	public DynamicQR updateQR(String id, String newUrl) {
	    Optional<DynamicQR> optionalQR = qrRepo.findById(id);
	    if (optionalQR.isEmpty()) {
	        throw new RuntimeException("QR Code not found");
	    }

	    DynamicQR qr = optionalQR.get();
	    qr.setDestinationUrl(newUrl);
	    return qrRepo.save(qr); // Save updated object
	}
	
	public void deleteQR(String id) {
	    DynamicQR qr = qrRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("QR not found with ID: " + id));
	    
	    qrRepo.delete(qr);
	}
}
