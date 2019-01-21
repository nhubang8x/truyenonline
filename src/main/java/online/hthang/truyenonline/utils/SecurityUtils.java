package online.hthang.truyenonline.utils;

/**
 * @author Huy Thang on 09/10/2018
 * @project truyenmvc
 */
public class SecurityUtils {

    public static final String[] PERMIT_ALL_LINK = {
            "/",
            "/dang-nhap",
            "/logout",
            "/api/load",
            "/api/load1",
            "/api/load2",
            "/api/load3",
            "api/accountStoryMy",
            "/danh-muc/**"};

    public static final String[] ROLE_USER_LINK = {
            "/tai-khoan/",
            "/tai-khoan/mat-khau",
            "/tai-khoan/nap-dau",
            "/tai-khoan/quan_ly_truyen",
            "/tai-khoan/list_chuong/**",
            "/tai-khoan/log_xu"
    };

    public static final String[] ROLE_AD_CV_LINK = {
            "/tai-khoan/them_chuong/**",
            "/tai-khoan/sua_truyen/**",
            "/tai-khoan/nap-dau",
            "/tai-khoan/them_truyen/**"
    };

    public static final String[] ROLE_AD_MOD_LINK = {
            "/tai-khoan/ad_mod/**"
    };

    public static final String[] ROLE_ADMIN_LINK = {"/admin"};

}
