package online.hthang.truyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Huy Thang
 */

public interface NewStory {

    Long getId();

    String getVnName();

    String getImages();

    String getAuthor();

    String getInfomation();

    @Value("#{@myComponent.getBetewwen(target.updateDate)}")
    String getTimeUpdate();

    Long getChapterId();

    Integer getChapterNumber();

    String getDisplayName();

    String getUsername();

    Integer getDealStatus();

}
