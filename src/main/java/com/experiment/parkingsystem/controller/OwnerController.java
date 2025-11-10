package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.OwnerCreateRequest;
import com.experiment.parkingsystem.dto.OwnerResponse;
import com.experiment.parkingsystem.dto.OwnerUpdateRequest;
import com.experiment.parkingsystem.service.OwnerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OwnerResponse>> createOwner(@RequestBody OwnerCreateRequest request) {
        OwnerResponse createdOwner = ownerService.createOwner(request);
        return new ResponseEntity<>(ApiResponse.success(createdOwner), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<OwnerResponse>>> getOwners(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name) {
        // Spring Pageable 页码从 0 开始
        PaginatedResponse<OwnerResponse> owners = ownerService.getOwners(page, size, name);
        return ResponseEntity.ok(ApiResponse.success(owners));
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<ApiResponse<OwnerResponse>> getOwnerById(@PathVariable String ownerId) {
        OwnerResponse owner = ownerService.getOwnerById(ownerId);
        return ResponseEntity.ok(ApiResponse.success(owner));
    }

    @PutMapping("/{ownerId}")
    public ResponseEntity<ApiResponse<OwnerResponse>> updateOwner(
            @PathVariable String ownerId,
            @RequestBody OwnerUpdateRequest request) {
        OwnerResponse updatedOwner = ownerService.updateOwner(ownerId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedOwner));
    }

    @DeleteMapping("/{ownerId}")
    public ResponseEntity<ApiResponse<Void>> deleteOwner(@PathVariable String ownerId) {
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }
}
