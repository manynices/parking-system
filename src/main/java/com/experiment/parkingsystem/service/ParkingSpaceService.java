package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.dto.parkingspace.*;
import java.util.List;

public interface ParkingSpaceService {
    // 4.1 查询车位状态
    ParkingSpaceStatsResponse getParkingStats(String parkingArea, String status, String spaceNo, String spaceType);

    // 4.2 更新车位状态
    ParkingSpaceItemResponse updateSpaceStatus(String spaceNo, ParkingSpaceStatusUpdateRequest request);

    // 4.3 用户查询可用车位
    ParkingSpaceAvailableResponse getAvailableSpaces(String parkingArea, String spaceType);

    // 4.4 查询可购买的私人车位
    ParkingSpaceSalesResponse getPrivateSpacesForSale(String parkingArea);

    // 4.5 业主购买私人车位
    ParkingSpacePurchaseResponse purchaseSpace(ParkingSpacePurchaseRequest request);

    // 4.6 用户查询已购买的车位
    List<ParkingSpaceItemResponse> getMyPrivateSpaces();
}