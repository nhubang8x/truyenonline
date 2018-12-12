package online.hthang.truyenonline.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import online.hthang.truyenonline.utils.ConstantsUtils;
import online.hthang.truyenonline.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "comment", schema = "")
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comID", unique = true, nullable = false)
    private Long comID;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createDate;
    @Column(name = "comStatus")
    private Integer comStatus;
    @JoinColumn(name = "sID", referencedColumnName = "sID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Story story;
    @JoinColumn(name = "uID", referencedColumnName = "uID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (comStatus == null) {
            comStatus = ConstantsUtils.STATUS_ACTIVED;
        }
    }
}
