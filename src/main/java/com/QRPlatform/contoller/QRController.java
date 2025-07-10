package com.QRPlatform.contoller;

import com.QRPlatform.dto.CreateQrRequest;
import com.QRPlatform.dto.UpdateQRRequest;
import com.QRPlatform.model.DynamicQR;
import com.QRPlatform.repository.DynamicQRRepository;
import com.QRPlatform.security.JwtUtil;
import com.QRPlatform.service.QRService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class QRController {

    @Autowired
    private DynamicQRRepository qrRepository;
    
    @Autowired
    private QRService qrService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createQR(@RequestBody CreateQrRequest payload, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtUtil.extractUsername(token);

        String qrName = payload.getQrName();
        String destinationUrl = payload.getDestinationUrl();

        if (qrName == null || destinationUrl == null) {
            return ResponseEntity.badRequest().body("qrName and destinationUrl are required.");
        }

        // Save QR
        DynamicQR qr = new DynamicQR(userEmail, qrName, destinationUrl);

        // Optional: Save design data into the entity (if applicable)
        if (payload.getDesignData() != null) {
            CreateQrRequest.DesignData reqDesign = payload.getDesignData();

            DynamicQR.DesignData qrDesign = new DynamicQR.DesignData();
            qrDesign.setLogo(reqDesign.getLogo());
            qrDesign.setFrameStyle(reqDesign.getFrameStyle());
            qrDesign.setDotsStyle(reqDesign.getDotsStyle());
            qrDesign.setBackgroundColor(reqDesign.getBackgroundColor());
            qrDesign.setDotsColor(reqDesign.getDotsColor());
            qrDesign.setMarkerBorderStyle(reqDesign.getMarkerBorderStyle());
            qrDesign.setMarkerCenterStyle(reqDesign.getMarkerCenterStyle());

            qr.setDesignData(qrDesign);
        }

        qrRepository.save(qr);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "QR created successfully");
        response.put("qrId", qr.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyQRCodes(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtUtil.extractUsername(token);
        List<DynamicQR> qrs = qrRepository.findByUserId(userEmail);

        // ➕ Add total and average calculation
        int totalScans = qrs.stream().mapToInt(DynamicQR::getScanCount).sum();
        int avgScans = qrs.size() > 0 ? totalScans / qrs.size() : 0;

        Map<String, Object> response = new HashMap<>();
        response.put("qrs", qrs); // actual list
        response.put("totalScans", totalScans);
        response.put("averageScans", avgScans);
        System.out.println(totalScans);
        System.out.println(avgScans);
//        return ResponseEntity.ok(qrRepository.findByUserId(userEmail));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> redirectToDestination(@PathVariable("id") String id) {
        Optional<DynamicQR> qrOptional = qrRepository.findById(id);

        if (qrOptional.isPresent()) {
            DynamicQR qr = qrOptional.get();
            qr.setScanCount(qr.getScanCount() + 1);
            qrRepository.save(qr); //  save the DynamicQR object

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", qr.getDestinationUrl())
                    .build(); // redirect
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("QR code not found");
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<DynamicQR> updateQR(@PathVariable("id") String id, @RequestBody UpdateQRRequest req) {
        return ResponseEntity.ok(qrService.updateQR(id, req.getDestinationUrl()));
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteQR(@PathVariable("id") String id) {
        qrService.deleteQR(id);
        return ResponseEntity.ok("QR Deleted Successfully");
    }

}