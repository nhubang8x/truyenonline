package online.hthang.truyenonline.service.impl;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.projections.ChapterOfStory;
import online.hthang.truyenonline.projections.ChapterSummary;
import online.hthang.truyenonline.repository.ChapterRepository;
import online.hthang.truyenonline.repository.StoryRepository;
import online.hthang.truyenonline.service.ChapterService;
import online.hthang.truyenonline.utils.ConstantsListUtils;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private StoryRepository storyRepository;

    /**
     * Lấy Chapter Theo
     *
     * @param chID
     * @return Chapter
     */
    @Override
    public Chapter getChapterDisplayByID(Long chID) {
        Optional< Chapter > chapter = chapterRepository
                .findChapterByIdAndStatusIn(chID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return chapter.orElse(null);
    }

    /**
     * Lấy Chapter Theo
     *
     * @param sID
     * @param chID
     * @return Optional<Chapter>
     */
    @Override
    public Optional< Chapter > getChapterBySIDAndChID(Long sID, Long chID) {
        return chapterRepository.findChapterByIdAndStory_IdAndStatusIn(chID,
                sID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }

    /**
     * Cập nhật Chapter
     *
     * @param chapter
     * @return void
     */
    @Override
    public void updateChapter(Chapter chapter) {
        chapterRepository.save(chapter);
    }

    /**
     * Lấy Chapter ID Tiếp Theo
     *
     * @param chSerial
     * @param sID
     * @return Long
     */
    @Override
    public Long getNextChapterID(float chSerial, Long sID) {
        Optional< Long > nextID = chapterRepository.getNextChapter(chSerial, sID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return nextID.orElseGet(() -> Long.valueOf(0));
    }

    /**
     * Lấy Chapter ID Trước
     *
     * @param chSerial
     * @param sID
     * @return Long
     */
    @Override
    public Long getPreviousChapterID(float chSerial, Long sID) {
        Optional< Long > previousID = chapterRepository
                .getPreviousChapter(chSerial, sID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return previousID.orElseGet(() -> Long.valueOf(0));
    }

    /**
     * Lấy Chapter ID Chương Đầu
     *
     * @param sID
     * @param listStatus
     * @return Long
     */
    @Override
    public ChapterSummary getChapterIDHead(Long sID, List< Integer > listStatus) {
        Optional< ChapterSummary > chapterIDHead = chapterRepository.getChapterHead(sID, listStatus);
        return chapterIDHead.orElse(null);
    }

    /**
     * Lấy Chapter ID Chương mới nhất
     *
     * @param sID
     * @param listStatus
     * @return Long
     */
    @Override
    public ChapterSummary getChapterIDNew(Long sID, List< Integer > listStatus) {
        Optional< ChapterSummary > chapterIDNew = chapterRepository.getNewChapter(sID, listStatus);
        return chapterIDNew.orElse(null);
    }

    /**
     * Cập Nhật Trạng Thái Chapter Vip nếu đến dealine
     */
    @Override
    public void updateStatusChapterVip() {
        chapterRepository.updateStatusChapterVip(ConstantsUtils.CHAPTER_ACTIVED,
                ConstantsUtils.CHAPTER_VIP_ACTIVED);
    }

    /**
     * Đếm số chương đăng bởi uID
     *
     * @param uID
     * @param listStatus
     * @return Long
     */
    @Override
    public Long countChapterByUser(Long uID, List< Integer > listStatus) {
        return chapterRepository.countByUser_IdAndStatusIn(uID, listStatus);
    }

    /**
     * Lấy Chapter Theo Story
     *
     * @param sID
     * @param page
     * @param size
     * @return Page<ChapterOfStory>
     */
    @Override
    public Page< ChapterOfStory > getChapterByStory(Long sID, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return chapterRepository
                .findByStory_IdOrderBySerialDesc(sID, pageable);
    }

    /**
     * Đếm số chương đăng bởi uID
     *
     * @param sID
     * @param listStatus
     * @param page
     * @param size
     * @return Page<ChapterSummary>
     */
    @Override
    public Page< ChapterOfStory > getChapterOfStory(Long sID, List< Integer > listStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return chapterRepository
                .findByStory_IdAndStatusInOrderBySerialDesc(sID, listStatus, pageable);
    }

    /**
     * Lấy Chapter Theo Story
     *
     * @param sID
     * @param listStatus
     * @return Page<ChapterSummary>
     */
    @Override
    public List< ChapterOfStory > getAllChapterOfStory(Long sID, List< Integer > listStatus) {
        return chapterRepository
                .findByStory_IdAndStatusInOrderBySerialDesc(sID, listStatus);
    }

    @Override
    public Long countChapterByStory(Long id) {
        return chapterRepository.countChapterByStory_Id(id);
    }

    @Override
    public boolean saveNewChapter(Chapter chapter) {
        Chapter newChapter = chapterRepository.save(chapter);
        if(newChapter.getId()!=null){
            Story story = storyRepository.findById(newChapter.getStory().getId()).get();
            story.setUpdateDate(newChapter.getCreateDate());
            storyRepository.save(story);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkChapterBySerial(Long id, Float serial) {
        return chapterRepository.existsByStory_IdAndSerial(id, serial);
    }

    @Override
    public boolean saveEditChapter(Chapter chapter) {
        try {
            Chapter editChapter = chapterRepository.findChapterById(chapter.getId());
            editChapter.setSerial(chapter.getSerial());
            editChapter.setStatus(chapter.getStatus());
            editChapter.setName(chapter.getName());
            editChapter.setChapterNumber(chapter.getChapterNumber());
            editChapter.setContent(chapter.getContent());
            chapterRepository.save(editChapter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy Chapter Theo Story
     *
     * @param search
     * @param sID
     * @param page
     * @param size
     * @return Page<ChapterOfStory>
     */
    @Override
    public Page< ChapterOfStory > getChapterByStoryAndSearch(Float search, Long sID, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return chapterRepository
                .findByStory_IdAndSerialOrderBySerialDesc(sID, search, pageable);
    }

    @Override
    public boolean checkChapterBySerialAndId(Long chapterId, Long storyId, Float serial) {
        return chapterRepository.existsByIdNotAndStory_IdAndSerial(chapterId, storyId, serial);
    }

    @Override
    public Chapter getChapterByID(Long id) {
        Optional< Chapter > chapterOptional = chapterRepository.findById(id);
        return chapterOptional.orElse(null);
    }

    @Transactional
    @Override
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
}
