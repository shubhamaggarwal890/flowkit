use workflow;
alter table workflow_instance drop column initiator_alert;
alter table activity_associates drop column associate_notification;
alter table activity_associates drop column initiator_notification;