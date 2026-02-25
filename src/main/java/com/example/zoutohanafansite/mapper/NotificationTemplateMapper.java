package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.form.AdminNotificationTemplateForm;
import com.example.zoutohanafansite.entity.notificationtemplate.NotificationTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationTemplateMapper {
    @Insert("""
        INSERT INTO notification_templates
        (name, title, content)
        VALUES 
            (#{name}, #{title}, #{content})
    """)
    void insertNotificationTemplate(NotificationTemplate notificationTemplate);

    @Select("""
        SELECT * FROM notification_templates WHERE deleted = false;
    """)
    List<NotificationTemplate> selectAllNotificationTemplate();

    @Select("""
        SELECT * FROM notification_templates
            WHERE deleted = dalse
                OR name LIKE CONCAT('%', #{keyword}, '%')
                OR title LIKE CONCAT('%', #{keyword}, '%')
                OR content LIKE CONCAT('%', #{keyword}, '%')
    """)
    List<NotificationTemplate> selectNotificationTemplateByKeyword(String keyword);

    @Select("""
        SELECT * FROM notification_templates WHERE id = #{id}
    """)
    NotificationTemplate selectNotificationTemplateById(long id);

    @Update("""
        UPDATE notification_templates
        SET 
            name = #{form.templateName},
            title = #{form.title},
            content = #{form.content},
            updated_at = NOW()
        WHERE 
            id = #{id}
    """)
    void update(@Param("form") AdminNotificationTemplateForm  adminNotificationTemplateForm,@Param("id") long id);

    @Update("""
        UPDATE notification_templates
        SET 
            deleted = true,
            updated_at = NOW()
        WHERE 
            id = #{id}
    """)
    void deleteNotificationTemplate(long id);

}
