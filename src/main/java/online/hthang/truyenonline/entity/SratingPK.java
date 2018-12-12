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
public class SratingPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "uID", nullable = false)
    private long uID;
    @Column(name = "sID", nullable = false)
    private long sID;

}
