package online.hthang.truyenonline.controller.account;

import online.hthang.truyenonline.entity.Chapter;
import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.projections.ChapterSummary;
import online.hthang.truyenonline.service.CategoryService;
import online.hthang.truyenonline.service.ChapterService;
import online.hthang.truyenonline.service.InformationService;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.utils.ConstantsListUtils;
import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Huy Thang on 29/11/2018
 * @project truyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan/")
public class ChapterAccountController {

    private final static Logger logger = LoggerFactory.getLogger(ChapterAccountController.class);
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    private final ChapterService chapterService;
    @Value("${hthang.truyenmvc.title.home}")
    private String titleHome;

    @Autowired
    public ChapterAccountController(InformationService informationService, CategoryService categoryService,
                                    StoryService storyService, ChapterService chapterService) {
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

    @RequestMapping("/list_chuong/{id}")
    public String listChapterPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
                                  Principal principal) {
        Story story = storyService.getStoryById(id);
        if (story == null) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Truyện không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        if (!story.getUser().getId().equals(loginedUser.getUser().getId())) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Bạn không có quyền quản lý truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        getMenuAndInfo(model, "Danh sách Chapter đã đăng");

        model.addAttribute("story", story);

        return "web/account/listChapterPage";
    }

    @GetMapping("/them_chuong/{id}")
    public String addStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
                               Principal principal) {

        Story story = storyService.getStoryById(id);
        if (story == null) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Truyện không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        if (!story.getUser().getId().equals(loginedUser.getUser().getId())) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Bạn không có quyền thêm Chapter thuộc Truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }

        getMenuAndInfo(model, titleHome);

        Chapter chapter = new Chapter();
        ChapterSummary newChapter = chapterService.getChapterIDNew(id, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        if (newChapter != null) {
            chapter.setSerial(newChapter.getSerial());
        } else {
            chapter.setSerial((float) 1);
        }
        chapter.setStory(story);
        model.addAttribute("chapter", chapter);

        return "web/account/addChapterPage";
    }

    @GetMapping("/them_chuong/{id}")
    public String saveStoryAddPage(@PathVariable("id") Long id,@Valid Chapter chapter, BindingResult result, Model model,
                                    Principal principal, RedirectAttributes redirectAttrs) {
        boolean hasError = result.hasErrors();
        if (hasError) {
            getMenuAndInfo(model, titleHome);
            model.addAttribute("chapter", chapter);
            return "web/account/addChapterPage";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        chapter.setUser(loginedUser.getUser());
        Story story = storyService.getStoryById(id);
        if (story.getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
            redirectAttrs.addFlashAttribute("checkAddStoryHidden", "Truyện đã bị khóa không thể đăng thêm chương");
        } else {
            if (story.getDealStatus().equals(ConstantsUtils.STORY_VIP)) {
                chapter.setStatus(ConstantsUtils.CHAPTER_VIP_ACTIVED);
                chapter.setPrice(story.getPrice());
                chapter.setDealine(DateUtils.getDateDeal(story.getTimeDeal()));
            }
            boolean check = chapterService.saveNewChapter(chapter);
            redirectAttrs.addFlashAttribute("checkAddChapter", check);
        }
        return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
    }

    @GetMapping("/sua_chuong/{id}")
    public String editStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
                               Principal principal) {

        Chapter chapter = chapterService.getChapterByID(id);
        if (chapter == null) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Chương không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }

        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        if (!chapter.getStory().getUser().getId().equals(loginedUser.getUser().getId())) {
            redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter thuộc Truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!chapter.getUser().getId().equals(loginedUser.getUser().getId())) {
            redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter không do bạn đăng!");
            return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
        }
        if (!chapter.getStory().getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
            redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        getMenuAndInfo(model, titleHome);

        model.addAttribute("chapter", chapter);

        return "web/account/addChapterPage";
    }

    @GetMapping("/sua_chuong/{id}")
    public String saveStoryEditPage(@PathVariable("id") Long id,@Valid Chapter chapter, BindingResult result, Model model,
                                    Principal principal, RedirectAttributes redirectAttrs) {
        boolean hasError = result.hasErrors();
        if (hasError) {
            getMenuAndInfo(model, titleHome);
            model.addAttribute("chapter", chapter);
            return "web/account/addChapterPage";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        Story story = storyService.getStoryById(chapter.getStory().getId());
        if(story.getStatus()equals(ConstantsUtils.STORY_STATUS_HIDDEN)){
          redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
          return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if(chapter.getStatus()equals(ConstantsUtils.CHAPTER_DENIED)){
          redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter bị khóa!");
          return "redirect:/tai-khoan/quan_ly_truyen";
        }
        boolean check = chapterService.saveEditChapter(chapter);
        if(check)
        redirectAttrs.addFlashAttribute("checkEditChapterTrue", "Sửa chương Thành Công!");
        else
        redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Sửa chương thất bại! Có lỗi xảy ra!");
        return "redirect:/tai-khoan/list_chuong/" + chapter.getStory().getId();
    }
}
