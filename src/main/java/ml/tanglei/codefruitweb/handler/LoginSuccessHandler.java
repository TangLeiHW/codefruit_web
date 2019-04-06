package ml.tanglei.codefruitweb.handler;

import ml.tanglei.codefruitweb.common.RestResponse;
import ml.tanglei.codefruitweb.model.Dto.JwtUser;
import ml.tanglei.codefruitweb.service.UserService;
import ml.tanglei.codefruitweb.utils.JsonUtils;
import ml.tanglei.codefruitweb.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆成功Handler
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        logger.info("jwtUser:" + jwtUser.toString());
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("username",jwtUser.getUsername());
        userInfo.put("userId",jwtUser.getUserId());
        userInfo.put("userPhone",jwtUser.getUserPhone());
        String token = JwtTokenUtils.createToken(userInfo);
        Map<String,String> map = new HashMap<>();
        map.put("token",JwtTokenUtils.TOKEN_PREFIX + token);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.objectToJson(RestResponse.ok(map)));
    }
}
