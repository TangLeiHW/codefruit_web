package ml.tanglei.codefruitweb.config;

import ml.tanglei.codefruitweb.filter.JWTLoginFilter;
import ml.tanglei.codefruitweb.filter.JWTAuthenticationFilter;
import ml.tanglei.codefruitweb.filter.VerifyTokenFilter;
import ml.tanglei.codefruitweb.handler.AuthEntryPoint;
import ml.tanglei.codefruitweb.handler.LoginFailureHandler;
import ml.tanglei.codefruitweb.handler.LoginSuccessHandler;
import ml.tanglei.codefruitweb.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthEntryPoint authEntryPoint;// 权限认证异常处理器
    @Autowired
    private LoginSuccessHandler loginSuccessHandler; // 登录成功处理器
    @Autowired
    private LoginFailureHandler loginFailureHandler; // 登录失败处理器
    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler; // 注销成功处理器


    //加密密码的
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //配置UserSevice和密码加密服务
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //默认会启用CRSF，此处因为没有使用thymeleaf模板（会自动注入_csrf参数），
        //     * 要先禁用csrf，否则登录时需要_csrf参数，而导致登录失败。
        http.cors().and().csrf().disable()
                .authorizeRequests()
                //测试用资源，需要验证了的用户才能访问
                //.antMatchers("/auth/login").permitAll()
                //其他都拦截
                .anyRequest().authenticated()
                .and()
                .formLogin().loginProcessingUrl("/auth/login").successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(myLogoutSuccessHandler)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .exceptionHandling().authenticationEntryPoint(authEntryPoint);
        //禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //禁用headers缓存
        http.headers().cacheControl();
        //配置jwt验证过滤器，位于用户名密码验证过滤后
        http.addFilterAfter(new VerifyTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(HttpMethod.POST,"/user");
        web.ignoring().mvcMatchers(HttpMethod.GET,"/captcha");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
