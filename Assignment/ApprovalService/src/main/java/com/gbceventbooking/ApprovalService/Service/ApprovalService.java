package com.gbceventbooking.ApprovalService.Service;

import com.gbceventbooking.ApprovalService.Model.Approval;
import com.gbceventbooking.ApprovalService.Repository.ApprovalRepository;
import com.gbceventbooking.ApprovalService.DTO.EventDetails;
import com.gbceventbooking.ApprovalService.DTO.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalService.class);

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${event.service.url}")
    private String eventServiceUrl;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }

    public Optional<Approval> getApprovalById(String id) {
        return approvalRepository.findById(id);
    }

    public Approval createApproval(Approval approval) {
        // Call EventService to fetch event details
        EventDetails eventDetails;
        try {
            eventDetails = getEventDetails(approval.getEventId());
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Event not found for ID: {}", approval.getEventId());
            throw new RuntimeException("Event not found for ID: " + approval.getEventId(), e);
        }

        // Call UserService to check approver details
        UserDetails userDetails;
        try {
            userDetails = getUserDetails(approval.getApproverId());
            if (userDetails == null) {
                logger.error("User response is null for ID: {}", approval.getApproverId());
                throw new RuntimeException("User not found for ID: " + approval.getApproverId());
            }
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("User not found for ID: {}", approval.getApproverId());
            throw new RuntimeException("User not found for ID: " + approval.getApproverId(), e);
        }

        // Additional logic to verify user role
        if (!"STAFF".equals(userDetails.getRole())) {
            logger.error("User with ID {} does not have sufficient privileges to approve events", approval.getApproverId());
            throw new RuntimeException("User does not have sufficient privileges to approve events");
        }

        return approvalRepository.save(approval);
    }

    public Approval updateApproval(String id, Approval approvalDetails) {
        return approvalRepository.findById(id).map(approval -> {
            approval.setStatus(approvalDetails.getStatus());
            approval.setComments(approvalDetails.getComments());
            return approvalRepository.save(approval);
        }).orElseThrow(() -> new RuntimeException("Approval not found"));
    }

    public void deleteApproval(String id) {
        approvalRepository.deleteById(id);
    }

    private EventDetails getEventDetails(String eventId) {
        String url = eventServiceUrl + "/" + eventId;
        try {
            EventDetails eventDetails = restTemplate.getForObject(url, EventDetails.class);
            if (eventDetails == null) {
                throw new RuntimeException("Event not found with ID: " + eventId);
            }
            return eventDetails;
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Event not found with ID: " + eventId, e);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching event details for ID: " + eventId, e);
        }
    }

    private UserDetails getUserDetails(String userId) {
        String url = userServiceUrl + "/" + userId;
        try {
            UserDetails userDetails = restTemplate.getForObject(url, UserDetails.class);
            if (userDetails == null) {
                throw new RuntimeException("User not found with ID: " + userId);
            }
            return userDetails;
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User not found with ID: " + userId, e);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching user details for ID: " + userId, e);
        }
    }
}
