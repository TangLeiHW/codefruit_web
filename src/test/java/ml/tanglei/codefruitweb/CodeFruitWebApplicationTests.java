package ml.tanglei.codefruitweb;

import ml.tanglei.codefruitweb.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeFruitWebApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    public void contextLoads() {
        boolean resulut = redisService.set("123", 123);
        if (resulut) {
            System.out.printf((Integer)redisService.get("123") + "");
        } else {
            System.out.printf("redis错误");
        }

    }

}

