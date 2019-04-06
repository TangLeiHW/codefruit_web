package ml.tanglei.codefruitweb.model.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_cf_user_password")
public class UserPasswordDO {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "encrypt_password")
    @NotNull
    private String encryptPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }
}
