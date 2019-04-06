package ml.tanglei.codefruitweb.service.impl;

import ml.tanglei.codefruitweb.model.Entity.UserDO;
import ml.tanglei.codefruitweb.model.Entity.UserPasswordDO;
import ml.tanglei.codefruitweb.repository.UserPassRepository;
import ml.tanglei.codefruitweb.repository.UserRepository;
import ml.tanglei.codefruitweb.model.Dto.JwtUser;
import ml.tanglei.codefruitweb.model.Dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPassRepository userPassRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO userDO = userRepository.findByUserName(username);
        Optional<UserPasswordDO> userPassOp = userPassRepository.findById(userDO.getId());
        UserDto um = new UserDto();
        um.setId(userDO.getId());
        um.setUserName(userDO.getUserName());
        um.setUserPhone(userDO.getUserPhone());
        um.setEncryptPassword(userPassOp.get().getEncryptPassword());
        return new JwtUser(um);
    }
}
