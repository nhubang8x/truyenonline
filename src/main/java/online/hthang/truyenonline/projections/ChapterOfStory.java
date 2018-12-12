package online.hthang.truyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author Huy Thang on 04/12/2018
 * @project truyenonline
 */
public interface ChapterOfStory {

    Long getChID();

    Integer getChNumber();

    String getChName();

    @Value("#{target.story.sID}")
    Long getsID();

    @Value("#{target.user.uName}")
    String getUName();

    @Value("#{target.user.uDname}")
    String getUDname();

    Date getCreateDate();

    @Value("#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();

    Integer getChStatus();

}
