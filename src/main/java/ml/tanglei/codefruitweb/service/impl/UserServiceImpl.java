package ml.tanglei.codefruitweb.service.impl;

import ml.tanglei.codefruitweb.model.Entity.UserDO;
import ml.tanglei.codefruitweb.common.RestResponse;
import ml.tanglei.codefruitweb.model.Entity.UserPasswordDO;
import ml.tanglei.codefruitweb.model.Vo.UserVO;
import ml.tanglei.codefruitweb.repository.UserPassRepository;
import ml.tanglei.codefruitweb.utils.QiniuOSSUtils;
import ml.tanglei.codefruitweb.utils.SmsUtils;
import ml.tanglei.codefruitweb.common.StaticParam;
import ml.tanglei.codefruitweb.repository.UserRepository;
import ml.tanglei.codefruitweb.service.RedisService;
import ml.tanglei.codefruitweb.service.UserService;
import ml.tanglei.codefruitweb.model.Dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPassRepository userPassRepository;
    @Autowired
    private BCryptPasswordEncoder bpe;
    @Value("${default.user.avatar}")
    private String DEFAULT_USER_AVATAR;
    @Value("${default.user.password}")
    private String DEFAULT_USER_PASSWORD;

    /**
     * 获取验证码
     * @param phoneNum
     */
    @Override
    public boolean getCaptcha(String phoneNum) {
        //1、生成6位数验证码
        int code = (int) ((Math.random()*9 + 1)*100000);
        //2、把六位验证码存入 redis key为手机号 value为验证码 并设置有效时间一个小时
        boolean result = redisService.set(phoneNum, code, 60 * 60 * 1000);
        //3、如果生成成功发送短信
        if (result) {
            //"{\"name\":\"Tom\", \"code\":\"123\"}"
            String templateParam = "{\"code\":\"" + code + "\"}";
            //result = SmsUtils.sendMsg(phoneNum, StaticParam.ALIDY_REGISTTEMPLETECODE, templateParam);
            System.out.println("验证码：" + code);
            result = true;
        }
        return result;
    }

    /**
     * 校验验证码
     * @param phoneNum
     * @param code
     * @return
     */
    @Override
    public RestResponse checkCaptcha(String phoneNum, String code) {
        //1、校验验证码是否存在
        Object obj = redisService.get(phoneNum);
        if (obj != null) {
            //2、校验验证码
            int oldCode = (int) obj;
            if (Integer.valueOf(code) != oldCode) {
                //验证码不正确
                return RestResponse.build(10001, "验证码不正确");
            }
            return RestResponse.ok();

        } else {
            //验证码失效
            return RestResponse.build(10001, "验证码失效");
        }
    }

    /**
     * 用户注册
     * @param phoneNum
     * @param password
     * @param localAddr
     * @return
     */
    @Override
    public RestResponse registered(String phoneNum, String password, String localAddr) {
        //查询用户是否已被注册
        boolean userIsExists = userRepository.existsByUserPhone(phoneNum);
        if (!userIsExists) {
                //1、验证成功，注册用户
                //初始化用户信息
                UserDO ud = new UserDO();
                ud.setUserName(phoneNum);
                ud.setUserPhone(phoneNum);
                ud.setLocalAddress(localAddr);
                ud.setUserRole(1);
                ud.setBeConcernedCount(0L);
                ud.setStartCount(0);
                ud.setSex("-1");
                ud.setRegisterTime(new Date());
                //设置默认头像
                ud.setUserAvatar(DEFAULT_USER_AVATAR);
                //插入数据库
                UserDO user = userRepository.save(ud);
                UserPasswordDO upd = new UserPasswordDO();
                upd.setId(user.getId());
                upd.setEncryptPassword(bpe.encode(password));
                userPassRepository.save(upd);
                redisService.remove(phoneNum);
                return RestResponse.ok();
        } else {
            //用户已存在
            return RestResponse.build(10001, "用户已存在");
        }
    }

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    @Override
    public UserDto getUser(String username) {
        UserDto udo = new UserDto();
        //查询用户信息
        UserDO user = userRepository.findByUserName(username);
        BeanUtils.copyProperties(user, udo);
        Optional<UserPasswordDO> optional = userPassRepository.findById(user.getId());
        udo.setEncryptPassword(optional.get().getEncryptPassword());
        return udo;
    }

    /**
     * 修改用户密码
     * @param id
     * @param password
     */
    @Override
    @Transactional
    public void updatePassword(Integer id, String password) {
        userPassRepository.updatePasswordById(id,bpe.encode(password));
    }

    /**
     * 修改用户头像
     * @param inputStream
     * @param userId
     */
    @Override
    @Transactional
    public void updateAvatar(InputStream inputStream, Integer userId) {
        //上传文件到七牛云
        String fileName = QiniuOSSUtils.uploadFile(inputStream);
        try {
            userRepository.updateAvatar(userId,StaticParam.QINIU_DOMIN + "/" + fileName);
        } catch (Exception e) {
            //如果插入失败删除上传的文件
            QiniuOSSUtils.removeFile(fileName);
            logger.error("修改用户头像异常",e);
        }
    }

    /**
     * 更新用户信息
     * @param userId
     * @param uv
     */
    @Override
    public void updateUser(Integer userId, UserVO uv) {
        //由于save 做更新操作是是全量更新，因此先做查询 赋值 避免未更新字段被当作null更新
        Optional<UserDO> optional = userRepository.findById(userId);
        UserDO ud = optional.get();
        BeanUtils.copyProperties(uv,ud);
        userRepository.save(ud);
    }


}
