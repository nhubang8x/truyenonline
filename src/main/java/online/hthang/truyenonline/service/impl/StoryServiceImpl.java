package online.hthang.truyenonline.service.impl;

import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.projections.*;
import online.hthang.truyenonline.repository.StoryRepository;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.utils.ConstantsListUtils;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Service
@Transactional
public class StoryServiceImpl implements StoryService {

    private final static Logger logger = LoggerFactory.getLogger(StoryServiceImpl.class);
    private final StoryRepository storyRepository;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @Override
    public Story getStoryById(Long id) {
        Optional< Story > storyOptional = storyRepository.findById(id);
        return storyOptional.orElse(null);
    }

    /**
     * Lấy List Truyện Mới Cập Nhật
     *
     * @param page
     * @param size
     * @return Page<NewStory>
     */
    @Override
    public Page< NewStory > getStoryNew(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getStoryNew(ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsListUtils.LIST_STORY_DISPLAY, pageable);
    }

    /**
     * Lấy List Truyện Vip Mới Cập Nhật
     *
     * @param page
     * @param size
     * @return Page<NewStory>
     */
    @Override
    public Page< NewStory > getVipStoryNew(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getVipStoryNew(ConstantsListUtils.LIST_CHAPTER_DISPLAY,
                        ConstantsListUtils.LIST_STORY_DISPLAY, ConstantsUtils.STATUS_ACTIVED, pageable);
    }

    /**
     * Lấy List Truyện Mới Cập Nhật theo Category
     *
     * @param cID
     * @param page
     * @param size
     * @return Page<NewStory>
     */
    @Override
    public Page< NewStory > getStoryNewByCID(Integer cID, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findStoryNewByCategory(ConstantsListUtils.LIST_CHAPTER_DISPLAY, cID,
                        ConstantsListUtils.LIST_STORY_DISPLAY, pageable);
    }

    /**
     * Lấy List Truyện Mới Cập Nhật theo Category
     *
     * @param startDate
     * @param endDate
     * @param cID
     * @param page
     * @param size
     * @return Page<TopStory>
     */
    @Override
    public Page< TopStory > getTopStoryByCID(Date startDate, Date endDate, Integer cID, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository.getTopStoryByCategory(cID, ConstantsUtils.STATUS_ACTIVED, ConstantsListUtils.LIST_STORY_DISPLAY,
                startDate, endDate, pageable);
    }

    /**
     * Lấy List Truyện Top View Trong Khoảng
     *
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<TopStory>
     */
    @Override
    public Page< TopStory > getTopStory(Date startDate, Date endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getTopStory(ConstantsListUtils.LIST_STORY_DISPLAY, startDate,
                        endDate, ConstantsUtils.STATUS_ACTIVED, pageable);
    }

    /**
     * Lấy List Truyện Vip Top Deal Trong Khoảng
     *
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<TopStory>
     */
    @Override
    public Page< TopStory > getTopStoryVip(Date startDate, Date endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getTopStoryVip(startDate, endDate, ConstantsUtils.STATUS_ACTIVED,
                        ConstantsListUtils.LIST_STORY_DISPLAY, pageable);
    }

    /**
     * Lấy List Truyện Hoàn Thành Top View Trong Khoảng
     *
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<TopStory>
     */
    @Override
    public Page< TopStory > getTopStoryComplete(List< Integer > listStatus, Integer favoritesStatus, Date startDate, Date endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getTopStory(listStatus, startDate, endDate, favoritesStatus, pageable);
    }

    /**
     * Lấy Top 10 Truyện Mới hoàn thành
     *
     * @return List<Story>
     */
    @Override
    public List< Story > getNewStoryCompleted() {
        return storyRepository
                .findTop10ByStatus(ConstantsUtils.STORY_STATUS_COMPLETED);
    }

    /**
     * Lấy Page Truyện Hoàn Thành
     *
     * @param page
     * @param size
     * @return Page<NewStory>
     */
    @Override
    public Page< NewStory > getStoryCompletedByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getPageStoryComplete(ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsUtils.STORY_STATUS_COMPLETED, pageable);
    }

    /**
     * Lấy List Truyện Theo searchName
     *
     * @param searchName
     * @return Page<SearchStory>
     */
    @Override
    public List< SearchStory > getSearch(String searchName) {
        return storyRepository
                .findTop10ByVnNameContainingAndStatusNot(searchName,
                        ConstantsUtils.STORY_STATUS_HIDDEN);
    }

    /**
     * Kiểm tra Tồn Tại Truyện Theo
     *
     * @param sID
     * @return boolean
     */
    @Override
    public boolean searchStoryByID(Long sID) {
        return storyRepository.existsStoryByIdAndStatusNot(sID, ConstantsUtils.STORY_STATUS_HIDDEN);
    }

    /**
     * Cập nhật lươt xem
     *
     * @param story
     * @return void
     */
    @Override
    public void updateViewStory(Story story) {
        storyRepository.save(story);
    }

    /**
     * Tìm Kiếm List Truyện Mới Theo
     *
     * @param search
     * @param page
     * @param size
     * @return Page<NewStory>
     */
    @Override
    public Page< NewStory > searchStoryByPage(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getSearchStory(ConstantsListUtils.LIST_CHAPTER_DISPLAY, search,
                        ConstantsListUtils.LIST_STORY_DISPLAY, pageable);
    }

    /**
     * Lấy Truyện Theo
     *
     * @param sID
     * @param listStatus
     * @return Page<NewStory>
     */
    @Override
    public Optional< StorySummary > getStoryBySIDAndStatus(Long sID, List< Integer > listStatus) {
        return storyRepository.findByIdAndStatusIn(sID, listStatus);
    }

    /**
     * Lấy Danh Sách Truyện Đăng Của Converter
     *
     * @param uID
     * @param listStatus
     * @return List<SearchStory>
     */
    @Override
    public List< SearchStory > getListStoryOfConverter(Long uID, List< Integer > listStatus) {
        return storyRepository
                .findTop5ByUser_IdAndStatusInOrderByCreateDateDesc(uID, listStatus);
    }

    @Override
    public Page< MemberStorySummary > getStoryByConverter(List< Integer > listStatus, Long uID, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findByUser_IdAndStatusInOrderByCreateDateDesc(uID, listStatus, pageable);
    }

    /**
     * Đếm số truyện đăng bởi uID
     *
     * @param uID
     * @param listStatus
     * @return Long
     */
    @Override
    public Long countStoryByUser(Long uID, List< Integer > listStatus) {
        return storyRepository.countByUser_IdAndStatusIn(uID, listStatus);
    }

    /**
     * Lấy Truyện Theo
     *
     * @param sID
     * @return Page<Story>
     */
    @Override
    public Optional< Story > searchStoryBySID(Long sID) {
        return storyRepository.findById(sID);
    }

    /**
     * Lấy Top 3 Truyện Mới Đăng Của Converter
     *
     * @param uID
     * @param listStatus
     * @return List<SearchStory>
     */
    @Override
    public List< SearchStory > getTop3StoryOfConverter(Long uID, List< Integer > listStatus) {
        return storyRepository.findTop3ByUser_IdAndStatusInOrderByCreateDateDesc(uID, listStatus);
    }

    @Override
    public boolean saveNewStory(Story story) {
        Story storyRes = storyRepository.save(story);
        return storyRes.getId() != null;
    }

    /**
     * Lấy Danh Sách Truyện Bởi Converter
     *
     * @param status
     * @param uID
     * @param page
     * @param size
     * @return Page<MemberStorySummary>
     */
    @Override
    public Page< StoryConverterSummary > getStoryConverter(Integer status, Long uID, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findByUser_IdAndStatusOrderByCreateDateDesc(uID, status, pageable);
    }

    @Override
    public boolean saveEditStory(Story story) {
        Optional< Story > storyOptional = storyRepository.findById(story.getId());
        Story storyEdit = storyOptional.get();
        if (storyEdit.getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN))
            return false;
        storyEdit.setAuthor(story.getAuthor());
        storyEdit.setVnName(story.getVnName());
        storyEdit.setCnName(story.getCnName());
        storyEdit.setCnLink(story.getCnLink());
        storyEdit.setInfomation(story.getInfomation());
        storyEdit.setCategoryList(story.getCategoryList());
        storyEdit.setStatus(story.getStatus());
        if (story.getImages() != null && !story.getImages().isEmpty()) {
            storyEdit.setImages(story.getImages());
        }
        storyRepository.save(storyEdit);
        return true;
    }

    /**
     * Lấy List Truyện Top Đề cử Trong Khoảng
     *
     * @param page
     * @param size
     * @return Page<TopStory>
     */
    @Override
    public Page< TopStory > getTopStoryAppoind(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getTopStoryAppoind(ConstantsListUtils.LIST_STORY_DISPLAY, pageable);
    }

    @Override
    public void updateAppoindStory() {
        storyRepository.updateAppoindStory(ConstantsListUtils.LIST_STORY_DISPLAY);
    }

    @Transactional
    @Override
    public void deleteStoryById(Long id) {
        storyRepository.deleteById(id);
    }

    @Override
    public Page< StoryConverterSummary > getStoryAdmin(String search, List< Integer > storyStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (search.isEmpty()) {
            return storyRepository.findByStatusInOrderByIdDesc(storyStatus, pageable);
        } else {
            return storyRepository.findByVnNameContainingAndStatusInOrderByIdDesc(search, storyStatus, pageable);
        }
    }

    @Override
    public boolean saveEditAdminStory(Story story) {
        Optional< Story > storyOptional = storyRepository.findById(story.getId());
        Story storyEdit = storyOptional.get();
        storyEdit.setAuthor(story.getAuthor());
        storyEdit.setVnName(story.getVnName());
        storyEdit.setCnName(story.getCnName());
        storyEdit.setCnLink(story.getCnLink());
        storyEdit.setInfomation(story.getInfomation());
        storyEdit.setCategoryList(story.getCategoryList());
        storyEdit.setStatus(story.getStatus());
        storyEdit.setDealStatus(story.getDealStatus());
        if (story.getImages() != null && !story.getImages().isEmpty()) {
            storyEdit.setImages(story.getImages());
        }
        if(story.getDealStatus().equals(ConstantsUtils.STORY_VIP)){
            storyEdit.setTimeDeal(story.getTimeDeal());
            storyEdit.setPrice(story.getPrice());
        }
        storyRepository.save(storyEdit);
        return true;
    }
}
