package org.example.ai_mha.config;

import cn.hutool.core.text.AntPathMatcher;
import org.example.ai_mha.util.JwtAuthticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static final String[] PUBLIC_PATHS={
            "/",
            "/api/test",
            "/api/User/login",
            "/api/User/add"
    };

    public static boolean isPublicPATH(String requestURI) {

        for (String publicPath : PUBLIC_PATHS) {
            if (antPathMatcher.match(publicPath,requestURI)) {
                return true;
            }
        }
        return false;
    }
    @Bean
    public JwtAuthticationFilter jwtAuthticationFilter(){
        return new JwtAuthticationFilter();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //配置请求的授权
                .authorizeHttpRequests(auth -> auth
                  //设置公开的路径
                  .requestMatchers(PUBLIC_PATHS).permitAll()
                //其他的请求都需要认证
                  .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
        }
        /*//添加JWT认证
        .addFilterBefore();*/
}
