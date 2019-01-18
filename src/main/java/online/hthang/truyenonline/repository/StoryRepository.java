package online.hthang.truyenonline.repository;

import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.projections.*;
import online.hthang.truyenonline.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */

@Repository
public interface StoryRepository extends JpaRepository< Story, Long > {

    List< Story > findTop10ByStatus(Integer sStatus);

    /**
     * Lấy Danh sách Truyện Mới
     *
     * @param listChStatus
     * @param listStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_NEW_UPDATE,
            countQuery = ConstantsQueryUtils.COUNT_STORY_NEW_UPDATE,
            nativeQuery = true)
    Page< NewStory > getStoryNew(@Param("chapterStatus") List< Integer > listChStatus,
                                 @Param("storyStatus") List< Integer > listStatus,
                                 Pageable pageable);

    /**
     * Lấy Danh sách Truyện Mới Theo Thể Loại
     *
     * @param listChStatus
     * @param cID
     * @param listStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_NEW_UPDATE_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_NEW_UPDATE_BY_CATEGORY,
            nativeQuery = true)
    Page< NewStory > findStoryNewByCategory(@Param("chapterStatus") List< Integer > listChStatus,
                                            @Param("categoryId") Integer cID,
                                            @Param("storyStatus") List< Integer > listStatus,
                                            Pageable pageable);

    /**
     * Lấy Danh sách Truyện Top
     *
     * @param startDate
     * @param endDate
     * @param listStatus
     * @param favoritesStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VIEW,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VIEW,
            nativeQuery = true)
    Page< TopStory > getTopStory(@Param("storyStatus") List< Integer > listStatus,
                                 @Param("startDate") Date startDate,
                                 @Param("endDate") Date endDate,
                                 @Param("favoritesStatus") Integer favoritesStatus,
                                 Pageable pageable);

    /**
     * Lấy Danh sách Truyện Top Đề Cử
     *
     * @param listStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_APPOIND,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_APPOIND,
            nativeQuery = true)
    Page< TopStory > getTopStoryAppoind(@Param("storyStatus") List< Integer > listStatus,
                                        Pageable pageable);

    /**
     * Lấy Danh sách Truyện Top Theo Category
     *
     * @param cID
     * @param favoritesStatus
     * @param startDate
     * @param endDate
     * @param listStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VIEW_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VIEW_BY_CATEGORY,
            nativeQuery = true)
    Page< TopStory > getTopStoryByCategory(@Param("categoryID") Integer cID,
                                           @Param("favoritesStatus") Integer favoritesStatus,
                                           @Param("storyStatus") List< Integer > listStatus,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate, Pageable pageable);

    /**
     * Lấy Danh sách Truyện VIP Top
     *
     * @param startDate
     * @param endDate
     * @param sDealStatus
     * @param listStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_VIP_TOP_VIEW,
            countQuery = ConstantsQueryUtils.COUNT_STORY_VIP_TOP_VIEW,
            nativeQuery = true)
    Page< TopStory > getTopStoryVip(@Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate,
                                    @Param("storyDealStatus") Integer sDealStatus,
                                    @Param("storyStatus") List< Integer > listStatus,
                                    Pageable pageable);

    /**
     * Lấy Danh sách Truyện Hoàn Thành
     *
     * @param listStatus
     * @param sStatus
     * @param pageable
     * @return Page<NewStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_COMPLETE,
            countQuery = ConstantsQueryUtils.COUNT_STORY_COMPLETE,
            nativeQuery = true)
    Page< NewStory > getPageStoryComplete(@Param("chapterStatus") List< Integer > listStatus,
                                          @Param("storyStatus") Integer sStatus,
                                          Pageable pageable);

    /**
     * Lấy Danh sách Truyện Hoàn Thành
     *
     * @param listChStatus
     * @param pageable
     * @param listStatus
     * @return Page<NewStory>
     */
    @Query(value = ConstantsQueryUtils.VIP_STORY_NEW_UPDATE,
            countQuery = ConstantsQueryUtils.COUNT_VIP_STORY_NEW_UPDATE,
            nativeQuery = true)
    Page< NewStory > getVipStoryNew(@Param("chapterStatus") List< Integer > listChStatus,
                                    @Param("storyStatus") List< Integer > listStatus,
                                    @Param("storyDealStatus") Integer sDealStatus,
                                    Pageable pageable);

    /**
     * Lấy Top View Truyện Hoàn Thành Trong Tháng
     *
     * @param sStatus
     * @param startDate
     * @param endDate
     * @param pageable
     * @return Page<Story>
     */
    @Query(value = ConstantsQueryUtils.COMPLETE_STORY_TOP_VIEW_SWAPPER,
            countQuery = ConstantsQueryUtils.COUNT_COMPLETE_STORY_TOP_VIEW_SWAPPER,
            nativeQuery = true)
    Page< Story > getCompleteStoryTopView(@Param("storyStatus") Integer sStatus, @Param("startDate") Date startDate,
                                          @Param("endDate") Date endDate, Pageable pageable);

    /**
     * Lấy Top View Truyện Hoàn Thành Trong Tháng
     *
     * @param searchName
     * @param sStatus
     * @return Page<SearchStory>
     */
    List< SearchStory > findTop10ByVnNameContainingAndStatusNot(String searchName, Integer sStatus);

    /**
     * Kiểm tra Tồn Tại Story Theo ID Và Status
     *
     * @param sStatus
     * @param sID
     * @return boolean
     */
    boolean existsStoryByIdAndStatusNot(Long sID, Integer sStatus);

    /**
     * Tìm kiếm Danh sách Truyện Theo
     *
     * @param listChStatus
     * @param listStatus
     * @param pageable
     * @return Page<NewStory>
     */
    @Query(value = ConstantsQueryUtils.SEARCH_STORY,
            countQuery = ConstantsQueryUtils.COUNT_SEARCH_STORY,
            nativeQuery = true)
    Page< NewStory > getSearchStory(@Param("chapterStatus") List< Integer > listChStatus,
                                    @Param("search") String search,
                                    @Param("storyStatus") List< Integer > listStatus,
                                    Pageable pageable);

    /**
     * Tìm Truyện Theo
     *
     * @param sID
     * @param listStatus
     * @return Optional<StorySummary>
     */
    Optional< StorySummary > findByIdAndStatusIn(Long sID, List< Integer > listStatus);

    /**
     * Lấy Danh Sách Truyện Theo converter
     *
     * @param uID
     * @param listStatus
     * @return List<TopStory>
     */
    List< SearchStory > findTop5ByUser_IdAndStatusInOrderByCreateDateDesc(Long uID, List< Integer > listStatus);

    /**
     * Lấy Danh Sách Truyện Theo Converter
     *
     * @param uID
     * @param listStatus
     * @return Page<MemberStorySummary>
     */
    Page< MemberStorySummary > findByUser_IdAndStatusInOrderByCreateDateDesc(Long uID,
                                                                             List< Integer > listStatus,
                                                                             Pageable pageable);

    /**
     * Lấy Danh Sách Truyện Theo Converter
     *
     * @param uID
     * @param status
     * @return Page<StoryConverterSummary>
     */
    Page< StoryConverterSummary > findByUser_IdAndStatusOrderByCreateDateDesc(Long uID,
                                                                              Integer status,
                                                                              Pageable pageable);

    /**
     * Đếm số truyện đăng bởi uID
     *
     * @param uID
     * @param listStatus
     * @return Long
     */
    Long countByUser_IdAndStatusIn(Long uID, List< Integer > listStatus);

    /**
     * Lấy Top 3 Truyện Mới Đăng Của Converter
     *
     * @param uID
     * @param listStatus
     * @return List<SearchStory>
     */
    List< SearchStory > findTop3ByUser_IdAndStatusInOrderByCreateDateDesc(Long uID,
                                                                          List< Integer > listStatus);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE story s SET s.countAppoint = 0 WHERE s.status IN :listStatus", nativeQuery = true)
    void updateAppoindStory(@Param("listStatus") List< Integer > listStatus);

}
