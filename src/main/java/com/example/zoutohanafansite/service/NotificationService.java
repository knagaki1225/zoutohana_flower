package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.admin.notification.NotificationSend;
import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.form.AdminNotificationSendForm;
import com.example.zoutohanafansite.entity.nominatedreview.NominatedReview;
import com.example.zoutohanafansite.entity.notification.Notification;
import com.example.zoutohanafansite.entity.notification.NotificationList;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.entity.review.Review;
import com.example.zoutohanafansite.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ReviewService reviewService;
    private final ProjectService projectService;
    private final UserService userService;
    private final NominatedReviewService nominatedReviewService;

    public NotificationService(NotificationRepository notificationRepository, ReviewService reviewService, ProjectService projectService, UserService userService, NominatedReviewService nominatedReviewService) {
        this.notificationRepository = notificationRepository;
        this.reviewService = reviewService;
        this.projectService = projectService;
        this.userService = userService;
        this.nominatedReviewService = nominatedReviewService;
    }

    /**
     * 未読のメール件数取得
     *
     * @param id userId
     * @return int
     */
    public int getSeenNum(long id){
        return notificationRepository.selectSeenNum(id);
    }

    public void insertNotification(Notification notification){
        notificationRepository.insertNotification(notification);
    }

    public void insertNotificationByForm(AdminNotificationSendForm form, long reviewId, long adminId){
        Notification notification = new Notification();
        Review review = reviewService.getReviewById(reviewId);
        Project project = projectService.getProjectById(review.getProjectId());
        User user = userService.getUserById(review.getUserId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月d日");

        notification.setAdminId(adminId);
        notification.setReviewId(reviewId);
        notification.setUserId(review.getUserId());
        notification.setTitle(form.getTitle());

        String content = form.getContent();
        content = content.replaceAll("\\{project-name}", project.getName());
        content = content.replaceAll("\\{project-submissionStartAt}", project.getSubmissionStartAt().format(formatter));
        content = content.replaceAll("\\{project-submissionEndAt}", project.getSubmissionEndAt().format(formatter));
        content = content.replaceAll("\\{project-votingStartAt}", project.getVotingStartAt().format(formatter));
        content = content.replaceAll("\\{project-votingEndAt}", project.getVotingEndAt().format(formatter));
        content = content.replaceAll("\\{user-nickname}", user.getNickname());
        content = content.replaceAll("\\{review-title}", review.getReviewTitle());
        content = content.replaceAll("\\{book-title}", review.getBookTitle());

        notification.setContent(content);

        insertNotification(notification);
    }

    public List<Notification> selectNotificationByUserid(long userId){
        return notificationRepository.selectNotificationByUserid(userId);
    }

    public List<NotificationList> getNotificationListByUserId(long userId){
        List<Notification> notifications = selectNotificationByUserid(userId);
        List<NotificationList> notificationLists = new ArrayList<>();
        for(Notification notification : notifications){
            notificationLists.add(new NotificationList(notification));
        }
        return notificationLists;
    }

    public Notification selectNotificationById(long id){
        return notificationRepository.selectNotificationById(id);
    }

    public void updateNotificationSeen(long id){
        notificationRepository.updateNotificationSeen(id);
    }

    public void insertBulkNotification(NotificationSend notificationSend, long adminId){
        Project project = projectService.getProjectByUrlKey(notificationSend.getUrlKey());
        if(notificationSend.getRecipient() == 1){
            List<Review> reviews = reviewService.selectReviewsByUrlKey(notificationSend.getUrlKey());
            for(Review review : reviews){
                AdminNotificationSendForm form = new AdminNotificationSendForm();
                form.setTitle(notificationSend.getTitle());
                form.setContent(notificationSend.getMessage());

                insertNotificationByForm(form, review.getId(), adminId);
            }
        }else{
             List<NominatedReview> reviews = nominatedReviewService.getByProjectId(project.getId());
             for(NominatedReview nominatedReview : reviews){
                 AdminNotificationSendForm form = new AdminNotificationSendForm();
                 form.setTitle(notificationSend.getTitle());
                 form.setContent(notificationSend.getMessage());
                 insertNotificationByForm(form, nominatedReview.getId(), adminId);
             }
        }
    }

}
