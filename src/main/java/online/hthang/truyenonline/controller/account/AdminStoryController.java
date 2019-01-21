package online.hthang.truyenonline.controller.account;

import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.Story;
import online.hthang.truyenonline.service.CategoryService;
import online.hthang.truyenonline.service.CloudinaryUploadService;
import online.hthang.truyenonline.service.InformationService;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.utils.ConstantsListUtils;
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
@RequestMapping(value = "/tai-khoan/ad_mod/")
public class AdminStoryController {

    private final static Logger logger = LoggerFactory.getLogger(StoryAccountController.class);
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final StoryService storyService;
    private final CloudinaryUploadService cloudinaryUploadService;
    @Value("${hthang.truyenmvc.title.home}")
    private String titleHome;

    @Autowired
    public AdminStoryController(InformationService informationService,
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

        getMenuAndInfo(model, "Danh sách truyện");

        model.addAttribute("statusList", ConstantsListUtils.LIST_STORY_STATUS_SELECTED);
        return "web/account/listStoryADPage";
    }

    @GetMapping("/sua_truyen/{id}")
    public String addStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs) {

        Story story = storyService.getStoryById(id);
        if (story == null) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Truyện không tồn tại");
            return "redirect:/tai-khoan/ad_mod/quan_ly_truyen";
        }

        getMenuAndInfo(model, titleHome);

        model.addAttribute("story", story);

        model.addAttribute("statusList", ConstantsListUtils.LIST_STORY_STATUS);

        model.addAttribute("vipList" , ConstantsListUtils.LIST_STORY_STATUS_VIP);
        return "web/account/editStoryADPage";
    }

    @PostMapping("/sua_truyen/save")
    public String saveStoryEditPage(@Valid Story storyEdit, BindingResult result, Model model,
                                    HttpServletRequest request, Principal principal, RedirectAttributes redirectAttrs) {
        boolean hasError = result.hasErrors();
        if (hasError) {
            getMenuAndInfo(model, titleHome);
            model.addAttribute("story", storyEdit);
            return "web/account/editStoryADPage";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        if (!storyEdit.getEditfile().isEmpty() && storyEdit.getEditfile() != null) {
            String url = cloudinaryUploadService
                    .upload(storyEdit.getEditfile(), loginedUser.getUser().getUsername() + "-" + System.nanoTime());
            storyEdit.setImages(url);
        }
        boolean check = storyService.saveEditAdminStory(storyEdit);
        redirectAttrs.addFlashAttribute("checkEditStory", check);
        return "redirect:/tai-khoan/ad_mod/quan_ly_truyen";
    }
}

