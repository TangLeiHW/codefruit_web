package ml.tanglei.codefruitweb.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET = "codefruitweb"; //JWT密码
    public static final String ISS = "survivor"; //JWT是由谁签发的

    //过期时间是3600秒，即是1个小时
    private static final long EXPIRATION = 3600L;

    //创建token
    public static String createToken(Map<String,Object> userInfo) {
        JwtBuilder builder = Jwts.builder()
                                .setHeaderParam("typ","JWT").setHeaderParam("alg","HS256")
                                .claim("userInfo",userInfo)
                                .setIssuer(ISS)
                                .signWith(SignatureAlgorithm.HS256,SECRET)
                                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION*1000))
                                .setNotBefore(new Date(System.currentTimeMillis()));
        return builder.compact();
    }

    //从token中获取用户信息
    public static Map<String,Object>  getUserName(String token){
        Map<String,Object> result = new HashMap<>();
        Object userInfo = null;
        try {
            userInfo = parseToken(token).get("userInfo");
            if (userInfo instanceof Map) {
                result = (Map<String,Object>) userInfo;
            }
        } catch (Exception e) {
            logger.error("获取token用户信息异常",e);
        }
        return result;
    }

    /**
     * 解析Token
     * @param token
     * @return
     */
    public static Claims parseToken(String token) throws Exception {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
    }
}
