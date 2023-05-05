package com.lostAndFind.project.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private User user;

    private List<String> permission;


    public LoginUser(User user, List<String> permission) {
        this.permission = permission;
        this.user = user;
    }

    //存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permission.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}