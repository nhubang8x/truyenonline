package online.hthang.truyenonline.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author Huy Thang
 */
@Embeddable
@Data
public class UserRatingPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "userId", nullable = false)
    private long userId;
    @Column(name = "storyId", nullable = false)
    private long storyId;

}
