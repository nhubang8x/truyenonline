package online.hthang.truyenonline.controller.account;

import online.hthang.truyenonline.entity.Category;
import online.hthang.truyenonline.service.CategoryService;
import online.hthang.truyenonline.service.ChapterService;
import online.hthang.truyenonline.service.InformationService;
import online.hthang.truyenonline.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * @author Huy Thang on 29/11/2018
 * @project truyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan/ad_mod/")
public class AdminCategoryController {

    private final static Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    private final ChapterService chapterService;
    @Value("${hthang.truyenmvc.title.home}")
    private String titleHome;

    @Autowired
    public AdminCategoryController(InformationService informationService, CategoryService categoryService,
                                   StoryService storyService, ChapterService chapterService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.storyService = storyService;
        this.chapterService = chapterService;
    }

    private void getMenuAndInfo(Model model, String title) {

        // Lấy Title Cho Page
        model.addAttribute("title", title);

        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getCategoryMenu());

        // Lấy Information của Webm
        model.addAttribute("information", informationService.getWebInfomation());
    }

    @RequestMapping("/list_the_loai")
    public String listChapterPage(Model model, RedirectAttributes redirectAttrs) {
        getMenuAndInfo(model, "Danh sách Thể Loại");

        return "web/account/listCategoryADPage";
    }

    @GetMapping("/them_the_loai/")
    public String addStoryPage(Model model, RedirectAttributes redirectAttrs, Principal principal) {


        getMenuAndInfo(model, "Thêm Thể Loại");

        model.addAttribute("category", new Category());
        return "web/account/addCategoryAdPage";
    }
//
//    @PostMapping("/them_chuong/{id}")
//    public String saveStoryAddPage(@PathVariable("id") Long id, @Valid Chapter chapter, BindingResult result, Model model,
//                                   Principal principal, RedirectAttributes redirectAttrs) {
//        boolean hasError = result.hasErrors();
//        if (hasError) {
//            getMenuAndInfo(model, titleHome);
//            model.addAttribute("chapter", chapter);
//            return "web/account/addChapterPage";
//        }
//        //Lấy Thông Tin người dùng đăng nhập
//        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
//        chapter.setUser(loginedUser.getUser());
//        Story story = storyService.getStoryById(id);
//        if (story.getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
//            redirectAttrs.addFlashAttribute("checkAddStoryHidden", "Truyện đã bị khóa không thể đăng thêm chương");
//        } else {
//            if (story.getDealStatus().equals(ConstantsUtils.STORY_VIP)) {
//                chapter.setStatus(ConstantsUtils.CHAPTER_VIP_ACTIVED);
//                chapter.setPrice(story.getPrice());
//                chapter.setDealine(DateUtils.getDateDeal(story.getTimeDeal()));
//            }
//            boolean check = chapterService.saveNewChapter(chapter);
//            redirectAttrs.addFlashAttribute("checkAddChapter", check);
//        }
//        return "redirect:/tai-khoan/ad_mod/list_chuong/" + chapter.getStory().getId();
//    }
//
//    @GetMapping("/sua_chuong/{id}")
//    public String editStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
//                                Principal principal) {
//
//        Chapter chapter = chapterService.getChapterByID(id);
//        if (chapter == null) {
//            redirectAttrs.addFlashAttribute("checkEditStory", "Chương không tồn tại");
//            return "redirect:/tai-khoan/ad_mod/quan_ly_truyen";
//        }
//
//        getMenuAndInfo(model, titleHome);
//
//        model.addAttribute("chapter", chapter);
//
//        model.addAttribute("listStatus", ConstantsListUtils.LIST_CHAPTER);
//        return "web/account/editChapterAdPage";
//    }
//
//    @PostMapping("/sua_chuong/{id}")
//    public String saveStoryEditPage(@PathVariable("id") Long id, @Valid Chapter chapter, BindingResult result, Model model,
//                                    Principal principal, RedirectAttributes redirectAttrs) {
//        boolean hasError = result.hasErrors();
//        if (hasError) {
//            getMenuAndInfo(model, titleHome);
//            model.addAttribute("chapter", chapter);
//            return "web/account/editChapterAdPage";
//        }
//        //Lấy Thông Tin người dùng đăng nhập
//        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
//        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_MOD");
//        Story story = storyService.getStoryById(chapter.getStory().getId());
//        if (loginedUser.getAuthorities().equals(authority)) {
//            if (story.getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
//                redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
//                return "redirect:/tai-khoan/ad_mod/quan_ly_truyen";
//            }
//            if (chapter.getStatus().equals(ConstantsUtils.CHAPTER_DENIED)) {
//                redirectAttrs.addFlashAttribute("checkEditChapter", "Bạn không có quyền sửa Chapter bị khóa!");
//                return "redirect:/tai-khoan/ad_mod/quan_ly_truyen";
//            }
//        }
//        boolean check = chapterService.saveEditChapter(chapter);
//        if (check)
//            redirectAttrs.addFlashAttribute("checkEditChapterTrue", "Sửa chương Thành Công!");
//        else
//            redirectAttrs.addFlashAttribute("checkEditChapterFalse", "Sửa chương thất bại! Có lỗi xảy ra!");
//        return "redirect:/tai-khoan/ad_mod/list_chuong/" + chapter.getStory().getId();
//    }
}
