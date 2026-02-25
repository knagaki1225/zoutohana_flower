package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.admin.project.ProjectCard;
import com.example.zoutohanafansite.entity.admin.review.NominatedReviewCard;
import com.example.zoutohanafansite.entity.admin.review.ReviewList;
import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.form.*;
import com.example.zoutohanafansite.entity.genre.Genre;
import com.example.zoutohanafansite.entity.notificationtemplate.NotificationTemplate;
import com.example.zoutohanafansite.entity.post.Post;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.entity.admin.review.ReviewCard;
import com.example.zoutohanafansite.entity.review.Review;
import com.example.zoutohanafansite.entity.validator.ProjectValidator;
import com.example.zoutohanafansite.security.CustomAdminUserDetails;
import com.example.zoutohanafansite.service.*;
import org.apache.ibatis.type.TypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProjectService projectService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final NotificationTemplateService notificationTemplateService;
    private final NotificationService notificationService;
    private final PostService postService;
    private final GenreService genreService;
    private final ReviewGenreService reviewGenreService;
    private final ObjectMapper objectMapper;
    private final ImageService imageService;
    private final NominatedReviewService nominatedReviewService;
    private final ValidationService validationService;

    public AdminController(ProjectService projectService, UserService userService, ReviewService reviewService, NotificationTemplateService notificationTemplateService, NotificationService notificationService, PostService postService, GenreService genreService, ReviewGenreService reviewGenreService, ObjectMapper objectMapper, NominatedReviewService nominatedReviewService, ImageService imageService, ValidationService validationService) {
        this.projectService = projectService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.notificationTemplateService = notificationTemplateService;
        this.notificationService = notificationService;
        this.postService = postService;
        this.genreService = genreService;
        this.reviewGenreService = reviewGenreService;
        this.objectMapper = objectMapper;
        this.imageService = imageService;
        this.nominatedReviewService = nominatedReviewService;
        this.validationService = validationService;
    }

    @GetMapping("/dash")
    public String admin(Model model){
//        List<Project> projects = projectService.getAllOngoingProjects();
//        List<AdminDashProject> adminDashProjects = new ArrayList<>();
//        for(Project project : projects){
//            adminDashProjects.add(new AdminDashProject(project));
//        }
//        model.addAttribute("projects",adminDashProjects);

        List<ProjectCard> projects = projectService.getAllOngoingProjectsAdmin();
        model.addAttribute("projects", projects);

        return "admin/top";
    }

    @GetMapping("/account/list")
    public String accountList(UserSearchForm form, Model model){
        List<User> users = userService.getAllUsers(form);
        model.addAttribute("users", users);
        model.addAttribute("form", form);
        return "admin/account_list";
    }

    @GetMapping("/account/view")
    public String accountView(@RequestParam(value="loginId", required = false) String loginId, Model model) {
        if (loginId == null || loginId.isEmpty()) {
            return "redirect:admin/account/list";
        }
        User user = userService.getUserByLoginId(loginId);
        List<ReviewCard> reviews = reviewService.getReviewCardsByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviews);
        return "admin/account_view";
    }

    @PostMapping("/account/view")
    public String accountStatusUpdate(String status, long id) {
        userService.updateStatus(status, id);
        User user = userService.getUserById(id);
        return "redirect:/admin/account/view?loginId=" + user.getLoginId();
    }

    @GetMapping("/project/create")
    public String createProject(Model model){
        if(model.containsAttribute("form")){
            return "admin/project_create";
        }
        AdminProjectCreateForm form = new AdminProjectCreateForm();
        model.addAttribute("form", form);
        return "admin/project_create";
    }

    @PostMapping("/project/create")
    public String createProject(AdminProjectCreateForm form, RedirectAttributes redirectAttributes) {
        ProjectValidator projectValidator = validationService.validateProject(form);
        if (projectValidator.isValid()) {
            redirectAttributes.addFlashAttribute("projectValidator", projectValidator);
            redirectAttributes.addFlashAttribute("form", form);
            return "redirect:/admin/project/create";
        }
        Project createdProject = projectService.createProject(form);
        redirectAttributes.addFlashAttribute("id", createdProject.getId());
        return "redirect:/admin/project/create/image";
    }

    @GetMapping("/project/create/image")
    public String createProjectImage(Model model, @ModelAttribute("id") Long id){
        AdminCreateProjectImageForm form = new AdminCreateProjectImageForm();
        form.setId(id);
        model.addAttribute("form", form);
        return "admin/project_create_image";
    }

    @PostMapping("/project/create/image")
    public String insertImage(AdminCreateProjectImageForm form) throws IOException {
        String fileName = imageService.saveImage(form.getImage());
        projectService.updateImageUrl(fileName, form.getId());
        Project project =  projectService.getProjectById(form.getId());
        return "redirect:/admin/project/view?id=" + project.getUrlKey();
    }

    @GetMapping("/project/list")
    public String projectList(ProjectSearchForm form, Model model) {
        List<ProjectCard> projects = projectService.getAllProjects(form);
        model.addAttribute("projects", projects);
        model.addAttribute("form", form);
        return "admin/project_list";
    }

    @GetMapping("/project/view")
    public String projectView(@RequestParam(value="urlKey", required = false) String urlKey, Model model) {
        if (urlKey == null || urlKey.isEmpty()) {
            return "redirect:/admin/project/list";
        }
        Project project = projectService.getProjectByUrlKey(urlKey);
        AdminProjectEditForm form = new AdminProjectEditForm();
        form.setId(project.getId());
        form.setPublished(project.isPublished());
        form.setStatus(project.getStatus());
        form.setName(project.getName());
        form.setUrlKey(project.getUrlKey());
        form.setIntroduction(project.getIntroduction());
        form.setThemeColor(project.getThemeColor());
        form.setProjectStartAt(project.getProjectStartAt());
        form.setProjectEndAt(project.getProjectEndAt());
        form.setSubmissionStartAt(project.getSubmissionStartAt());
        form.setSubmissionEndAt(project.getSubmissionEndAt());
        form.setVotingStartAt(project.getVotingStartAt());
        form.setVotingEndAt(project.getVotingEndAt());

        List<NominatedReviewCard> nominatedReviews = nominatedReviewService.getNominatedReviewCardByProjectId(project.getId());
        List<NominatedReviewCard> awardedReviews = nominatedReviewService.getAwardedReviewCardByProjectId(project.getId());
        model.addAttribute("project", form);
        model.addAttribute("fileName", project.getMainImgUrl());
        model.addAttribute("nominatedReviews", nominatedReviews);
        model.addAttribute("awardedReviews", awardedReviews);
        return "admin/project_edit";
    }

    @PostMapping("/project/view/{urlKey}")
    public String projectUpdate(AdminProjectEditForm form, @PathVariable String urlKey) {
        Project project =  projectService.getProjectByUrlKey(urlKey);
        project.setName(form.getName());
        if(!form.getUrlKey().equals(urlKey) && projectService.getProjectByUrlKey(form.getUrlKey()) != null){
            return "redirect:/admin/project/view?error&urlKey=" + urlKey;
        }
        project.setUrlKey(form.getUrlKey());
        project.setIntroduction(form.getIntroduction());
        project.setThemeColor(form.getThemeColor());
        project.setProjectStartAt(form.getProjectStartAt());
        project.setProjectEndAt(form.getProjectEndAt());
        project.setSubmissionStartAt(form.getSubmissionStartAt());
        project.setSubmissionEndAt(form.getSubmissionEndAt());
        project.setVotingStartAt(form.getVotingStartAt());
        project.setVotingEndAt(form.getVotingEndAt());
        project.setPublished(form.isPublished());

        projectService.updateProject(project);
        return "redirect:/admin/project/view?urlKey=" + project.getUrlKey();
    }

    @PostMapping("/project/delete")
    public String deleteProject(
            @RequestParam Long projectId,
            @RequestParam String inputUrlKey,
            RedirectAttributes ra) {

        Project project = projectService.getProjectById(projectId);

        if (!project.getUrlKey().equals(inputUrlKey)) {
            ra.addFlashAttribute("errorMessage", "企画URLが一致しません");
            return "redirect:/admin/project/list";
        }

        projectService.deleteProjectById(projectId);

        ra.addFlashAttribute("successMessage", "企画を削除しました");
        return "redirect:/admin/project/list";
    }

    @GetMapping("/review/list")
    public String reviewList(@RequestParam String urlKey, ReviewSearchForm form, Model model) {
        Project project = projectService.getProjectByUrlKey(urlKey);
        List<ReviewList> reviews = reviewService.getReviewsByUrlKey(form, urlKey);
        List<Genre> genres = genreService.selectAllGenre();
        model.addAttribute("project", project);
        model.addAttribute("reviews", reviews);
        model.addAttribute("form", form);
        model.addAttribute("genres", genres);
        return "admin/review_list";
    }

    @GetMapping("/review/view")
    public String reviewView(@RequestParam long id, Model model) {
        ReviewCard reviewCard = reviewService.getReviewCardById(id);
        List<Genre> genreOptions = genreService.selectAllGenre();
        List<Genre> thisReviewGenres = reviewGenreService.getGenresByReviewId(id);
        model.addAttribute("review", reviewCard);
        model.addAttribute("genreOptions", genreOptions);
        model.addAttribute("thisReviewGenres", thisReviewGenres);
        return "admin/review_edit";
    }

    // 書評のステータス更新
    @PostMapping("/review/statusUpdateSingle")
    public String reviewStatusUpdateSingle(
            @RequestParam Long reviewId,
            @RequestParam String targetStatus) {

        reviewService.changeStatusSingle(reviewId, targetStatus);
        return "redirect:/admin/review/view?id=" + reviewId;
    }

    // 書評一覧のステータス一括変更
    @PostMapping("/review/statusUpdate")
    public String statusUpdate(
            ReviewSearchForm form,
            @RequestParam String urlKey,
            @RequestParam List<Long> reviewIds,
            @RequestParam String targetStatus,
            RedirectAttributes redirectAttributes) {

        reviewService.changeStatus(reviewIds, targetStatus);

        redirectAttributes.addAttribute("urlKey", urlKey);
        redirectAttributes.addAttribute("keyword", form.getKeyword());
        redirectAttributes.addAttribute("sort", form.getSort());

        if (form.getStatus() != null) {
            for (String s : form.getStatus()) {
                redirectAttributes.addAttribute("status", s);
            }
        }
        if (form.getGenreIds() != null) {
            for (Long gid : form.getGenreIds()) {
                redirectAttributes.addAttribute("genreIds", gid);
            }
        }
        if (form.getUserGender() != null) {
            for (String g : form.getUserGender()) {
                redirectAttributes.addAttribute("userGender", g);
            }
        }
        if (form.getUserAgeGroup() != null) {
            for (String a : form.getUserAgeGroup()) {
                redirectAttributes.addAttribute("userAgeGroup", a);
            }
        }
        if (form.getUserAddress() != null) {
            for (String addr : form.getUserAddress()) {
                redirectAttributes.addAttribute("userAddress", addr);
            }
        }

        redirectAttributes.addAttribute("createdStartAt", form.getCreatedStartAt());
        redirectAttributes.addAttribute("createdEndAt", form.getCreatedEndAt());

        return "redirect:/admin/review/list";
    }

    @PostMapping("/review/genreUpdate")
    public String genreUpdate(
            @RequestParam Long reviewId,
            @RequestParam List<Long> genres) {

        reviewGenreService.updateReviewGenres(reviewId, genres);

        return "redirect:/admin/review/view?id=" + reviewId;
    }

    @PostMapping("/review/delete")
    public String deleteReview(
            @RequestParam String urlKey,
            @RequestParam Long reviewId,
            @RequestParam String inputUserLoginId,
            RedirectAttributes ra) {

        ReviewCard review = reviewService.getReviewCardById(reviewId);

        if (!review.getUserLoginId().equals(inputUserLoginId)) {
            ra.addFlashAttribute("errorMessage", "投稿者のIDが一致しません");
            return "redirect:/admin/review/view?id=" + reviewId;
        }

        reviewService.deleteReviewById(reviewId);

        ra.addFlashAttribute("successMessage", "書評を削除しました");
        return "redirect:/admin/review/list?urlKey=" + urlKey;
    }

    @GetMapping("/review/print")
    public String reviewPrint(@RequestParam long id, Model model) {
        ReviewCard reviewCard = reviewService.getReviewCardById(id);
        List<Genre> thisReviewGenres = reviewGenreService.getGenresByReviewId(id);
        model.addAttribute("review", reviewCard);
        model.addAttribute("thisReviewGenres", thisReviewGenres);
        return "admin/review_print";
    }

    @GetMapping("/review/export")
    public ResponseEntity<byte[]> exportReviewsCsv(@RequestParam String urlKey) throws IOException {

        List<ReviewCard> reviews = reviewService.selectReviewCardForExport(urlKey);
        String csv = reviewService.generateCsv(reviews);

        String fileName = "reviews_" + urlKey + ".csv";

        // BOM追加（Excel文字化け防止）
        byte[] bom = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
        byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(bom);
        out.write(csvBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(out.toByteArray());
    }

    @GetMapping("/genre/list")
    public String genreList(GenreSearchForm form, Model model) {
        List<Genre> genres = genreService.getGenreList(form);
        model.addAttribute("genres", genres);
        model.addAttribute("form", form);
        return "admin/book_genre_list";
    }

    @PostMapping("/genre/create")
    public String createGenre(Genre genre) {
        genreService.createGenre(genre);
        return "redirect:/admin/genre/list";
    }

    @PostMapping("/genre/update")
    public String updateGenre(Genre genre) {
        genreService.updateGenre(genre);
        return "redirect:/admin/genre/list";
    }

    @PostMapping("/genre/delete")
    public String deleteGenre(@RequestParam Long id) {
        genreService.deleteById(id);
        return "redirect:/admin/genre/list";
    }

    @GetMapping("/notification/template")
    public String notificationTemplateList(Model model, @RequestParam(defaultValue = "") String s){
        List<NotificationTemplate> notificationTemplates;
        if(s.isEmpty()){
            notificationTemplates = notificationTemplateService.selectAllNotificationTemplate();
        }else{
            notificationTemplates = notificationTemplateService.selectNotificationTemplateByKeyword(s);
        }

        model.addAttribute("notificationTemplates", notificationTemplates);
        return "admin/notification_template_list";
    }

    @GetMapping("/notification/template/create")
    public String notificationTemplateCreate(Model model){
        AdminNotificationTemplateForm adminNotificationTemplateForm = new AdminNotificationTemplateForm();
        model.addAttribute("adminNotificationTemplateForm", adminNotificationTemplateForm);
        return "admin/notification_template_create";
    }

    @PostMapping("/notification/template/create")
    public String notificationTemplateGet(AdminNotificationTemplateForm form, RedirectAttributes redirectAttributes){
        int i = 1;
        redirectAttributes.addFlashAttribute("form", form);
        i++;
        return "redirect:/admin/notification/template/create/confirm";
    }

    @GetMapping("/notification/template/create/confirm")
    public String notificationTemplateCreateConfirm(){
        return "admin/notification_template_create_confirm";
    }

    @PostMapping("/notification/template/create/confirm")
    public String notificationTemplateInsert(AdminNotificationTemplateForm form){
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setName(form.getTemplateName());
        notificationTemplate.setTitle(form.getTitle());
        notificationTemplate.setContent(form.getContent());
        notificationTemplateService.insertNotificationTemplate(notificationTemplate);
        return "redirect:/admin/notification/template";
    }

    @GetMapping("/notification/template/edit/{id}")
    public String notificationTemplateEdit(@PathVariable long id, Model model){
        NotificationTemplate notificationTemplate = notificationTemplateService.selectNotificationTemplateById(id);
        AdminNotificationTemplateForm form =  new AdminNotificationTemplateForm();
        form.setTemplateName(notificationTemplate.getName());
        form.setTitle(notificationTemplate.getTitle());
        form.setContent(notificationTemplate.getContent());
        model.addAttribute("form", form);
        model.addAttribute("templateId", id);
        return "admin/notification_template_edit";
    }

    @PostMapping("/notification/template/edit/{id}")
    public String notificationTemplateEditPost(AdminNotificationTemplateForm form, RedirectAttributes redirectAttributes, @PathVariable long id){
        redirectAttributes.addFlashAttribute("form", form);
        NotificationTemplate notificationTemplate = notificationTemplateService.selectNotificationTemplateById(id);
        return "redirect:/admin/notification/template/edit/confirm/" + notificationTemplate.getId();
    }

    @GetMapping("/notification/template/edit/confirm/{id}")
    public String notificationTemplateEditConfirm(@PathVariable long id, Model model){
        model.addAttribute("id", id);
        return "admin/notification_template_edit_confirm";
    }

    @PostMapping("/notification/template/edit/confirm/{id}")
    public String notificationTemplateUpdate(AdminNotificationTemplateForm form, @PathVariable long id){
        notificationTemplateService.update(form,id);
        return "redirect:/admin/notification/template";
    }

    @PostMapping("/notification/template/delete/{id}")
    public String deleteNotificationTemplate(@PathVariable long id){
        notificationTemplateService.deleteNotificationTemplate(id);
        return "redirect:/admin/notification/template";
    }

    @GetMapping("/notification/send/{id}")
    public String notificationSend(@PathVariable long id, Model model){
        ReviewCard reviewCard = reviewService.getReviewCardById(id);
        model.addAttribute("review", reviewCard);

        AdminNotificationSendForm adminNotificationSendForm = new AdminNotificationSendForm();
        model.addAttribute("form", adminNotificationSendForm);

        return "admin/notification";
    }

    @PostMapping("/notification/send/{id}")
    public String notificationSendPost(@PathVariable long id, Model model, AdminNotificationSendForm form, @AuthenticationPrincipal CustomAdminUserDetails user){
        notificationService.insertNotificationByForm(form, id, user.getUserId());

        return "redirect:/admin/notification/template";
    }

    @GetMapping("/post/list")
    public String postList(PostSearchForm form, Model model) {
        List<Post> posts = postService.getAllPosts(form);
        model.addAttribute("posts", posts);
        model.addAttribute("form", form);
        return "admin/post_list";
    }

    @GetMapping("/post/view")
    public String postView(@RequestParam(value="id", required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/admin/post/list";
        }
        Post post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/admin/post/list";
        }
        model.addAttribute("post", post);
        return "admin/post_edit";
    }

    @PostMapping("/post/view")
    public String postUpdate(Post post) {
        postService.updatePost(post);
        return "redirect:/admin/post/view?id=" + post.getId();
    }

    @PostMapping("/post/delete")
    public String deletePost(@RequestParam Long id) {
        postService.deletePostById(id);
        return "redirect:/admin/post/list";
    }

    @GetMapping("/post/create")
    public String createPostForm(){
        return "admin/post_create";
    }

    @PostMapping("/post/create")
    public String createPost(Post post, @AuthenticationPrincipal CustomAdminUserDetails user) {
        post.setAdminId((int) user.getUserId());
        Post createdPost = postService.createPost(post);
        return "redirect:/admin/post/view?id=" + createdPost.getId();
    }
}
