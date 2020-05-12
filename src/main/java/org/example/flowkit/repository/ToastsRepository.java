package org.example.flowkit.repository;

import org.example.flowkit.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToastsRepository extends CrudRepository<Toasts, Long> {

    @Query(value = "select a from Toasts a where a.notify = ?1 and a.notified=false")
    List<Toasts> getNotificationByAssociate(Associates b);

    @Query(value = "select a from Toasts a where a.activityInstance = ?1 and a.notify=?2 and a.notified=false")
    Toasts getNotificationByActivityInstanceAndNotifier(ActivityInstance b, Associates c);

    @Query(value = "select a from Toasts a where a.activityInstance = ?1 and a.notified=false")
    List<Toasts> getNotificationByActivityInstance(ActivityInstance b);


}
