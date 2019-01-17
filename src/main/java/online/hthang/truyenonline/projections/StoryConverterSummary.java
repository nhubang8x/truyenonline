package online.hthang.truyenonline.projections;

import java.util.Date;

/**
 * @author Huy Thang on 20/11/2018
 * @project truyenonline
 */
public interface StoryConverterSummary {

    Long getId();

    String getVnName();

    String getCnLink();

    Long getCountView();

    Integer getDealStatus();

    Date getUpdateDate();
}
