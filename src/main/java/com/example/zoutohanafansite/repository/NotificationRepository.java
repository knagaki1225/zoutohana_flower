package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.notification.Notification;
import com.example.zoutohanafansite.mapper.NotificationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepository {
    private final NotificationMapper notificationMapper;

    public NotificationRepository(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    public int selectSeenNum(long userId){
        return notificationMapper.selectSeenNum(userId);
    }

    public void insertNotification(Notification notification){
        notificationMapper.insertNotification(notification);
    }

    public List<Notification> selectNotificationByUserid(long userId){
        return notificationMapper.selectNotificationByUserid(userId);
    }

    public Notification selectNotificationById(long id){
        return notificationMapper.selectNotificationById(id);
    }

    public void updateNotificationSeen(long id){
        notificationMapper.updateNotificationSeen(id);
    }

}
