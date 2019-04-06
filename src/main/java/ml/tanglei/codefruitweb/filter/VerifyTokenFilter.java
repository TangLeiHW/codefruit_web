package ml.tanglei.codefruitweb.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import ml.tanglei.codefruitweb.common.Response;
import ml.tanglei.codefruitweb.utils.JsonUtils;
import ml.tanglei.codefruitweb.utils.JwtTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Token验证过滤器
 */
public class VerifyTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String authorization = httpServletRequest.getHeader("Authorization");
        //放行GET请求
      /*  if (httpServletRequest.getMethod().equals(String.valueOf(RequestMethod.GET))) {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }*/
        //未提供Token
        if (StringUtils.isEmpty(authorization)) {
            httpServletResponse.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(403).setMessage("Token not provided").build()));
            return;
        }
        //Token格式错误
        if (!authorization.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            httpServletResponse.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(403).setMessage("Token format error").build()));
            return;
        }
        String token = authorization.replace(JwtTokenUtils.TOKEN_PREFIX,"");
        Claims claims = null;
        try {
            claims = JwtTokenUtils.parseToken(token);
        } catch (ExpiredJwtException e) {
            logger.error("token expired", e);
            httpServletResponse.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(403).setMessage("token expired").build()));
            return;
        } catch (InvalidClaimException e) {
            logger.error("token invalid", e);
            httpServletResponse.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(403).setMessage("token invalid").build()));
            return;
        } catch (Exception e) {
            logger.error("token error" , e);
            httpServletResponse.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(403).setMessage("token error").build()));
            return;
        }
        //Token不可解码
        if (null == claims) {
            httpServletResponse.getWriter().write(JsonUtils.objectToJson(new Response.Builder().setStatus(403).setMessage("Can't parse token").build()));
            return;
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
