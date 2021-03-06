package online.hthang.truyenonline.restful;

import online.hthang.truyenonline.entity.*;
import online.hthang.truyenonline.exception.*;
import online.hthang.truyenonline.projections.ChapterOfStory;
import online.hthang.truyenonline.projections.CommentSummary;
import online.hthang.truyenonline.projections.ConveterSummary;
import online.hthang.truyenonline.projections.SearchStory;
import online.hthang.truyenonline.service.*;
import online.hthang.truyenonline.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang on 30/11/2018
 * @project truyenonline
 */

@RestController
@RequestMapping(value = "/api")
public class WebRestfulController {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebRestfulController.class);
    private final StoryService storyService;
    private final PayService payService;
    private final ChapterService chapterService;
    private final UserService userService;
    private final UserRatingService userRatingService;
    private final CommentService commentService;

    @Autowired
    public WebRestfulController(StoryService storyService, PayService payService, ChapterService chapterService, UserService userService, UserRatingService userRatingService, CommentService commentService) {
        this.storyService = storyService;
        this.payService = payService;
        this.chapterService = chapterService;
        this.userService = userService;
        this.userRatingService = userRatingService;
        this.commentService = commentService;
    }

    @PostMapping(value = "/chapterVip")
    @Transactional
    public ResponseEntity< ? > getuserlogin(@RequestParam(value = "chID") String chID, Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Optional< User > optionalUser = userService.getUserByID(user.getId());
        if (!optionalUser.isPresent()) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        user = optionalUser.get();
        if (user.getStatus().equals(ConstantsUtils.STATUS_DENIED)) {
            throw new HttpUserLockedException();
        }
        if (chID == null || WebUtils.checkLongNumber(chID)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Chapter chapter = chapterService.getChapterDisplayByID(Long.valueOf(chID));
        if (chapter == null) {
            throw new HttpMyException("Không tồn tại chương truyện này!");
        }
        if (chapter.getStatus() == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //Lấy Thời Gian Hiện Tại
        Date now = DateUtils.getCurrentDate();

        // Lấy Thời Gian 24h Trước
        Date dayAgo = DateUtils.getOneDayAgo(now);
        if (payService.checkDealStoryVip(Long.valueOf(chID), user.getId(), dayAgo, now)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (user.getGold() < chapter.getPrice()) {
            throw new HttpUserGoldException();
        }
        boolean payCheck = payService
                .savePay(null, chapter, user, chapter.getUser(), chapter.getPrice(), ConstantsUtils.PAY_CHAPTER_VIP_TYPE);
        if (payCheck) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new HttpMyException("Có lỗi xảy ra. Vui lòng thử lại sau");
        }
    }

    @PostMapping(value = "/rating")
    @Transactional
    public ResponseEntity< Object > saveUserAvatar(@RequestParam("idBox") Long idBox,
                                                   @RequestParam("rate") Integer rate,
                                                   HttpServletRequest request,
                                                   Principal principal) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setError(false);
        exceptionResponse.setServer("");
        if (principal == null) {
            exceptionResponse.setMessage("Bạn chưa đăng nhập!");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Optional< User > optionalUser = userService.getUserByID(user.getId());
        if (!optionalUser.isPresent()) {
            exceptionResponse.setMessage("Tài khoản không tồn tại!");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        user = optionalUser.get();
        if (user.getStatus().equals(ConstantsUtils.STATUS_DENIED)) {
            exceptionResponse.setMessage("Tài khoản của bạn đã bị khóa. Mời liên hệ admin!");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        if (userRatingService.checkRatingWithUser(idBox, user.getId())) {
            exceptionResponse.setMessage("Bạn đã đánh giá truyện này rồi");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        String locationIP = getLocationIP(request);
        Date now = DateUtils.getCurrentDate();
        Optional< UserRating > optionalSrating = userRatingService.checkRatingWithLocationIP(idBox, locationIP, DateUtils.getHalfAgo(now), now);
        if (optionalSrating.isPresent()) {
            exceptionResponse.setMessage("Đã có đánh giá truyện tại địa chỉ IP này. Hãy đợi " + DateUtils.betweenHours(optionalSrating.get().getCreateDate()) + " để tiếp tục đánh giá");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        Float result = userRatingService.saveRating(user.getId(), idBox, locationIP, rate);
        //Lưu đánh giá
        if (result != -1) {
            exceptionResponse.setMyrating(userRatingService.getSumRaitingOfStory(idBox));
            DecimalFormat df = new DecimalFormat("#.0");
            exceptionResponse.setMyrate(df.format(result));
        }
        return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/search")
    public ResponseEntity< ? > searchStory(@RequestParam("txtSearch") String txtSearch) {
        String decryptedText = new String(java.util.Base64.getDecoder().decode(txtSearch));
        AesUtil aesUtil = new AesUtil(128, 1000);
        if (decryptedText.split("::").length == 3) {
            String searchText = aesUtil.decrypt(decryptedText.split("::")[1], decryptedText.split("::")[0], "1234567891234567", decryptedText.split("::")[2]);
            return new ResponseEntity<>(storyService.getSearch(searchText), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @PostMapping(value = "/chapterOfStory")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("sID") Long sID,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("size") Integer size) {
        Page< ChapterOfStory > chapterOfStoryPage;
        if (size == 1) {
            chapterOfStoryPage = chapterService
                    .getChapterOfStory(sID,
                            ConstantsListUtils.LIST_CHAPTER_DISPLAY,
                            pagenumber,
                            ConstantsUtils.PAGE_SIZE_CHAPTER_OF_STORY);
        } else {
            List< ChapterOfStory > chapterOfStories = chapterService
                    .getAllChapterOfStory(sID,
                            ConstantsListUtils.LIST_CHAPTER_DISPLAY);
            chapterOfStoryPage = new PageImpl<>(chapterOfStories);
        }
        return new ResponseEntity<>(chapterOfStoryPage, HttpStatus.OK);
    }

    @PostMapping(value = "/commentOfStory")
    public ResponseEntity< ? > loadCommentOfStory(@RequestParam("sID") Long sID,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("size") Integer size) {
        Page< CommentSummary > commentSummaryPage;
        if (size == 1) {
            commentSummaryPage = commentService
                    .getCommentOfStoryByPage(sID,
                            pagenumber,
                            ConstantsUtils.PAGE_SIZE_COMMENT_OF_STORY);
        } else {
            List< CommentSummary > commentSummaryList = commentService.getAllCommentOfStory(sID);
            commentSummaryPage = new PageImpl<>(commentSummaryList);
        }
        return new ResponseEntity<>(commentSummaryPage, HttpStatus.OK);
    }


    @PostMapping(value = "/add/commentOfStory")
    public ResponseEntity< ? > newComment(@RequestParam("sID") Long sID,
                                          @RequestParam("commentText") String commentTextEncode,
                                          Principal principal) throws Exception {
        String decryptedText = new String(java.util.Base64.getDecoder().decode(commentTextEncode));
        AesUtil aesUtil = new AesUtil(128, 1000);
        if (decryptedText.split("::").length == 3) {
            String commentText = aesUtil.decrypt(decryptedText.split("::")[1],
                    decryptedText.split("::")[0],
                    "1234567891234567",
                    decryptedText.split("::")[2]);
            if (commentText.trim().length() == 0) {
                throw new HttpMyException("Bình luận không được để trống");
            }
            if (principal == null) {
                throw new HttpNotLoginException();
            }
            MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
            User user = myUser.getUser();
            Optional< User > optionalUser = userService.getUserByID(user.getId());
            if (!optionalUser.isPresent()) {
                throw new HttpNotLoginException("Tài khoản không tồn tại");
            }
            user = optionalUser.get();
            if (user.getStatus().equals(ConstantsUtils.STATUS_DENIED)) {
                throw new HttpUserLockedException();
            }
            Optional< Story > storyOptional = storyService.searchStoryBySID(sID);
            if (!storyOptional.isPresent()) {
                throw new HttpMyException("Truyện không tồn tại!");
            }
            Story story = storyOptional.get();
            if (story.getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
                throw new HttpMyException("Truyện đã bị xóa hoặc không tìm thấy!");
            }
            boolean check = commentService.saveComment(user, story, commentText);
            if (check) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new HttpMyException("Có lỗi xảy ra. Mong bạn quay lại sau");
            }
        } else {
            throw new HttpMyException("Có lỗi xảy ra. Mong bạn quay lại sau");
        }
    }

    //Lấy Thông Tin Converter
    @PostMapping(value = "/converterInfo")
    public ResponseEntity< ? > loadConverter(@RequestParam("uID") Long uID) {
        ConveterSummary conveterSummary = userService.getConverterByID(uID);
        return new ResponseEntity<>(conveterSummary, HttpStatus.OK);
    }

    //Lấy Top 3 Truyện đã đăng của Converter
    @PostMapping(value = "/storyOfConverter")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam("uID") Long uID) {
        List< SearchStory > listNewStory = storyService
                .getTop3StoryOfConverter(uID, ConstantsListUtils.LIST_STORY_DISPLAY);
        return new ResponseEntity<>(listNewStory, HttpStatus.OK);
    }


    @PostMapping(value = "/appoint")
    public ResponseEntity< ? > appoindStory(@RequestParam("sID") Long sID,
                                            @RequestParam("coupon") Integer coupon,
                                            Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Optional< User > optionalUser = userService.getUserByID(user.getId());
        if (!optionalUser.isPresent()) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        user = optionalUser.get();
        if (user.getStatus().equals(ConstantsUtils.STATUS_DENIED)) {
            throw new HttpUserLockedException();
        }
        Optional< Story > storyOptional = storyService.searchStoryBySID(sID);
        if (!storyOptional.isPresent()) {
            throw new HttpMyException("Truyện không tồn tại!");
        }
        Story story = storyOptional.get();
        if (story.getStatus().equals(ConstantsUtils.STORY_STATUS_HIDDEN)) {
            throw new HttpMyException("Truyện đã bị xóa hoặc không tìm thấy!");
        }
        if (coupon <= 0) {
            throw new HttpMyException("Số phiếu đề cử ít nhất là 1!");
        }
        if (user.getGold() < (coupon * 1000))
            throw new HttpMyException("Số dư của bạn không đủ để đề cử");
        boolean check = payService.savePay(story, null, user, null, (double) (coupon * 1000), ConstantsUtils.PAY_STORY_APPOINT_TYPE);
        if (check)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            throw new HttpMyException("Có lỗi xảy ra mong bạn quay lại sau!");
    }



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

}
