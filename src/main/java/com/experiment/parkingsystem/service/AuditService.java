package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.audit.*;

public interface AuditService {
    PaginatedResponse<AuditListItemResponse> listPendingAudits(int page, int size, String type);

    AuditReviewResponse approveAudit(String applicationIdStr, AuditReviewRequest request);

    AuditReviewResponse rejectAudit(String applicationIdStr, AuditReviewRequest request);
}