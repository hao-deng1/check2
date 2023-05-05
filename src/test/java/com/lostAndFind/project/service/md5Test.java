package com.lostAndFind.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lostAndFind.project.mapper.UserMapper;
import com.lostAndFind.project.model.entity.User;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

public class md5Test {
    @Autowired
    UserMapper userMapper;
    @Test
    public void testEncrypt() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword("Djk");                        // 加密的密钥，随便自己填写，很重要千万不要告诉别人
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "6379";         //自己的密码
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }
    @Test
    public void testDe() {
        String SALT = "Djk";
        String userPassword = "12345678";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        System.out.println(encryptPassword);
//        fb89569495f47e1e4e4fafc68e2c0dd8
//        $2a$10$IetKpBLeqbD5H7bhSooU2u9T9rSjSFKOqhAAXk6wycSgjMtYDzSGi
//        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
//        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
//
//        config.setAlgorithm("PBEWithMD5AndDES");
//        config.setPassword("Djk");
//        standardPBEStringEncryptor.setConfig(config);
//        String encryptedText = "wmwFSvW/mPD+Yw9OrO2nDiOXSeJ870/0hnUrKooItOgPAzi13MrftyAFR+1MjTpE";   //加密后的密码
//        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
//        System.out.println(plainText);
    }
    @Test
    public void testLogin(){
        String userPassword = "12345678";
        String userAccount = "dogdjk";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userPassword);
        System.out.println("========");
        System.out.println(encode);
        System.out.println("========");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encode);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println("========");
        System.out.println(user);
        System.out.println("========");
    }
}
