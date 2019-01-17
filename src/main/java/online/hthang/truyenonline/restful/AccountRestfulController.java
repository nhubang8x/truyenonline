package online.hthang.truyenonline.restful;

import online.hthang.truyenonline.entity.MyUserDetails;
import online.hthang.truyenonline.entity.User;
import online.hthang.truyenonline.exception.*;
import online.hthang.truyenonline.service.CloudinaryUploadService;
import online.hthang.truyenonline.service.PayService;
import online.hthang.truyenonline.service.StoryService;
import online.hthang.truyenonline.service.UserService;
import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.WebUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Huy Thang on 23/10/2018
 * @project truyenonline
 */

@RestController
@RequestMapping(value = "/api")
public class AccountRestfulController {

    private final static Logger logger = LoggerFactory.getLogger(AccountRestfulController.class);
    private final PayService payService;
    private final UserService userService;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final StoryService storyService;

    @Autowired
    public AccountRestfulController(PayService payService, UserService userService, CloudinaryUploadService cloudinaryUploadService, StoryService storyService) {
        this.payService = payService;
        this.userService = userService;
        this.cloudinaryUploadService = cloudinaryUploadService;
        this.storyService = storyService;
    }

    @PostMapping(value = "/saveDName")
    @Transactional
    public ResponseEntity< Object > saveUserDisplayName(@RequestParam(value = "uDname") String uDname,
                                                        Principal principal)
            throws Exception {
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
        String uDName = uDname.trim();
        if (uDName == null || uDName.isEmpty() || uDName.length() > 25) {
            throw new HttpSizeException("Ngoại hiệu không được để trống hoặc dài quá 25 ký tự!");
        }
        if (uDName.equalsIgnoreCase(user.getDisplayName())) {
            throw new HttpMyException("Ngoại hiệu này bạn đang sử dụng");
        }
        if (userService.checkUserdNameExits(user.getId(), uDName)) {
            throw new HttpMyException("Ngoại hiệu đã tồn tại!");
        }
        if (user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
            userService.updateDnameOfUser(user.getId(), uDName);
            payService.savePay(null, null, user, null, (double) 0, ConstantsUtils.PAY_DNAME_TYPE);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (user.getGold() < ConstantsUtils.PRICE_UPDATE_DNAME) {
            throw new HttpUserGoldException();
        }
        userService
                .updateDnameAndGoldOfUser(user.getId(), uDName, ConstantsUtils.PRICE_UPDATE_DNAME);
        payService.savePay(null, null, user, null,
                ConstantsUtils.PRICE_UPDATE_DNAME, ConstantsUtils.PAY_DNAME_TYPE);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/saveNotification")
    public ResponseEntity< Object > saveUserNotification(@RequestParam(value = "notification") String notification,
                                                         Principal principal)
            throws Exception {
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
        if (notification.trim().length() > 255) {
            throw new HttpSizeException("Thông báo không được dài quá 255 ký tự!");
        }
        userService.updateNotificationOfUser(user.getId(), notification.trim());
        logger.info("Đã Cập Nhật");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    // Cập nhật Avatar
    @PostMapping(value = "/upload")
    @Transactional
    public ResponseEntity< Object > saveUserAvatar(@RequestParam("userfile") MultipartFile uploadfile,
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
        String fileExtension = FilenameUtils.getExtension(uploadfile.getOriginalFilename());
        assert fileExtension != null;
        if (!WebUtils.checkExtension(fileExtension)) {
            throw new HttpMyException("Chỉ upload ảnh có định dạng JPG | JPEG | PNG!");
        }
        if (uploadfile.getSize() > (20 * 1024 * 1024)) {
            throw new HttpSizeException("Kích thước ảnh upload tối đa là 20 Megabybtes!");
        }
        String url = cloudinaryUploadService.upload(uploadfile, user.getUsername() + "-" + System.nanoTime());
        userService.updateAvatarOfUser(user.getId(), url, ConstantsUtils.PRICE_AVATAR_DNAME);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }
}
