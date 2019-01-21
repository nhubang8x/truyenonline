package online.hthang.truyenonline.restful;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.exception.HttpMyException;
import online.hthang.truyenonline.exception.HttpNotLoginException;
import online.hthang.truyenonline.projections.ChapterOfStory;
import online.hthang.truyenonline.service.*;
import online.hthang.truyenonline.utils.AesUtil;
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
 * @author Huy Thang on 30/11/2018
 * @project truyenonline
 */

@RestController
@RequestMapping(value = "/api")
public class AccountChapterRestfulController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountChapterRestfulController.class);
    private final StoryService storyService;
    private final ChapterService chapterService;
    private final FavoritesService favoritesService;


    @Autowired
    public AccountChapterRestfulController(StoryService storyService, ChapterService chapterService, FavoritesService favoritesService) {
        this.storyService = storyService;
        this.chapterService = chapterService;
        this.favoritesService = favoritesService;
    }


    @PostMapping(value = "/chapterByStory")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("storyId") Long storyId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("search") String searchEncrypt,
                                                  Principal principal) throws HttpNotLoginException {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        Page< ChapterOfStory > chapterOfStoryPage = null;
        String decryptedText = new String(java.util.Base64.getDecoder().decode(searchEncrypt));
        AesUtil aesUtil = new AesUtil(128, 1000);
        if (decryptedText.split("::").length == 3) {
            String searchText = aesUtil.decrypt(decryptedText.split("::")[1], decryptedText.split("::")[0], "1234567891234567", decryptedText.split("::")[2]);
            if (searchText.trim().isEmpty()) {
                chapterOfStoryPage = chapterService
                        .getChapterByStory(storyId, pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
            } else {
                chapterOfStoryPage = chapterService
                        .getChapterByStoryAndSearch(Float.parseFloat(searchText), storyId,
                                pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
            }
        }
        return new ResponseEntity<>(chapterOfStoryPage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteChapter/{id}")
    public ResponseEntity< ? > getStoryByAccount(@PathVariable("id") Long id, Principal principal)
            throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Chapter chapter = chapterService.getChapterByID(id);
        if (chapter == null)
            throw new HttpMyException("Không tồn tại chương này này");
        if (!chapter.getUser().getId().equals(user.getId()))
            throw new HttpMyException("Bạn không thể xóa Chương không phải bản thân đăng");
        if (favoritesService.checkChapterId(chapter.getId()) || chapter.getStatus().equals(ConstantsUtils.CHAPTER_VIP_ACTIVED))
            throw new HttpMyException("Không thể xóa truyện được");
        chapterService.deleteChapter(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
