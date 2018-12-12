package online.hthang.truyenonline.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "story", schema = "")
@Data
public class Story implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sID", unique = true, nullable = false)
    private Long sID;
    @Column(name = "vnName", nullable = false)
    private String vnName;
    @Column(name = "cnName")
    private String cnName;
    @Column(name = "cnLink")
    private String cnLink;
    @Column(name = "sImages", nullable = false, length = 150)
    private String sImages;
    @Column(name = "sAuthor", nullable = false)
    private String sAuthor;
    @Column(name = "sInfo", columnDefinition = "TEXT")
    private String sInfo;
    @Column(name = "sView")
    private Integer sView;
    @Column(name = "sRating", precision = 12, scale = 0)
    private Float sRating;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "modifiedBy", length = 19)
    private Date modifiedBy;
    @Column(name = "sPrice", precision = 22, scale = 0)
    private Double sPrice;
    @Column(name = "sTimeDeal")
    private Integer sTimeDeal;
    @Column(name = "sDealStatus")
    private Integer sDealStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "sUpdate", length = 19)
    private Date sUpdate;
    @Column(name = "sStatus")
    private Integer sStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sConverter", referencedColumnName = "uID", nullable = false)
    private User sConverter;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "_scategory",
            joinColumns = {@JoinColumn(name = "sID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "cID", nullable = false)})
    private List<Category> categoryList;


}
