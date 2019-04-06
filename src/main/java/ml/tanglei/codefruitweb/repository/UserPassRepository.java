package ml.tanglei.codefruitweb.repository;

import ml.tanglei.codefruitweb.model.Entity.UserPasswordDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPassRepository extends JpaRepository<UserPasswordDO,Integer> {

    @Modifying
    @Query(value = "update tb_cf_user_password set encrypt_password = :password where id = :id",nativeQuery=true)
    void updatePasswordById(@Param(value = "id") Integer id,@Param(value = "password") String newpassword);
}
