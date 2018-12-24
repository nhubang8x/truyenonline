package online.hthang.truyenonline.component;

import online.hthang.truyenonline.projections.ChapterSummary;
import online.hthang.truyenonline.service.ChapterService;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.service.UserService;
import online.hthang.truyenonline.utils.ConstantsListUtils;
import online.hthang.truyenonline.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Huy Thang on 26/10/2018
 * @project truyenonline
 */

@Component
public class MyComponent {

    private final ChapterService chapterService;

    private final StoryService storyService;

    @Autowired
    public MyComponent(ChapterService chapterService, StoryService storyService) {
        this.chapterService = chapterService;
        this.storyService = storyService;
    }

    public String getBetewwen(Date date) {
        return DateUtils.betweenTwoDays(date);
    }

    //Lấy Chapter Đầu Tiên
    public ChapterSummary getChapterHead(Long sID) {
        return chapterService
                .getChapterIDHead(sID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }

    //Lấy Chapter Mới Nhất
    public ChapterSummary getNewChapter(Long sID) {
        return chapterService
                .getChapterIDNew(sID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }

    public Long countStoryOfUser(Long uID){
        return  storyService.
                countStoryByUser(uID, ConstantsListUtils.LIST_STORY_DISPLAY);
    }

    public Long countChapterOfUser(Long uID){
        return  chapterService.
                countChapterByUser(uID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }
}
