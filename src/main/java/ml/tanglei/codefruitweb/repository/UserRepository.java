package ml.tanglei.codefruitweb.repository;

import ml.tanglei.codefruitweb.model.Entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserDO,Integer> {

    UserDO findByUserName(String userName);

    boolean existsByUserPhone(String userPhone);

    @Modifying
    @Query(value = "update tb_cf_user set user_avatar = :userAvatar where id = :id",nativeQuery = true)
    void updateAvatar(@Param(value = "id") Integer userId,@Param(value = "userAvatar") String userAvatar);
}
