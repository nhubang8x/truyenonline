package online.hthang.truyenonline.component;

import online.hthang.truyenonline.projections.ChapterSummary;
import online.hthang.truyenonline.service.ChapterService;
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

    @Autowired
    public MyComponent(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    public String getBetewwen(Date date) {
        return DateUtils.betweenTwoDays(date);
    }

    public ChapterSummary getChapterHead(Long sID) {
        return chapterService
                .getChapterIDHead(sID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }

    public ChapterSummary getNewChapter(Long sID) {
        return chapterService
                .getChapterIDNew(sID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }
}
