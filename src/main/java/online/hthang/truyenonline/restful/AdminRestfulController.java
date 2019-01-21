package online.hthang.truyenonline.restful;

import online.hthang.truyenonline.entity.*;
import online.hthang.truyenonline.exception.HttpMyException;
import online.hthang.truyenonline.exception.HttpNotLoginException;
import online.hthang.truyenonline.exception.HttpUserLockedException;
import online.hthang.truyenonline.projections.StoryConverterSummary;
import online.hthang.truyenonline.service.*;
import online.hthang.truyenonline.utils.AesUtil;
import online.hthang.truyenonline.utils.ConstantsListUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang on 30/11/2018
 * @project truyenonline
 */

@RestController
@RequestMapping(value = "/api")
public class AdminRestfulController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdminRestfulController.class);
    private final StoryService storyService;
    private final ChapterService chapterService;
    private final FavoritesService favoritesService;
    private final UserRatingService userRatingService;
    private final PayService payService;
    private final UserService userService;
    @Autowired
    private CategoryService categoryService;

    @Autowired

    public AdminRestfulController(StoryService storyService, ChapterService chapterService, FavoritesService favoritesService, UserRatingService userRatingService, PayService payService, UserService userService) {
        this.storyService = storyService;
        this.chapterService = chapterService;
        this.favoritesService = favoritesService;
        this.userRatingService = userRatingService;
        this.payService = payService;
        this.userService = userService;
    }


    @PostMapping(value = "/storyByAdmin")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("statusId") Integer statusId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("search") String searchEncrypt,
                                                  Principal principal) throws HttpNotLoginException {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        Page< StoryConverterSummary > storyConverterSummaries = null;
        String decryptedText = new String(java.util.Base64.getDecoder().decode(searchEncrypt));
        AesUtil aesUtil = new AesUtil(128, 1000);
        List< Integer > statusList;
        if (decryptedText.split("::").length == 3) {
            String searchText = aesUtil.decrypt(decryptedText.split("::")[1], decryptedText.split("::")[0], "1234567891234567", decryptedText.split("::")[2]);
            if (statusId == -1) {
                statusList = ConstantsListUtils.LIST_STORY_ALL;
            } else {
                statusList = new ArrayList<>();
                statusList.add(statusId);
            }
            storyConverterSummaries = storyService
                    .getStoryAdmin(searchText.trim(), statusList, pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
        }
        return new ResponseEntity<>(storyConverterSummaries, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteAdModStory/{id}")
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
        Long countChapter = chapterService.countChapterByStory(story.getId());
        Long countPay = payService.countPay(id);
        Long countRating = userRatingService.getSumRaitingOfStory(id);
        if (countChapter > 0 || countPay > 0 || countRating > 0
                || story.getStatus().equals(ConstantsUtils.STORY_STATUS_COMPLETED))
            throw new HttpMyException("Không thể xóa truyện được");
        storyService.deleteStoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping(value = "/deleteAdChapter/{id}")
    public ResponseEntity< ? > deleteChapter(@PathVariable("id") Long id, Principal principal)
            throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Chapter chapter = chapterService.getChapterByID(id);
        if (chapter == null)
            throw new HttpMyException("Không tồn tại chương này này");
        if (favoritesService.checkChapterId(chapter.getId()) || chapter.getStatus().equals(ConstantsUtils.CHAPTER_VIP_ACTIVED))
            throw new HttpMyException("Không thể xóa truyện được");
        chapterService.deleteChapter(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(value = "/payOfUser")
    public ResponseEntity< ? > searchPayUser(@RequestParam("pagenumber") Integer pagenumber,
                                             Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Optional< User > optionalUser = userService.getUserByID(user.getId());
        if (!optionalUser.isPresent()) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        user = optionalUser.get();
        if (user.getStatus().equals(ConstantsUtils.STATUS_DENIED)) {
            throw new HttpUserLockedException();
        }
        Page< Pay > payPage = payService.getPagePayByUser(user, ConstantsListUtils.LIST_PAY_TYPE,
                pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(payPage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteAdCategory/{id}")
    public ResponseEntity< ? > deleteCategory(@PathVariable("id") Integer id, Principal principal)
            throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        boolean check = categoryService.deleteCategory(id);
        if (check)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            throw new HttpMyException("Không thể xóa Thể Loại được");

    }
}
