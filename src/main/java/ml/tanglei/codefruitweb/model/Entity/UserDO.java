package ml.tanglei.codefruitweb.model.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_cf_user")
public class UserDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_phone")
    private String userPhone;
    @Column(name = "user_avatar")
    private String userAvatar;
    @Column(name = "user_role")
    private Integer userRole;
    @Column(name = "sex")
    private String sex;
    @Column(name = "be_concerned_count")
    private Long beConcernedCount;
    @Column(name = "start_count")
    private Integer startCount;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "introduction")
    private String introduction;
    @Column(name = "local_address")
    private String localAddress;
    @Column(name = "opend_id")
    private String opendId;
    @Column(name = "opend_type")
    private String opendType;
    @Column(name = "register_time")
    private Date registerTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getBeConcernedCount() {
        return beConcernedCount;
    }

    public void setBeConcernedCount(Long beConcernedCount) {
        this.beConcernedCount = beConcernedCount;
    }

    public Integer getStartCount() {
        return startCount;
    }

    public void setStartCount(Integer startCount) {
        this.startCount = startCount;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getOpendId() {
        return opendId;
    }

    public void setOpendId(String opendId) {
        this.opendId = opendId;
    }

    public String getOpendType() {
        return opendType;
    }

    public void setOpendType(String opendType) {
        this.opendType = opendType;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
