package ml.tanglei.codefruitweb.controller;

import ml.tanglei.codefruitweb.common.RestResponse;
import ml.tanglei.codefruitweb.model.Dto.UserDto;
import ml.tanglei.codefruitweb.model.Vo.UserVO;
import ml.tanglei.codefruitweb.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 获取验证码
     * @param phoneNum
     * @return
     */
    @GetMapping(value = "/captcha")
    public RestResponse getCode(@Pattern(regexp="^1[3|4|5|7|8][0-9]{9}$", message="请输入正确的手机号") String phoneNum) {
        if (StringUtils.isBlank(phoneNum)) {
            return RestResponse.build(10001,"手机号码不能为空");
        }
        boolean result = userService.getCaptcha(phoneNum);
        if (!result) {
            return RestResponse.build(20001,"发送验证码失败");
        }
        return RestResponse.ok();
    }

    @PostMapping(value = "/captcha")
    public RestResponse checkCaptcha(@Pattern(regexp="^1[3|4|5|7|8][0-9]{9}$", message="请输入正确的手机号")String phoneNum,
                                     @NotBlank(message = "验证码不能为空") String code) {
        RestResponse restResponse = userService.checkCaptcha(phoneNum, code);
        return restResponse;
    }


    /**
     * 注册用户
     * @param phoneNum
     * @param code
     * @param localAddr
     * @return
     */
    @PostMapping(value = "/user")
    public  RestResponse registerUser(@Pattern(regexp="^1[3|4|5|7|8][0-9]{9}$", message="请输入正确的手机号")String phoneNum,
                                      @NotBlank(message = "验证码不能为空") String code,
                                      String localAddr) {
        //注册用户
        RestResponse result = userService.registered(phoneNum, code, localAddr);
        return result;
    }

    /**
     * 获取用户信息
     * @param userInfo
     * @return
     */
    @GetMapping(value = "/user")
    public RestResponse getUser(@NotNull(message = "token中不存在用户信息") @AuthenticationPrincipal Object userInfo) {
        Map<String,Object> userInfoMap = (Map<String, Object>) userInfo;
        //查询用户信息
        UserDto ud = userService.getUser((String) userInfoMap.get("username"));
        UserVO uv = new UserVO();
        BeanUtils.copyProperties(ud,uv);
        return RestResponse.ok(uv);
    }


    /**
     * 修改密码
     * @param newPassword
     * @param userInfo
     * @return
     */
    @PutMapping(value = "/user/password")
    public RestResponse updatePassword(@NotBlank(message = "新密码不能为空") String newPassword,
                                       @NotNull(message = "token中不存在用户名") @AuthenticationPrincipal Object userInfo) {
        Map<String,Object> userInfoMap = (Map<String, Object>) userInfo;
        userService.updatePassword((Integer) userInfoMap.get("userId"),newPassword);
        return RestResponse.ok();
    }

    /**
     * 修改用户头像
     * @param file
     * @param userInfo
     * @return
     */
    @PutMapping(value = "/user/avatar")
    public RestResponse updateAvatar(@RequestParam("file")MultipartFile file,
                                     @NotNull(message = "token中不存在用户名") @AuthenticationPrincipal Object userInfo) {
        try {
            Map<String,Object> userInfoMap = (Map<String, Object>) userInfo;
            InputStream inputStream = file.getInputStream();
            userService.updateAvatar(inputStream, (Integer) userInfoMap.get("userId"));
            return RestResponse.ok();
        } catch (Exception e) {
            logger.error("获取文件流异常",e);
            return RestResponse.build(20001,"修改用户头像失败");
        }
    }

    /**
     * 更新用户信息
     * @param userVO
     * @param userInfo
     * @return
     */
    @PutMapping(value = "/user")
    public RestResponse updateUser(UserVO userVO,
                                   @NotNull(message = "token中不存在用户名") @AuthenticationPrincipal Object userInfo) {
        Map<String,Object> userInfoMap = (Map<String, Object>) userInfo;
        userService.updateUser((Integer) userInfoMap.get("userId"),userVO);
        return RestResponse.ok();
    }


}
