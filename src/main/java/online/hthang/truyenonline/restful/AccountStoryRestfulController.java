package online.hthang.truyenonline.restful;

import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.exception.HttpNotLoginException;
import online.hthang.truyenonline.projections.MemberStorySummary;
import online.hthang.truyenonline.projections.StoryConverterSummary;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.utils.ConstantsListUtils;
import online.hthang.truyenonline.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Huy Thang on 17/01/2019
 * @project truyenonline
 */

@RestController
@RequestMapping(value = "/api")
public class AccountStoryRestfulController {

    private final static Logger logger = LoggerFactory.getLogger(AccountStoryRestfulController.class);
    private final StoryService storyService;

    @Autowired
    public AccountStoryRestfulController(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping(value = "/accountStory")
    public ResponseEntity< Object > getStoryByAccount(@RequestParam("pagenumber") int pagenumber,
                                                      @RequestParam("status") int status,
                                                      Principal principal)
            throws HttpNotLoginException {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Page< StoryConverterSummary > pageStory = storyService.getStoryConverter(status,
                user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(pageStory, HttpStatus.OK);

    }
}
