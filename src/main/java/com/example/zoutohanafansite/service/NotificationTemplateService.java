package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.form.AdminNotificationTemplateForm;
import com.example.zoutohanafansite.entity.notificationtemplate.NotificationTemplate;
import com.example.zoutohanafansite.repository.NotificationTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationTemplateService {
    private final NotificationTemplateRepository notificationTemplateRepository;

    public NotificationTemplateService(NotificationTemplateRepository notificationTemplateRepository) {
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    public void insertNotificationTemplate(NotificationTemplate  notificationTemplate){
        notificationTemplateRepository.insertNotificationTemplate(notificationTemplate);
    }

    public List<NotificationTemplate> selectAllNotificationTemplate(){
        return notificationTemplateRepository.selectAllNotificationTemplate();
    }

    public List<NotificationTemplate> selectNotificationTemplateByKeyword(String keyword){
        return notificationTemplateRepository.selectNotificationTemplateByKeyword(keyword);
    }

    public NotificationTemplate selectNotificationTemplateById(long id){
        return notificationTemplateRepository.selectNotificationTemplateById(id);
    }

    public void update(AdminNotificationTemplateForm adminNotificationTemplateForm, long id){
        notificationTemplateRepository.update(adminNotificationTemplateForm,id);
    }

    public void deleteNotificationTemplate(long id){
        notificationTemplateRepository.deleteNotificationTemplate(id);
    }
}
