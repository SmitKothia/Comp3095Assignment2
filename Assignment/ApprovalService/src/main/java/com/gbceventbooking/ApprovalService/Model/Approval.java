package com.gbceventbooking.ApprovalService.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Document(collection = "approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {
    @Id
    private String id;
    private String bookingId;
    private String userId;
    private String status;
    private String comments;
    private String eventId;  // Add this property
    private String approverId; 


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }
}
