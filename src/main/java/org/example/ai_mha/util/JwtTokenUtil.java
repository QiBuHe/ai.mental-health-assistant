package org.example.ai_mha.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.ServletRequestAttributeEvent;
import lombok.Getter;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.example.ai_mha.config.JwtConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
@Component
public class JwtTokenUtil implements ApplicationContextAware {
    private static final String ISSUER = "mental-health-assistant";

    private static ApplicationContext applicationContext;

    //用于在静态工具类管理容器的方法
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        JwtTokenUtil.applicationContext = applicationContext;
    }

    public static JwtConfig getJwtConfig(){
      return applicationContext.getBean(JwtConfig.class);
    }
    //生成taken的方法
    public static String generateToken(long userId, String username, Integer roleType){
        try{
            //获取jwt配置
            JwtConfig jwtConfig = getJwtConfig();
            //生成签名的算法
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());

            /*long expireation = System.currentTimeMillis() + jwtConfig.getExpiration();*/

            Date expiration=new Date(System.currentTimeMillis() + jwtConfig.getExpiration());

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withClaim("userId", userId)
                    .withClaim("username", username)
                    .withClaim("roleType", roleType)
                    .withExpiresAt(expiration)
                    .sign(algorithm);
        }catch (Exception e){
            throw new RuntimeException("生成token失败:"+e);
        }
    }
    //提取token
    public static String extractTokenFromRequest(HttpServletRequest request){
        if(request==null){
            return null;
        }
        String tokenHeader=request.getHeader("token");
        if(StringUtils.hasText(tokenHeader)){
            return tokenHeader;
        }
        return null;
    }

    //获取当前的token
    public static String getCurrentToken(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes!=null){
            HttpServletRequest request = attributes.getRequest();
            String token =(String) request.getAttribute("jwtToken");
            if(token != null){
                return token;
            }
            //从请求头
            String headToken = extractTokenFromRequest(request);
            return headToken;
        }
return null;
    }

    //验证token
    public static TokenValidationResult validateToken(String token){

        DecodedJWT jwt = verifyToken(token);
        Long userId = jwt.getClaim("userId").asLong();
        String username = jwt.getClaim("username").asString();

        //角色类型
        Integer roleType = null;
        try{
            roleType = jwt.getClaim("roleType").asInt();
        }catch (Exception e){
            String roleTypeStr = jwt.getClaim("roleType").asString();
            if(StringUtils.hasText(roleTypeStr)){
                roleType = Integer.valueOf(roleTypeStr);
            }
        }
        if(userId != null && StringUtils.hasText(username) && roleType != null){
            return new TokenValidationResult(userId,username,roleType,true);
        }
        return null;
    }

    //验证token的有效性
    public static DecodedJWT verifyToken(String token){
        if(!StringUtils.hasText(token)){
            throw new JWTVerificationException("token不能为空");
        }
        JwtConfig jwtConfig = getJwtConfig();
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        return verifier.verify(token);
    }

    @Getter    //token验证结果返回类
    public static class TokenValidationResult{
        private final Long userId;
        private final String username;
        private final Integer roleType;
        private final boolean valid;

        public TokenValidationResult(Long userId, String username, Integer roleType, boolean valid) {
            this.userId = userId;
            this.username = username;
            this.roleType = roleType;
            this.valid = valid;
        }
    }
}
