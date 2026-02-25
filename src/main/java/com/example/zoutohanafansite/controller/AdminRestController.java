package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.admin.notification.NotificationSend;
import com.example.zoutohanafansite.entity.admin.notification.NotificationTemplateContent;
import com.example.zoutohanafansite.entity.admin.notification.NotificationTemplateList;
import com.example.zoutohanafansite.entity.form.AdminProjectCreateForm;
import com.example.zoutohanafansite.entity.notificationtemplate.NotificationTemplate;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.security.CustomAdminUserDetails;
import com.example.zoutohanafansite.service.NotificationService;
import com.example.zoutohanafansite.service.NotificationTemplateService;
import com.example.zoutohanafansite.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final NotificationTemplateService notificationTemplateService;
    private final NotificationService notificationService;
    private final ProjectService projectService;

    public AdminRestController(NotificationTemplateService notificationTemplateService, NotificationService notificationService, ProjectService projectService) {
        this.notificationTemplateService = notificationTemplateService;
        this.notificationService = notificationService;
        this.projectService = projectService;
    }

    @GetMapping("/notification/template/list")
    public ResponseEntity<List<NotificationTemplateList>> getNotificationTemplateList(){
        List<NotificationTemplate> notificationTemplates = notificationTemplateService.selectAllNotificationTemplate();
        List<NotificationTemplateList> notificationTemplateLists = new ArrayList<>();
        for(NotificationTemplate notificationTemplate : notificationTemplates){
            NotificationTemplateList notificationTemplateList = new NotificationTemplateList();
            notificationTemplateList.setId(notificationTemplate.getId());
            notificationTemplateList.setTitle(notificationTemplate.getTitle());
            notificationTemplateLists.add(notificationTemplateList);
        }
        return ResponseEntity.ok(notificationTemplateLists);
    }

    @GetMapping("/notification/template/{id}")
    public ResponseEntity<NotificationTemplateContent>  getNotificationTemplate(@PathVariable long id){
        NotificationTemplate notificationTemplate = notificationTemplateService.selectNotificationTemplateById(id);
        NotificationTemplateContent notificationTemplateContent = new NotificationTemplateContent();
        notificationTemplateContent.setId(id);
        notificationTemplateContent.setTitle(notificationTemplate.getTitle());
        notificationTemplateContent.setContent(notificationTemplate.getContent());
        return ResponseEntity.ok(notificationTemplateContent);
    }

    @PostMapping("/notification/send")
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationSend notificationSend, @AuthenticationPrincipal CustomAdminUserDetails user){
        notificationService.insertBulkNotification(notificationSend, user.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/project/new")
    public ResponseEntity<Void> createProject(@ModelAttribute AdminProjectCreateForm adminProjectCreateForm){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/project/status/next/{urlKey}")
    public ResponseEntity<Void> nextStatusProject(@PathVariable String urlKey){
        projectService.nextStatus(urlKey);
        return ResponseEntity.ok().build();
    }
}
