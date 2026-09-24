package com.ptpmhdv.payment.controller;

import com.ptpmhdv.common.security.CurrentUser;
import com.ptpmhdv.payment.dto.NotificationResponse;
import com.ptpmhdv.payment.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> myNotifications() {
        return ResponseEntity.ok(notificationService.myNotifications(CurrentUser.get().userId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(CurrentUser.get().userId(), id);
        return ResponseEntity.noContent().build();
    }
}
