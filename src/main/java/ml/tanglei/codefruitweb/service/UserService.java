package ml.tanglei.codefruitweb.service;

import ml.tanglei.codefruitweb.common.RestResponse;
import ml.tanglei.codefruitweb.model.Dto.UserDto;
import ml.tanglei.codefruitweb.model.Vo.UserVO;

import java.io.File;
import java.io.InputStream;

public interface UserService {

    boolean getCaptcha(String phoneNum);

    RestResponse checkCaptcha(String phoneNum,String code);

    RestResponse registered(String phoneNum, String password, String localAddr);

    UserDto getUser(String username);

    void updatePassword(Integer id,String password);

    void updateAvatar(InputStream inputStream, Integer userId);

    void updateUser(Integer userId, UserVO uv);

}
