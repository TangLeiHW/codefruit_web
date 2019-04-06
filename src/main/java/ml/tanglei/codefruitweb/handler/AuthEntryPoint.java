package ml.tanglei.codefruitweb.handler;

import ml.tanglei.codefruitweb.common.Response;
import ml.tanglei.codefruitweb.utils.JsonUtils;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 处理AuthenticationException异常，即：未登录状态下访问受保护资源
 *  * Security默认实现 {@link org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint}
 *
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(401).setMessage("Please login").build()));
    }
}
