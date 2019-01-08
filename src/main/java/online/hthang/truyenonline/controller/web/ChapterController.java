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

    Logger logger = LoggerFactory.getLogger(ChapterController.class);

    private final InformationService informationService;

    private final CategoryService categoryService;

    private final StoryService storyService;

    private final ChapterService chapterService;

    private final PayService payService;

    private final UfavoritesService ufavoritesService;

    @Autowired
    public ChapterController(InformationService informationService, CategoryService categoryService, StoryService storyService, ChapterService chapterService, PayService payService, UfavoritesService ufavoritesService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.storyService = storyService;
        this.chapterService = chapterService;
        this.payService = payService;
        this.ufavoritesService = ufavoritesService;
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
        if (sid == null || !WebUtils.checkLongNumber(sid)
                || chid == null || !WebUtils.checkLongNumber(chid)) {
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
                + " - Chương " + chapter.getChNumber()
                + ": " + chapter.getChName();

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
        if (chapter.getUser().getUAvatar() == null || chapter.getUser().getUAvatar().isEmpty()) {
            logger.info("Đã log avatar");
            chapter.getUser().setUAvatar(ConstantsUtils.AVATAR_DEFAULT);
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
        if (chapter.getChStatus() == 2) {

            // Kiểm tra người dùng đã đăng nhập chưa
            if (user != null) {
                boolean checkUser = user.equals(chapter.getUser());
                // Kiểm tra người dùng đã thanh toán chương vip trong 24h qua không
                // Nếu chưa thanh toán rồi thì check = false
                if (!checkUser) {
                    boolean checkPay = checkDealStory(chapter.getChID(), user.getUID(), dayAgo, now);
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
        logger.info("Hôm Trước:" + dayAgo);
        logger.info("Hiện Tại:" + now);
        boolean check = payService
                .checkDealStoryVip(chID, uID, dayAgo, now);
        logger.info(String.valueOf(check));
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
        boolean check = ufavoritesService
                .checkChapterAndLocationIPInTime(chapter.getChID(), LocationIP, halfHourAgo, now);
        Integer ufView = 1;

        if (check) {
            ufView = 0;
        } else {
            // Chưa Đọc Chapter Trong Khoảng 30 phút Thì Tăng Lượt View Của Chapter
            increaseView(chapter);
        }
        ufavoritesService.saveUfavorite(chapter, user, LocationIP, ufView);
    }

    //Tăng Lượt Xem Của Chapter Và Story
    private void increaseView(Chapter chapter) {
        chapter.setChView(chapter.getChView() + 1);
        chapterService.updateChapter(chapter);
        Story story = chapter.getStory();
        story.setSView(story.getSView() + 1);
        storyService.updateViewStory(story);
    }

    private void getPreAndNextChapter(Model model,
                                      Chapter chapter) {
        model.addAttribute("preChapter", chapterService
                .getPreviousChapterID(chapter.getChSerial(), chapter.getStory().getSID()));

        model.addAttribute("nextChapter", chapterService.
                getNextChapterID(chapter.getChSerial(), chapter.getStory().getSID()));
    }
}
