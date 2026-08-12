package org.example.ai_mha.util;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ai_mha.DTO.response.UserLoginResponseDTO;
import org.example.ai_mha.common.ResultCode;
import org.example.ai_mha.config.SecurityConfig;
import org.example.ai_mha.enumClass.UserStatus;
import org.example.ai_mha.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthticationFilter extends OncePerRequestFilter {

    @Resource
    private UserService userService;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
     String requestURI=request.getRequestURI();
     return SecurityConfig.isPublicPATH(requestURI);
    }

    @Override
    protected void doFilterInternal(
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException,
            IOException {

        String requestURI=request.getRequestURI();
        String method=request.getMethod();
        System.out.println("requestURI:"+requestURI+" method:"+method);

        //提取Jwt的token
        String token=JwtTokenUtil.extractTokenFromRequest(request);
        if(StringUtils.hasText(token)){
//验证token并获取用户信息
            JwtTokenUtil.TokenValidationResult validationResult = JwtTokenUtil.validateToken(token);
            if(validationResult != null && validationResult.isValid()){
//查询用户信息验证状态
                UserLoginResponseDTO.UserDetailResponseDTO user = userService.getUserById(validationResult.getUserId());
                System.out.println(JSONUtil.parseObj(user));
                if(user != null && UserStatus.NORMAL.getCode().equals(user.getStatus())){
//用户状态正常，处理请求
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            //创建Spring Security认证对象
                            new SimpleGrantedAuthority("ROLE_"+ validationResult.getRoleType())

                    );
                    //创建UsernamePasswordAuthenticationToken对象
                    UsernamePasswordAuthenticationToken authcation=new UsernamePasswordAuthenticationToken(
                            validationResult.getUsername(),
                            null,
                            authorities
                    );
                    //设置认证信息到上下文
                    SecurityContextHolder.getContext().setAuthentication(authcation);
                    //将token存储到申请属性中
                    request.setAttribute("jwtToken",token);

                } else {
                    //清理上下文
                    clearSecurityContext();
                    ResponseUtil.writeError(response, ResultCode.ACCESS_UNAUTHORIZED);
                }
            }
            else {
                clearSecurityContext();
                ResponseUtil.writeError(response, ResultCode.TOKEN_INVALID);
            }
        }
//继续过滤器链
        chain.doFilter(request,response);
    }
    private void clearSecurityContext(){
        SecurityContextHolder.clearContext();
    }


}