package com.QRPlatform.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.QRPlatform.model.DynamicQR;

public interface DynamicQRRepository extends MongoRepository<DynamicQR, String> {
    List<DynamicQR> findByUserId(String userId);
}
