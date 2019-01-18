package online.hthang.truyenonline.restful;

import online.hthang.truyenonline.projections.NewStory;
import online.hthang.truyenonline.projections.TopConverter;
import online.hthang.truyenonline.projections.TopStory;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.service.UserService;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Huy Thang on 25/12/2018
 * @project truyenonline
 */

@RestController
@RequestMapping(value = "/api/home")
public class HomeRestfulController {

    private final StoryService storyService;

    private final UserService userService;

    @Autowired
    public HomeRestfulController(StoryService storyService, UserService userService) {
        this.storyService = storyService;
        this.userService = userService;
    }

    //Lấy Top 3 Truyện Mới Của Converter
    @PostMapping(value = "/topAppoidMonth")
    public ResponseEntity< ? > loadStoryTopViewMonth() {

        // Lấy Danh Sách Truyện Top View trong tháng
        List< TopStory > topstory = storyService
                .getTopStoryAppoind(ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE)
                .getContent();
        return new ResponseEntity<>(topstory, HttpStatus.OK);
    }

    //Lấy Top 3 Truyện Mới Của Converter
    @PostMapping(value = "/storyNewUpdate")
    public ResponseEntity< ? > loadStoryNewUpdate() {
        // Lấy Danh Sách Truyện Mới Cập Nhật
        List< NewStory > listNewStory = storyService
                .getStoryNew(ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_HOME)
                .getContent();
        return new ResponseEntity<>(listNewStory, HttpStatus.OK);
    }

    @PostMapping(value = "/storyVipNew")
    public ResponseEntity< ? > loadStoryVipNew() {

        // Lấy Danh Sách Truyện Vip Top trong tuần
        List< NewStory > topvipstory = storyService
                .getVipStoryNew(ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE)
                .getContent();
        return new ResponseEntity<>(topvipstory, HttpStatus.OK);
    }

    @PostMapping(value = "/topConveter")
    public ResponseEntity< ? > loadStoryOfConverter() {
        // Lấy Danh Sách Top Converter
        List< TopConverter > topConverters = userService
                .getTopConverter(ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE);

        return new ResponseEntity<>(topConverters, HttpStatus.OK);
    }
}
