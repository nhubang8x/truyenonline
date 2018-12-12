package online.hthang.truyenonline.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "_srating", schema = "")
@Data
public class Srating implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "uid", column = @Column(name = "uID", nullable = false)),
            @AttributeOverride(name = "sid", column = @Column(name = "sID", nullable = false))})
    private SratingPK sratingPK;
    @Column(name = "rating", nullable = false)
    private Integer rating;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;
    @JoinColumn(name = "sID", referencedColumnName = "sID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Story story;
    @JoinColumn(name = "uID", referencedColumnName = "uID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
    @Column(name = "locationIP", length = 50, nullable = false)
    private String locationIP;

}
