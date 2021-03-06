package online.hthang.truyenonline.controller.web;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.exception.NotFoundException;
import online.hthang.truyenonline.service.*;
import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.DateUtils;
import online.hthang.truyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

/**
 * @author Huy Thang on 30/10/2018
 * @project truyenonline
 */

@Controller
@RequestMapping("/truyen")
public class ChapterController {

    private final InformationService informationService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    private final ChapterService chapterService;
    private final PayService payService;
    private final FavoritesService favoritesService;
    Logger logger = LoggerFactory.getLogger(ChapterController.class);

    @Autowired
    public ChapterController(InformationService informationService, CategoryService categoryService, StoryService storyService, ChapterService chapterService, PayService payService, FavoritesService favoritesService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.storyService = storyService;
        this.chapterService = chapterService;
        this.payService = payService;
        this.favoritesService = favoritesService;
    }

    private void getMenuAndInfo(Model model, String title) {

        // Lấy Title Cho Page
        model.addAttribute("title", title);

        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getCategoryMenu());

        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }

    @RequestMapping("/{sID}/chuong-{chID}")
    public String chapterPage(@PathVariable("sID") String sid,
                              @PathVariable("chID") String chid,
                              Principal principal,
                              Model model,
                              HttpServletRequest request) throws NotFoundException {

        //Kiểm Tra Type sID và chID
        //Nếu Không Chuyển Trang Lỗi
        if (sid == null || WebUtils.checkLongNumber(sid)
                || chid == null || WebUtils.checkLongNumber(chid)) {
            throw new NotFoundException();
        }
        Long sID = Long.parseLong(sid);
        Long chID = Long.parseLong(chid);

        //Kiểm Tra Truyện Có Tồn Tại Hay Không Theo sID
        if (!storyService.searchStoryByID(sID)) {
            throw new NotFoundException();
        }

        //Lấy Chapter Theo sID và chID
        Optional< Chapter > optionalChapter = chapterService.getChapterBySIDAndChID(sID, chID);
        if (!optionalChapter.isPresent()) {
            throw new NotFoundException();
        }

        Chapter chapter = optionalChapter.get();
        String title = chapter.getStory().getVnName()
                + " - Chương " + chapter.getChapterNumber()
                + ": " + chapter.getName();

        //Lấy Thời Gian Hiện Tại
        Date now = DateUtils.getCurrentDate();

        // Lấy Thời Gian 24h Trước
        Date dayAgo = DateUtils.getOneDayAgo(now);

        // Lấy thời gian 30 phút trước
        Date halfHourAgo = DateUtils.getHalfAgo(now);

        logger.info(halfHourAgo.toString());
        User user = checkUserLogin(principal);

        //Lấy LocationIP Client
        String locationIP = getLocationIP(request);

        getMenuAndInfo(model, title);

        checkVipStory(model, user, chapter, dayAgo, now);

        //Lấy Chapter ID Next Và Previous
        getPreAndNextChapter(model, chapter);

        model.addAttribute("chapter", chapter);

        //Lưu Lịch Sử Đọc Truyện
        saveFavorites(user, chapter, halfHourAgo, now, locationIP);
        if (chapter.getUser().getAvatar() == null || chapter.getUser().getAvatar().isEmpty()) {
            logger.info("Đã log avatar");
            chapter.getUser().setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }
        return "web/chapterPage";
    }

    // Kiểm Tra Người Dùng Đã Đăng Nhập Chưa
    private User checkUserLogin(Principal principal) {
        User user = null;
        if (principal != null) {

            // Lấy Người Dùng đang đăng nhập
            MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
            user = loginedUser.getUser();
        }
        return user;
    }

    private void checkVipStory(Model model,
                               User user,
                               Chapter chapter,
                               Date dayAgo,
                               Date now) {
        boolean check = true;

        //Kiểm Tra Chapter có phải tính phí hay không
        //Chapter tính phí là chapter có chStatus = 2
        if (chapter.getStatus() == 2) {

            // Kiểm tra người dùng đã đăng nhập chưa
            if (user != null) {
                boolean checkUser = user.equals(chapter.getUser());
                // Kiểm tra người dùng đã thanh toán chương vip trong 24h qua không
                // Nếu chưa thanh toán rồi thì check = false
                if (!checkUser) {
                    boolean checkPay = checkDealStory(chapter.getId(), user.getId(), dayAgo, now);
                    logger.info(String.valueOf(checkPay));
                    if (!checkPay) {
                        check = false;
                    }
                }
            } else {
                check = false;
            }
        }

        model.addAttribute("checkVip", check);
    }

    private boolean checkDealStory(Long chID,
                                   Long uID,
                                   Date dayAgo,
                                   Date now) {
        boolean check = payService
                .checkDealStoryVip(chID, uID, dayAgo, now);
        return check;
    }

    //Lấy Địa Chỉ Ip client
    private String getLocationIP(HttpServletRequest request) {
        String remoteAddr = "";

        //Kiểm Tra HttpServletRequest có null
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    private void saveFavorites(User user,
                               Chapter chapter,
                               Date halfHourAgo,
                               Date now,
                               String LocationIP) {
        // Kiểm Tra đã đọc Chapter trong Khoảng
        boolean check = favoritesService
                .checkChapterAndLocationIPInTime(chapter.getId(), LocationIP, halfHourAgo, now);
        int uView = 1;

        if (check) {
            uView = 0;
        } else {
            // Chưa Đọc Chapter Trong Khoảng 30 phút Thì Tăng Lượt View Của Chapter
            increaseView(chapter);
        }
        favoritesService.saveUfavorite(chapter, user, LocationIP, uView);
    }

    //Tăng Lượt Xem Của Chapter Và Story
    private void increaseView(Chapter chapter) {
        Chapter updateChapter = chapterService.getChapterDisplayByID(chapter.getId());
        updateChapter.setCountView(chapter.getCountView() + 1);
        chapterService.updateChapter(updateChapter);
        Story story = storyService.getStoryById(chapter.getStory().getId());
        logger.info(story.toString());
        story.setCountView(story.getCountView() + 1);
        storyService.updateViewStory(story);
    }

    private void getPreAndNextChapter(Model model,
                                      Chapter chapter) {
        model.addAttribute("preChapter", chapterService
                .getPreviousChapterID(chapter.getSerial(), chapter.getStory().getId()));

        model.addAttribute("nextChapter", chapterService.
                getNextChapterID(chapter.getSerial(), chapter.getStory().getId()));
    }
}
