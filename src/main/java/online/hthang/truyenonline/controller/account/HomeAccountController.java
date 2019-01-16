package online.hthang.truyenonline.controller.account;

import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.exception.NotFoundException;
import online.hthang.truyenonline.service.*;
import online.hthang.truyenonline.utils.ConstantsListUtils;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Huy Thang on 23/11/2018
 * @project truyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan")
public class HomeAccountController {

    private final static Logger logger = LoggerFactory.getLogger(HomeAccountController.class);

    private final UserService userService;

    private final InformationService informationService;

    private final CategoryService categoryService;

    private final StoryService storyService;

    private final ChapterService chapterService;

    @Autowired
    public HomeAccountController(UserService userService,
                                 InformationService informationService,
                                 CategoryService categoryService,
                                 StoryService storyService,
                                 ChapterService chapterService) {
        this.userService = userService;
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.storyService = storyService;
        this.chapterService = chapterService;
    }


    private void getMenuAndInfo(Model model,
                                String title) {

        // Lấy Title Cho Page
        model.addAttribute("title", title);

        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getCategoryMenu());

        // Lấy Information của Webm
        model.addAttribute("information", informationService.getWebInfomation());
    }

    @RequestMapping
    public String defaultPage(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        //Lấy thông tin Tài Khoản đăng nhập
        Optional< User > optionalUser = userService.getUserByID(loginedUser.getUser().getId());
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        User user = optionalUser.get();
        if (user.getStatus().equals(ConstantsUtils.STATUS_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        String title = user.getDisplayName() != null ? user.getDisplayName() : user.getUsername();
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }

        model.addAttribute("user", user);

        getMenuAndInfo(model, title);

        loadStory_ChapterByUser(user.getId(), model);

        return "web/account/homePage";
    }

    // Lấy Số Chapter Và Số Truyện Đăng bởi User
    private void loadStory_ChapterByUser(Long uID, Model model) {
        model.addAttribute("countStory", storyService.
                countStoryByUser(uID, ConstantsListUtils.LIST_STORY_DISPLAY));
        model.addAttribute("countChapter", chapterService
                .countChapterByUser(uID, ConstantsListUtils.LIST_CHAPTER_DISPLAY));
    }

}
