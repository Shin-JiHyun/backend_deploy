package com.example.backend.user;

import com.example.backend.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private User user;

    public CustomOAuth2User(User user) {
        this.user = user;
    }


    //소셜로그인할때 받아오는 정보들 회사를 인증해야 email을 받아올수 있으므로 일단 nickname
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("nickname", user.getEmail());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }
}
