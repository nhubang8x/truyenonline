package online.hthang.truyenonline.component;

import online.hthang.truyenonline.service.ChapterService;
import online.hthang.truyenonline.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Huy Thang on 22/11/2018
 * @project truyenonline
 */

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final ChapterService chapterService;
    private final StoryService storyService;

    @Autowired
    public ScheduledTasks(ChapterService chapterService, StoryService storyService) {
        this.chapterService = chapterService;
        this.storyService = storyService;
    }

    @Scheduled(fixedRate = 300000)
    public void updateStatusVipChapter() {
        chapterService.updateStatusChapterVip();
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void updateAppoindStoryScheduled() {
        storyService.updateAppoindStory();
    }
}
