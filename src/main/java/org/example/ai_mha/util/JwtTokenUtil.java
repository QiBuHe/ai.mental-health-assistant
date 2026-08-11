package org.example.ai_mha.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.ai_mha.config.JwtConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtTokenUtil implements ApplicationContextAware {
    private static final String ISSUER = "ai-mha";

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
}
