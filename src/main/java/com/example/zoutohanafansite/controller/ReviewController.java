package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.form.IsbnForm;
import com.example.zoutohanafansite.entity.form.ReviewForm;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.entity.review.Review;
import com.example.zoutohanafansite.exception.AccessDeniedException;
import com.example.zoutohanafansite.security.CustomUserDetails;
import com.example.zoutohanafansite.service.ProjectService;
import com.example.zoutohanafansite.service.ReviewService;
import com.example.zoutohanafansite.util.AgeUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {
    private final ProjectService projectService;
    private final ReviewService reviewService;

    public ReviewController(ProjectService projectService, ReviewService reviewService) {
        this.projectService = projectService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String registerReview(Model model, @RequestParam long id, @AuthenticationPrincipal CustomUserDetails user) {
        Long draft = reviewService.getDraftId(id, user.getUserId());
        if(draft != null){
            return "redirect:/review/draft?reviewId=" + draft;
        }
        IsbnForm isbnForm = new IsbnForm();
        isbnForm.setProjectId(id);
        model.addAttribute("isbnForm", isbnForm);
        return "books/isbn-search";
    }

    @PostMapping
    public String registerIsbn(IsbnForm isbnForm, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("isbnForm", isbnForm);
        return "redirect:/review/write";
    }

    @GetMapping("/write")
    public String registerReview(IsbnForm isbnForm, Model model){
        ReviewForm  reviewForm = new ReviewForm();
        reviewForm.setProjectId(isbnForm.getProjectId());
        reviewForm.setBookTitle(isbnForm.getTitle());
        reviewForm.setAuthor(isbnForm.getAuthor());
        reviewForm.setPublisher(isbnForm.getPublisher());
        reviewForm.setIsbn(isbnForm.getIsbn());
        model.addAttribute("reviewForm", reviewForm);

        Project project = projectService.getProjectById(isbnForm.getProjectId());
        model.addAttribute("isEnableVisibleBookTitle", project.isEnableVisibleBookTitle());

        model.addAttribute("isDraft", false);

        return "books/review-form";
    }

    @PostMapping("/write")
    public String sendReview(ReviewForm reviewForm, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("reviewForm", reviewForm);
        return "redirect:/review/confirm";
    }

    @GetMapping("/confirm")
    public String confirmReview(Model model, ReviewForm reviewForm){
        model.addAttribute("reviewForm", reviewForm);
        model.addAttribute("draft", false);
        return "books/book-review";
    }

    @PostMapping("/rewrite")
    public String resendReview(ReviewForm reviewForm, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("reviewForm", reviewForm);
        return "redirect:/review/rewrite";
    }

    @GetMapping("/rewrite")
    public String reregisterReview(ReviewForm reviewForm, Model model){
        model.addAttribute("reviewForm", reviewForm);

        Project project = projectService.getProjectById(reviewForm.getProjectId());
        model.addAttribute("isEnableVisibleBookTitle", project.isEnableVisibleBookTitle());

        model.addAttribute("isDraft", false);

        return "books/review-form";
    }

    @PostMapping("/confirm")
    public String addReview(ReviewForm reviewForm, @AuthenticationPrincipal CustomUserDetails user, RedirectAttributes redirectAttributes) {
        Review review = new Review();
        Project project = projectService.getProjectById(reviewForm.getProjectId());
        review.setProjectId(reviewForm.getProjectId());
        review.setUserId(user.getUserId());
        review.setUserNickname(user.getUserNickname());
        review.setUserAddress(user.getAddress());
        review.setUserAgeGroup(AgeUtil.calculateGeneration(user.getBirthYear()));
        review.setUserSelfIntroduction(user.getUserSelfIntroduction());
        review.setBookIsbn(reviewForm.getIsbn());
        review.setBookTitle(reviewForm.getBookTitle());
        review.setBookPublisher(reviewForm.getPublisher());
        review.setBookAuthor(reviewForm.getAuthor());
        review.setReviewTitle(reviewForm.getReviewTitle());
        review.setReviewContent(reviewForm.getReviewContent());
        review.setDraft(false);

        reviewService.setReview(review);

        redirectAttributes.addFlashAttribute("project", projectService.getProjectById(reviewForm.getProjectId()));
        return "redirect:/review/complete";
    }

    @GetMapping("/complete")
    public String completeReview(Model model, Project project){
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, project.getSubmissionEndAt());
        model.addAttribute("duration", duration);

        return "books/book-complete";
    }

    @GetMapping("/draft")
    public String editDraftReview(Model model, @RequestParam long reviewId, @AuthenticationPrincipal CustomUserDetails user){
        Review review = reviewService.getReviewById(reviewId);
        Project project = projectService.getProjectById(review.getProjectId());
        if(review == null || !review.isDraft() || review.getUserId() != user.getUserId() || project.getStatus() != ProjectStatus.DURING_SUBMISSION){
            throw new AccessDeniedException();
        }

        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setProjectId(review.getProjectId());
        reviewForm.setBookTitle(review.getBookTitle());
        reviewForm.setAuthor(review.getBookAuthor());
        reviewForm.setPublisher(review.getBookPublisher());
        reviewForm.setIsbn(review.getBookIsbn());
        reviewForm.setReviewTitle(review.getReviewTitle());
        reviewForm.setReviewContent(review.getReviewContent());


        model.addAttribute("review", reviewForm);
        model.addAttribute("draft", true);

        return  "books/review-form";
    }

    @PostMapping("/draft")
    public String draftReview(ReviewForm reviewForm, @AuthenticationPrincipal CustomUserDetails user) {
        Review review = new Review();
        Project project = projectService.getProjectById(reviewForm.getProjectId());
        review.setProjectId(reviewForm.getProjectId());
        review.setUserId(user.getUserId());
        review.setUserNickname(user.getUserNickname());
        review.setUserAddress(user.getAddress());
        review.setUserAgeGroup(AgeUtil.calculateGeneration(user.getBirthYear()));
        review.setUserSelfIntroduction(user.getUserSelfIntroduction());
        review.setBookIsbn(reviewForm.getIsbn());
        review.setBookTitle(reviewForm.getBookTitle());
        review.setBookPublisher(reviewForm.getPublisher());
        review.setBookAuthor(reviewForm.getAuthor());
        review.setReviewTitle(reviewForm.getReviewTitle());
        review.setReviewContent(reviewForm.getReviewContent());
        review.setDraft(true);

        reviewService.setReview(review);

        return "redirect:/review/draft?reviewId=" + review.getId();
    }

    @PostMapping("/draft/update")
    public String updateDraftReview(ReviewForm review, @RequestParam long reviewId){
        Review draftReview = reviewService.getReviewById(reviewId);
        draftReview.setReviewContent(review.getReviewContent());
        draftReview.setReviewTitle(review.getReviewTitle());
        draftReview.setDraft(true);

        reviewService.updateDraftReview(draftReview);
        return "redirect:/review/draft?reviewId=" + reviewId;
    }

    @PostMapping("/draft/new")
    public String draftNewReview(ReviewForm review, RedirectAttributes redirectAttributes, @RequestParam long reviewId){
        redirectAttributes.addFlashAttribute("reviewForm", review);
        redirectAttributes.addFlashAttribute("id", reviewId);
        return "redirect:/review/draft/confirm";
    }

    @GetMapping("/draft/confirm")
    public String draftConfirm(Model model){
        model.addAttribute("draft", true);

        return  "books/book-review";
    }

    @PostMapping("/draft/confirm")
    public String updateDraftReview(ReviewForm reviewForm, RedirectAttributes redirectAttributes, @RequestParam long id){
        Review draftReview = reviewService.getReviewById(id);
        draftReview.setReviewContent(reviewForm.getReviewContent());
        draftReview.setReviewTitle(reviewForm.getReviewTitle());
        draftReview.setDraft(false);

        reviewService.updateDraftReview(draftReview);
        redirectAttributes.addFlashAttribute("project", projectService.getProjectById(reviewForm.getProjectId()));

        return "redirect:/review/complete";
    }

    @PostMapping("/draft/delete")
    public String deleteDraftReview(Review review, @AuthenticationPrincipal CustomUserDetails user){
        if(review.getUserId() != user.getUserId()){

        }

        reviewService.deleteReviewById(review.getId());
        return "redirect:/mypage";
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails user){
        Review review = reviewService.getReviewById(id);
        Project project = projectService.getProjectById(review.getProjectId());
        if(review.getUserId() != user.getUserId() || project.getStatus() != ProjectStatus.DURING_SUBMISSION){
            throw new IllegalStateException();
        }
        reviewService.deleteReviewById(review.getId());
        String toast = "削除完了";
        redirectAttributes.addFlashAttribute("toast", toast);
        return "redirect:/mypage";
    }

    @GetMapping("/edit/{id}")
    public String editReview(@PathVariable long id, Model model, @AuthenticationPrincipal CustomUserDetails user){
        Review review = reviewService.getReviewById(id);
        if(review == null || review.getUserId() != user.getUserId()){
            throw new AccessDeniedException();
        }
        Project project = projectService.getProjectById(review.getProjectId());
        if(project.getStatus() != ProjectStatus.DURING_SUBMISSION){
            throw new AccessDeniedException();
        }

        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setProjectId(review.getProjectId());
        reviewForm.setBookTitle(review.getBookTitle());
        reviewForm.setAuthor(review.getBookAuthor());
        reviewForm.setPublisher(review.getBookPublisher());
        reviewForm.setIsbn(review.getBookIsbn());
        reviewForm.setReviewTitle(review.getReviewTitle());
        reviewForm.setReviewContent(review.getReviewContent());
        model.addAttribute("reviewId", review.getId());
        model.addAttribute("reviewForm", reviewForm);
        model.addAttribute("project", project);

        return "books/book-detail-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateReview(@PathVariable long id, ReviewForm reviewForm, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails user){
        Review review = reviewService.getReviewById(id);
        if(review == null || review.getUserId() != user.getUserId()){
            throw new AccessDeniedException();
        }

        reviewService.editReview(reviewForm, id);
        String toast = "編集完了";
        redirectAttributes.addFlashAttribute("toast", toast);

        return "redirect:/mypage";
    }

}
