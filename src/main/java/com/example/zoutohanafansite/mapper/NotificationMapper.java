package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.notification.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Select("""
            SELECT COUNT(*)
            FROM notifications
            WHERE user_id = #{userId}        -- 取得したいユーザーのIDを指定
              AND seen = false           -- 未読状態
              AND deleted = false;       -- 削除されていない
            """)
    int selectSeenNum(long userId);

    @Insert("""
        INSERT INTO notifications
        (admin_id, user_id, review_id, title, content)
        VALUES 
            (#{adminId}, #{userId}, #{reviewId}, #{title}, #{content})
    """)
    void insertNotification(Notification notification);

    @Select("""
        SELECT * FROM notifications
        WHERE user_id = #{userId}
            AND deleted = false
        ORDER BY created_at DESC;
    """)
    List<Notification> selectNotificationByUserid(long userId);

    @Select("""
        SELECT * FROM notifications
        WHERE id = #{id}
            AND deleted = false
    """)
    Notification selectNotificationById(long id);

    @Update("""
        UPDATE notifications
        SET 
            seen = true,
            updated_at = NOW()
        WHERE 
            id = #{id}
    """)
    void updateNotificationSeen(long id);
}
