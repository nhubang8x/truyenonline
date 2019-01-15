package online.hthang.truyenonline.projections;

/**
 * @author Huy Thang
 * @project truyenonline
 *
 */
public interface TopStory {

    Long getId();

    String getVnName();

    String getImages();

    String getAuthor();

    Long getCnt();

    Long getCategoryId();

    String getCategoryName();

    String getInfomation();

    Integer getDealStatus();
}
