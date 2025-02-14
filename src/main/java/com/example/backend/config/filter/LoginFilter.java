package com.example.backend.config.filter;


import com.example.backend.user.model.User;
import com.example.backend.user.model.UserDto;
import com.example.backend.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // 원래는 form-data 형식으로 사용자 정보를 입력받았는데
    // 우리는 JSON 형태로 입력을 받기 위해서 재정의
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginFilter 실행됐다.");
        UsernamePasswordAuthenticationToken authToken;
        // 그림에서 1번 로직
//        UserDto.SignupRequest UserDto =
//                new UserDto.SignupRequest(request.getParameter("Username"), request.getParameter("password"));
        try {
            // 그림에서 원래 1번이었던 로직을 JSON 형태의 데이터를 처리하도록 변경
            UserDto.SignupRequest UserDto  = new ObjectMapper().readValue(request.getInputStream(), UserDto.SignupRequest.class);

            // 그림에서 2번 로직
            authToken =
                    new UsernamePasswordAuthenticationToken(UserDto.getEmail(), UserDto.getPassword(), null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 그림에서 3번 로직
        return authenticationManager.authenticate(authToken);
    }

    
    // 그림에서 9번 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User User = (User) authResult.getPrincipal();
        String jwtToken = JwtUtil.generateToken(User.getIdx(), User.getEmail());

        ResponseCookie cookie = ResponseCookie
                .from("ATOKEN", jwtToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofHours(1L))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect("/");



    }
}
