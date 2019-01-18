package online.hthang.truyenonline.restful;

import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.exception.HttpMyException;
import online.hthang.truyenonline.exception.HttpNotLoginException;
import online.hthang.truyenonline.projections.StoryConverterSummary;
import online.hthang.truyenonline.service.ChapterService;
import online.hthang.truyenonline.service.PayService;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.service.UserRatingService;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Huy Thang on 17/01/2019
 * @project truyenonline
 */

@RestController
@RequestMapping(value = "/api")
public class AccountStoryRestfulController {

    private final static Logger logger = LoggerFactory.getLogger(AccountStoryRestfulController.class);
    private final StoryService storyService;
    private final ChapterService chapterService;
    private final UserRatingService userRatingService;
    private PayService payService;

    @Autowired
    public AccountStoryRestfulController(StoryService storyService, ChapterService chapterService, PayService payService, UserRatingService userRatingService) {
        this.storyService = storyService;
        this.chapterService = chapterService;
        this.payService = payService;
        this.userRatingService = userRatingService;
    }

    @PostMapping(value = "/accountStory")
    public ResponseEntity< Object > getStoryByAccount(@RequestParam("pagenumber") int pagenumber,
                                                      @RequestParam("status") int status,
                                                      Principal principal)
            throws HttpNotLoginException {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Page< StoryConverterSummary > pageStory = storyService.getStoryConverter(status,
                user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(pageStory, HttpStatus.OK);

    }

    @DeleteMapping(value = "/deleteStory/{id}")
    public ResponseEntity< ? > getStoryByAccount(@PathVariable("id") Long id, Principal principal)
            throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Story story = storyService.getStoryById(id);
        if (story == null)
            throw new HttpMyException("Không tồn tại truyện này");
        if (!story.getUser().getId().equals(user.getId()))
            throw new HttpMyException("Bạn không thể xóa truyện không phải bản thân đănh");
        Long countChapter = chapterService.countChapterByStory(story.getId());
        Long countPay = payService.countPay(id);
        Long countRating = userRatingService.getSumRaitingOfStory(id);
        if (countChapter > 0 || countPay > 0 || countRating > 0
                || story.getStatus().equals(ConstantsUtils.STORY_STATUS_COMPLETED))
            throw new HttpMyException("Không thể xóa truyện được");
        storyService.deleteStoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
