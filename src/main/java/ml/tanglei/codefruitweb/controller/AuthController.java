package ml.tanglei.codefruitweb.controller;

import ml.tanglei.codefruitweb.common.RestResponse;
import ml.tanglei.codefruitweb.model.Entity.UserDO;
import ml.tanglei.codefruitweb.model.Entity.UserPasswordDO;
import ml.tanglei.codefruitweb.repository.UserPassRepository;
import ml.tanglei.codefruitweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPassRepository userPassRepository;

    @PostMapping("/register")
    public RestResponse registerUser(@RequestBody Map<String, String> registerUser) {
        UserDO user = new UserDO();
        user.setUserName(registerUser.get("userName"));
        user.setUserPhone("18109872380");
        UserDO userDO = userRepository.save(user);
        UserPasswordDO upd = new UserPasswordDO();
        upd.setId(userDO.getId());
        upd.setEncryptPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));
        userPassRepository.save(upd);
        return RestResponse.ok();
    }

}
