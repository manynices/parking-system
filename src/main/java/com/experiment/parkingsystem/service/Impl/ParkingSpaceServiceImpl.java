package com.experiment.parkingsystem.service.Impl;
import com.experiment.parkingsystem.dto.*;
import com.experiment.parkingsystem.entity.ParkingSpace;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.ParkingSpaceMapper;
import com.experiment.parkingsystem.service.ParkingSpaceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
    private final ParkingSpaceMapper parkingSpaceMapper;
    public ParkingSpaceServiceImpl(ParkingSpaceMapper parkingSpaceMapper) {
        this.parkingSpaceMapper = parkingSpaceMapper;
    }
    @Override
    public ParkingAreaStatusResponse getParkingAreaStatus(String parkingArea, String status) {
        List<ParkingSpace> spaces = parkingSpaceMapper.findByCriteria(parkingArea, status);
        List<ParkingSpaceResponse> spaceDtoList = spaces.stream().map(ParkingSpaceResponse::fromEntity).collect(Collectors.toList());
        long occupiedCount = parkingSpaceMapper.countByAreaAndStatus(parkingArea, "占用");
        long freeCount = parkingSpaceMapper.countByAreaAndStatus(parkingArea, "空闲");
        ParkingAreaStatusResponse response = new ParkingAreaStatusResponse();
        response.setParkingArea(parkingArea);
        response.setTotal(occupiedCount + freeCount);
        response.setOccupied(occupiedCount);
        response.setFree(freeCount);
        response.setSpaceList(spaceDtoList);
        return response;
    }
    @Override
    public ParkingSpaceUpdateResponse updateParkingSpaceStatus(String spaceNo, ParkingSpaceStatusUpdateRequest request) {
        ParkingSpace existingSpace = parkingSpaceMapper.findBySpaceNo(spaceNo);
        if (existingSpace == null) {
            throw new ResourceNotFoundException("找不到编号为 " + spaceNo + " 的车位");
        }
        if ("占用".equals(request.getStatus()) && !StringUtils.hasText(request.getVehicleId())) {
            throw new IllegalArgumentException("当车位状态更新为'占用'时，必须提供车辆ID (vehicleId)");
        }
        ParkingSpace spaceToUpdate = new ParkingSpace();
        spaceToUpdate.setSpaceNo(spaceNo);
        spaceToUpdate.setStatus(request.getStatus());
        if ("空闲".equals(request.getStatus())) {
            spaceToUpdate.setVehicleId(null);
        } else {
            spaceToUpdate.setVehicleId(request.getVehicleId());
        }
        parkingSpaceMapper.update(spaceToUpdate);
        return ParkingSpaceUpdateResponse.fromEntity(parkingSpaceMapper.findBySpaceNo(spaceNo));
    }
}