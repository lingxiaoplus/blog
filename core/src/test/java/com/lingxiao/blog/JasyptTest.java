package com.lingxiao.blog;

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author renml
 * @date 2020/12/9 9:11
 */
@Slf4j
public class JasyptTest {
    static {
        System.setProperty("jasypt.encryptor.password","lE1rl5K$");
    }
    StringEncryptor stringEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());
    @Test
    void getMysqlEncoder(){
        String username = stringEncryptor.encrypt("root");
        log.debug("加密之后的username:{}",username);
        String password = stringEncryptor.encrypt("123456");
        log.debug("加密之后的password:{}",password);
    }
    @Test
    void getOssEncoder(){
        String accessKey = stringEncryptor.encrypt("V_ZleIlYzHPjekmAuRvDkkXLdV_MDkk69VZWM12f");
        String secretKey = stringEncryptor.encrypt("U8SndxcceqMZGIKvhfPL3gEPkYRN_NhrP75cJlOH");
        log.debug("加密之后的accessKey:{}, 解密: {}",accessKey,stringEncryptor.decrypt(accessKey));
        log.debug("加密之后的secretKey:{}, 解密: {}",secretKey,stringEncryptor.decrypt(secretKey));
        String redisPassword = stringEncryptor.encrypt("lingxiao");
        log.debug("加密之后的redisPassword:{}",redisPassword);
    }
}
