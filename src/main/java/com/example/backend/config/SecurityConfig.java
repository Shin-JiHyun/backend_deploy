package com.example.backend.config;

import com.example.backend.config.filter.JwtFilter;
import com.example.backend.config.filter.LoginFilter;
import com.example.backend.config.filter.OAuth2SuccessHandler;
import com.example.backend.user.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration configuration;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.oauth2Login( config-> {
            config.successHandler(new OAuth2SuccessHandler());
            config.userInfoEndpoint( endpoint->
                    endpoint.userService(customOAuth2UserService)
            );
        });

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);
//        http.logout(logout -> logout
//                        .logoutSuccessUrl("/login")
//                        .deleteCookies("ATOKEN")
//                );
//

        http.authorizeHttpRequests(
                (auth) -> auth
                        .requestMatchers("/**","/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("/course/register").hasRole("INSTRUCTOR")
                        .requestMatchers("/login", "/user/signup", "/user/instructor/signup", "/user/verify").permitAll()
                        .requestMatchers("/course/list", "/course/*", "/user/verify").permitAll()
                        .anyRequest().authenticated()
        );

        // 기존에 사용자한테 설정하도록 한 쿠키(JSESSIONID)를 사용하지 않도록 하는 설정
        http.sessionManagement(AbstractHttpConfigurer::disable);

        http.addFilterAt(new LoginFilter(configuration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAt(new CustomLogoutFilter(new SimpleUrlLogoutSuccessHandler(), new Log), LogoutFilter.class);
        return http.build();
    }
}
