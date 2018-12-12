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
@Table(name = "_seditor", schema = "")
@Data
public class Seditor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "uid", column = @Column(name = "uID", nullable = false)),
            @AttributeOverride(name = "sid", column = @Column(name = "sID", nullable = false))})
    protected SeditorPK seditorPK;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createDate;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "sID", referencedColumnName = "sID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Story story;
    @JoinColumn(name = "uID", referencedColumnName = "uID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

}
