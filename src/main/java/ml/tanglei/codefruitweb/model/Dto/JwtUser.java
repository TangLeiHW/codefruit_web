package ml.tanglei.codefruitweb.model.Dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class JwtUser implements UserDetails {

    private Integer userId;
    private String userName;
    private String password;
    private String userPhone;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser() {
    }

    public JwtUser(UserDto user) {
        this.userId = user.getId();
        this.userName = user.getUserName();
        this.password = user.getEncryptPassword();
        this.userPhone = user.getUserPhone();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public String getUserPhone() {
        return this.userPhone;
    }

    public Integer getUserId() {
        return this.userId;
    }

    //账号是否未过期，默认false,
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号是否未锁定，默认是false,
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //账号凭证是否未过期，默认false
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //账号是否激活
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "JwtUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
