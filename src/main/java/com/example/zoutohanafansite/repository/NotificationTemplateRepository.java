package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.form.AdminNotificationTemplateForm;
import com.example.zoutohanafansite.entity.notificationtemplate.NotificationTemplate;
import com.example.zoutohanafansite.mapper.NotificationTemplateMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationTemplateRepository {
    private final NotificationTemplateMapper notificationTemplateMapper;

    public NotificationTemplateRepository(NotificationTemplateMapper notificationTemplateMapper) {
        this.notificationTemplateMapper = notificationTemplateMapper;
    }

    public void insertNotificationTemplate(NotificationTemplate notificationTemplate){
        notificationTemplateMapper.insertNotificationTemplate(notificationTemplate);
    }

    public List<NotificationTemplate> selectAllNotificationTemplate(){
        return notificationTemplateMapper.selectAllNotificationTemplate();
    }

    public List<NotificationTemplate> selectNotificationTemplateByKeyword(String keyword){
        return notificationTemplateMapper.selectNotificationTemplateByKeyword(keyword);
    }

    public NotificationTemplate selectNotificationTemplateById(long id){
        return notificationTemplateMapper.selectNotificationTemplateById(id);
    }

    public void update(AdminNotificationTemplateForm adminNotificationTemplateForm, long id){
        notificationTemplateMapper.update(adminNotificationTemplateForm,id);
    }

    public void deleteNotificationTemplate(long id){
        notificationTemplateMapper.deleteNotificationTemplate(id);
    }
}
