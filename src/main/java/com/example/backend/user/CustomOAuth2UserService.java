package com.example.backend.user;

import com.example.backend.user.model.User;
import com.example.backend.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        //DefaltOAuth2UserService는 빈으로 등록 안되어있어서 객체 직접 생성해야함
        private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
        //private final UserService userService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        return oAuth2User;

        // 내 웹에 회원 가입이 안 되어있으면 회원가입으로 유저 등록 처리
//        User user = (User) userService.loadUserByUsername(oAuth2User.getName());
//        if (user == null) {
//            userService.signup(UserDto.SignupRequest.builder().email(user.getEmail()).build());
//        }
//
//        return null;
    }
}
