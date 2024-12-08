package com.gbceventbooking.ApprovalService.Repository;

import com.gbceventbooking.ApprovalService.Model.Approval;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApprovalRepository extends MongoRepository<Approval, String> {
}
