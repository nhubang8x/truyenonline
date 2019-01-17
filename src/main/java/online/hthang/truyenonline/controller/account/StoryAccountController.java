package online.hthang.truyenonline.controller.account;

import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.service.CategoryService;
import online.hthang.truyenonline.service.CloudinaryUploadService;
import online.hthang.truyenonline.service.InformationService;
import online.hthang.truyenonline.service.StoryService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Huy Thang on 29/11/2018
 * @project truyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan/")
public class StoryAccountController {

    private final static Logger logger = LoggerFactory.getLogger(StoryAccountController.class);
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    private final CloudinaryUploadService cloudinaryUploadService;
    @Value("${hthang.truyenmvc.title.home}")
    private String titleHome;

    @Autowired
    public StoryAccountController(InformationService informationService,
                                  CategoryService categoryService, StoryService storyService, CloudinaryUploadService cloudinaryUploadService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.storyService = storyService;
        this.cloudinaryUploadService = cloudinaryUploadService;
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

    @RequestMapping("/quan_ly_truyen")
    public String listStoryPage(Model model, Principal principal) {
        // Lấy Danh sách truyện đang đọc của người dùng
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();

        model.addAttribute("userId", loginedUser.getUser().getId());

        getMenuAndInfo(model, "Danh sách truyện đã đăng");

        return "web/account/listStoryPage";
    }

    @GetMapping("/them_truyen")
    public String addStoryPage(Model model) {

        getMenuAndInfo(model, titleHome);

        model.addAttribute("story", new Story());

        return "web/account/addStoryPage";
    }

    @PostMapping("/them_truyen")
    public String saveStoryPage(@Valid Story story, BindingResult result, Model model,
                                HttpServletRequest request, Principal principal, RedirectAttributes redirectAttrs) {
        boolean hasError = result.hasErrors();
        if (hasError) {
            getMenuAndInfo(model, titleHome);
            model.addAttribute("story", story);
            return "web/account/addStoryPage";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();

        story.setUser(loginedUser.getUser());
        String url = cloudinaryUploadService
                .upload(story.getUploadfile(), loginedUser.getUser().getUsername() + "-" + System.nanoTime());
        story.setImages(url);
        boolean check = storyService.saveNewStory(story);
        if (check) {
            redirectAttrs.addFlashAttribute("checkAddStory", "Đăng truyện mới thành công!");
        } else {
            redirectAttrs.addFlashAttribute("checkAddStory", "Có lỗi xảy ra, Đăng truyện thất bại!");
        }
        return "redirect:/account/quan_ly_truyen";
    }

    @GetMapping("/sua_truyen/{id}")
    public String addStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
                               Principal principal) {

        Story story = storyService.getStoryById(id);
        if (story == null) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Truyện không tồn tại");
            return "redirect:/account/quan_ly_truyen";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        if (!story.getUser().getId().equals(loginedUser.getUser().getId())) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Bạn không có quyền sửa truyện không do bạn đăng!");
            return "redirect:/account/quan_ly_truyen";
        }

        getMenuAndInfo(model, titleHome);

        model.addAttribute("story", story);

        return "web/account/editStoryPage";
    }

    @PostMapping("/sua_truyen/{id}")
    public String saveStoryEditPage(@Valid Story story, BindingResult result, Model model,
                                    HttpServletRequest request, Principal principal, RedirectAttributes redirectAttrs) {
        boolean hasError = result.hasErrors();
        if (hasError) {
            getMenuAndInfo(model, titleHome);
            model.addAttribute("story", story);
            return "web/account/addStoryPage";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();

        story.setUser(loginedUser.getUser());
        String url = cloudinaryUploadService
                .upload(story.getUploadfile(), loginedUser.getUser().getUsername() + "-" + System.nanoTime());
        story.setImages(url);
        boolean check = storyService.saveNewStory(story);
        redirectAttrs.addFlashAttribute("checkAddStory", check);
        return "redirect:/account/quan_ly_truyen";
    }
}
