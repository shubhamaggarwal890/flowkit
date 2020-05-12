package org.example.flowkit.controller;

import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Toasts;
import org.example.flowkit.service.AssociateService;
import org.example.flowkit.service.ToastsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ToastsController {
    private ToastsService toastService;
    private AssociateService associateService;

    @Autowired
    public void setToastService(ToastsService toastService) {
        this.toastService = toastService;
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping("/get_notifications")
    public ResponseEntity<List<Toasts>> getNotifications(@RequestBody Associates associates) {
        Associates notify = associateService.getAssociateById(associates.getId());
        if (notify == null) {
            System.out.println("Error: [getNotifications] [NotificationController] couldn't find notifier " +
                    "for the associate provided");
            return ResponseEntity.notFound().build();
        }

        List<Toasts> toasts = toastService.getNotificationForAssociate(notify);
        if (toasts == null || toasts.isEmpty()) {
            return ResponseEntity.ok().body(null);
        }
        List<Toasts> responseToasts = new ArrayList<>();
        for (Toasts toast : toasts) {
            toast.setNotify(null);
            toast.setActivityInstance(null);
            responseToasts.add(toast);
        }
        return ResponseEntity.ok().body(responseToasts);
    }

    @PostMapping("/dismiss_notification")
    public Boolean getNotifications(@RequestBody Toasts toasts) {
        Toasts toasts1 = toastService.dismissNotification(toasts.getId());
        return toasts1 != null;
    }
}
