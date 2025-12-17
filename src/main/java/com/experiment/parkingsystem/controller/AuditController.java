package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.audit.*;
import com.experiment.parkingsystem.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audits")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    // 9.1 获取待审核列表
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<PaginatedResponse<AuditListItemResponse>>> listPending(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.success(
                auditService.listPendingAudits(page, size, type)
        ));
    }

    // 9.2 审核通过
    @PutMapping("/{applicationId}/approve")
    public ResponseEntity<ApiResponse<AuditReviewResponse>> approve(
            @PathVariable String applicationId,
            @RequestBody AuditReviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                auditService.approveAudit(applicationId, request)
        ));
    }

    // 9.3 审核拒绝
    @PutMapping("/{applicationId}/reject")
    public ResponseEntity<ApiResponse<AuditReviewResponse>> reject(
            @PathVariable String applicationId,
            @RequestBody AuditReviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                auditService.rejectAudit(applicationId, request)
        ));
    }
}