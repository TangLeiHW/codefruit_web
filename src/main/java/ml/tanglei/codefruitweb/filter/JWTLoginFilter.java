package ml.tanglei.codefruitweb.filter;

import ml.tanglei.codefruitweb.common.Response;
import ml.tanglei.codefruitweb.utils.JsonUtils;
import ml.tanglei.codefruitweb.utils.JwtTokenUtils;
import ml.tanglei.codefruitweb.model.Dto.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证登录的信息
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final static Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            //获取到登陆的信息
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

    /**
     * 成功验证调用的方法
     * 如果验证成功，就生成token并返回
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        logger.info("jwtUser:" + jwtUser.toString());
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("username",jwtUser.getUsername());
        userInfo.put("userId",jwtUser.getUserId());
        userInfo.put("userPhone",jwtUser.getUserPhone());
        String token = JwtTokenUtils.createToken(userInfo);
        //返回创建成功的token
        //但是这里创建的token只是单纯的token
        //安装jwt的规定。最后请求的格式应该是 “Bearer token”
        Map<String,String> map = new HashMap<>();
        map.put("token",JwtTokenUtils.TOKEN_PREFIX + token);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(200).setData(map).build()));
    }

    /**
     * 验证失败时候调用的方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(401).setMessage("Incorrect username or password").build()));
    }
}
