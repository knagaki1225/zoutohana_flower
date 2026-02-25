package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.admin.review.NominatedReviewCard;
import com.example.zoutohanafansite.entity.admin.review.ReviewList;
import com.example.zoutohanafansite.entity.form.ReviewForm;
import com.example.zoutohanafansite.entity.form.ReviewSearchForm;
import com.example.zoutohanafansite.entity.pagination.PaginationView;
import com.example.zoutohanafansite.entity.review.Review;
import com.example.zoutohanafansite.entity.review.ReviewApiData;
import com.example.zoutohanafansite.entity.admin.review.ReviewCard;
import com.example.zoutohanafansite.entity.review.ReviewPagination;
import com.example.zoutohanafansite.mapper.ReviewMapper;
import com.example.zoutohanafansite.repository.NominatedReviewRepository;
import com.example.zoutohanafansite.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final NominatedReviewRepository nominatedReviewRepository;
    private final PaginationService paginationService;
    private final ProjectService projectService;

    public ReviewService(ReviewRepository reviewRepository, NominatedReviewRepository nominatedReviewRepository, PaginationService paginationService, ProjectService projectService) {
        this.reviewRepository = reviewRepository;
        this.nominatedReviewRepository = nominatedReviewRepository;
        this.paginationService = paginationService;
        this.projectService = projectService;
    }

    /**
     * 新規review登録
     *
     * @param review 新規登録するreview
     */
    public void setReview(Review review) {
        reviewRepository.insertReview(review);
    }

    /**
     * reviewIdを指定してreview取得
     *
     * @param id 指定するreviewId
     * @return Review
     */
    public Review getReviewById(long id){
        return reviewRepository.selectReviewById(id);
    }

    /**
     * 下書き保存したreviewの更新
     * 下書き保存のまま更新の場合はproject.draftをtrue
     *
     * @param review 更新するreview
     */
    public void updateDraftReview(Review review){
        reviewRepository.updateDraftReview(review);
    }

    /**
     * reviewを論理削除
     *
     * @param id 削除するreviewId
     */
    public void deleteReviewById(long id){
        reviewRepository.deleteReviewById(id);
    }

    /**
     * 指定したuserのreviewを全件取得
     *
     * @param userId 指定するuserId
     * @return List<Review>
     **/
    public List<Review> getReviewByUserId(long userId){
        return reviewRepository.selectReviewByUserId(userId);
    }

    /**
     * reviewIdを指定してreview(プロジェクト名あり)を取得
     *
     * @param id 書評のid
     * @return ReviewCard
     **/
    public ReviewCard getReviewCardById(long id){
        return reviewRepository.selectReviewCardById(id);
    }

    /**
     * 指定したuserのreview(プロジェクト名あり)を全件取得
     *
     * @param userId 指定するuserId
     * @return List<ReviewCard>
     **/
    public List<ReviewCard> getReviewCardsByUserId(long userId){
        return reviewRepository.selectReviewCardsByUserId(userId);
    }

    /**
     * List<Review>の中に下書きのものがあるかどうか
     *
     * @param reviews 検索するreview
     * @return long 下書きのものがある場合そのreviewId、ない場合-1
     */
    public long isDraftReview(List<Review> reviews){
        for(Review review : reviews){
            if(review.isDraft()){
                return review.getId();
            }
        }
        return -1;
    }

    /**
     * projectIdとuserIdを指定して下書きのreviewIdを取得
     *
     * @param projectId
     * @param userId
     * @return Long ない場合はnull
     */
    public Long getDraftId(long projectId, long userId){
        return reviewRepository.selectDraftId(projectId, userId);
    }

    /**
     * userIdを取得して下書きではないreviewを全件取得
     *
     * @param userId
     * @return List<Review>
     */
    public List<Review> getAllReviewsNotDraftByUserId(long userId){
        return reviewRepository.selectAllReviewsNotDraftByUserId(userId);
    }

    /**
     * reviewの編集(投稿後)
     *
     * @param reviewForm
     */
    public void editReview(ReviewForm reviewForm, long id){
        reviewRepository.updateReview(reviewForm, id);
    }

    /**
     * 指定したurlKeyのprojectに属するreviewを全件取得
     *
     * @param urlKey 指定するurlKey
     * @return List<Review>
     */
    public List<Review> selectReviewsByUrlKey(String urlKey){
        return reviewRepository.selectReviewsByUrlKey(urlKey);
    }

    /**
     * 指定したurlKeyのprojectに属するreviewを全件取得(api用データに変換)
     *
     * @param urlKey 指定するurlKey
     * @return <ReviewApiData>
     */
    public List<ReviewApiData> getReviewApiData(String urlKey){
        List<Review> reviews = selectReviewsByUrlKey(urlKey);
        List<ReviewApiData> reviewApiDataList = new ArrayList<>();
        int i = 1;
        for(Review review : reviews){
            ReviewApiData reviewApiData = new ReviewApiData(review, "/api/image/book" + i + ".png");
            reviewApiDataList.add(reviewApiData);
            if(i == 4){
                i = 1;
            }else{
                i++;
            }
        }

        Collections.shuffle(reviewApiDataList);
        return reviewApiDataList;
    }

    /**
     * 指定したurlKeyのprojectに属するreviewのページネーション用データ取得
     *
     * @param urlKey
     * @param page 取得するページ数
     * @return ReviewPagination
     */
    public ReviewPagination getReviewApiData(String urlKey, int page){
        List<Review> reviews = selectReviewsByUrlKey(urlKey);
        List<ReviewApiData> reviewApiDataList = new ArrayList<>();
        LocalDateTime votingEndAt = projectService.getVotingEndAt(urlKey);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekLater = now.plusWeeks(1);
        boolean isVoteCount = votingEndAt.isAfter(now) && votingEndAt.isBefore(oneWeekLater.plusNanos(1));

        PaginationView paginationView = paginationService.getPaginationView(page, reviews.size(), 10);
        for(int i = paginationView.getStartNum(); i < paginationView.getEndNum(); i++){
            ReviewApiData reviewApiData = new ReviewApiData(reviews.get(i), "/api/image/book" + (i % 4 + 1) + ".png");
            if(isVoteCount){
                reviewApiData.setVoteCount(null);
            }
            if(isVoteCount){
                reviewApiData.setVoteCount(null);
            }
            reviewApiDataList.add(reviewApiData);
        }

        return new ReviewPagination(paginationView.getPaginationInfo(), reviewApiDataList);
    }

    /**
     * 指定したidのreviewのvoteCountを1プラス
     *
     * @param id
     */
    public void incrementVoteCount(long id){
        reviewRepository.incrementVoteCount(id);
    }

    /**
     * 指定したidのreviewのvoteCountを1マイナス
     *
     * @param id
     */
    public void decrementVoteCount(long id){
        reviewRepository.decrementVoteCount(id);
    }

    /**
     * 指定したurlKeyのprojectに属するreviewの中に指定したidのListの中に含まれる場合Reviewを返す
     *
     * @param urlKey
     * @param idList
     * @return Review
     */
    public List<Review> selectReviewByUrlKeyAndIdList(String urlKey, List<String> idList){
        List<Long> ids = new ArrayList<>();
        for(String id : idList){
            ids.add(Long.parseLong(id));
        }
        return reviewRepository.selectReviewByUrlKeyAndIdList(urlKey, ids);
    }

    /**
     * projectIdを指定してreviewを全件取得(管理者用)
     *
     * @oaram form ReviewSearchForm(検索条件)
     *              String sort, keyword
     *              List<String> status, published
     *              List<LocalDateTime> startAt, endAt
     *        projectId
     * @return List<ProjectCard>
     */
    public List<ReviewList> getReviewsByUrlKey(ReviewSearchForm form, String urlKey) {
        return reviewRepository.getReviewsByUrlKey(form, urlKey);
    }

    /**
     * 書評詳細での書評ステータス更新(書評一覧での一括操作に横流し)
     *
     * @param id ステータスを変更する書評のID
     * @param status 変更するステータス
     */
    @Transactional
    public void changeStatusSingle(Long id, String status) {
        changeStatus(List.of(id), status);
    }

    /**
     * 書評一覧でチェックした書評の一括操作
     *
     * @param ids ステータスを変更する書評たちのID
     * @param status 変更するステータス
     */
    @Transactional
    public void changeStatus(List<Long> ids, String status) {

        switch (status) {

            case "INITIAL":
                reviewRepository.updateFirstStagePassed(ids, false);
                nominatedReviewRepository.deleteByReviewIds(ids);
                break;

            case "FIRST_STAGE_PASSED":
                reviewRepository.updateFirstStagePassed(ids, true);
                nominatedReviewRepository.deleteByReviewIds(ids);
                break;

            case "SECOND_STAGE_PASSED":
                reviewRepository.updateFirstStagePassed(ids, true);
                nominatedReviewRepository.insertIgnore(ids);
                nominatedReviewRepository.updateAwarded(ids, false);
                break;

            case "AWARDED":
                reviewRepository.updateFirstStagePassed(ids, true);
                nominatedReviewRepository.insertIgnore(ids);
                nominatedReviewRepository.updateAwarded(ids, true);
                break;

            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }
    }

    /**
     * 指定したurlKeyのprojectに属するreviewを全件取得(CSVエクスポート用)
     *
     * @param urlKey 指定するurlKey
     * @return List<ReviewCard>
     */
    public List<ReviewCard> selectReviewCardForExport(String urlKey){
        return reviewRepository.selectReviewCardForExport(urlKey);
    }

    /**
     * 書評一覧をCSV形式に
     */
    public String generateCsv(List<ReviewCard> reviews) {
        StringBuilder sb = new StringBuilder();

        // ヘッダー
        sb.append("投稿日時,ステータス,書評ID,書評タイトル,書評本文,得票数,ISBN,書籍タイトル,出版社,著者,投稿者のログインID,投稿者名,投稿者の住所,投稿者の年代,投稿者の性別,投稿者の自己紹介\n");

        for (ReviewCard r : reviews) {
            sb.append(r.getCreatedAt()).append(",");
            sb.append(r.getStatus()).append(",");
            sb.append(r.getId()).append(",");
            sb.append(escape(r.getReviewTitle())).append(",");
            sb.append(escape(r.getReviewContent())).append(",");
            sb.append(escape(String.valueOf(r.getVoteCount()))).append(",");
            sb.append(escape(String.valueOf(r.getBookIsbn()))).append(",");
            sb.append(escape(r.getBookTitle())).append(",");
            sb.append(escape(r.getBookPublisher())).append(",");
            sb.append(escape(r.getBookAuthor())).append(",");
            sb.append(r.getUserLoginId()).append(",");
            sb.append(escape(r.getUserNickname())).append(",");
            sb.append(escape(r.getUserAddress())).append(",");
            sb.append(escape(String.valueOf(r.getUserAgeGroup()))).append(",");
            sb.append(escape(r.getUserGender().getLabel())).append(",");
            sb.append(escape(r.getUserSelfIntroduction())).append("\n");
        }

        return sb.toString();
    }

    // CSVエスケープ（改行・カンマ・ダブルクォート対応）
    private String escape(String s) {
        if (s == null) return "";
        return "\"" + s.replace("\"", "\"\"")
                .replace("\n", " ")
                .replace("\r", "") + "\"";
    }

}

