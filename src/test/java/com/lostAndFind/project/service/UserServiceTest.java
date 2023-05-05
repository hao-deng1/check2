//package com.lostAndFind.project.service;
//
//import com.lostAndFind.project.model.entity.Pick;
//import com.lostAndFind.project.model.entity.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 用户服务测试
// *
// * @author yupi
// */
//@SpringBootTest
//class UserServiceTest {
//
//    @Resource
//    private UserService userService;
//    @Resource
//    private PickService pickService;
//
//    @Test
//    void testUrl(){
//        String url = "http%3A%2F%2Fcdn.djkyyds.fun%2F40cce4274ab64f96b865bf05bafae39a.jpg=";
//        url = url.replaceAll("%3A", ":").replaceAll("%2F", "/")  //过滤URL 包含中文
//                .replaceAll("%3F", "?").replaceAll("%3D", "=").replaceAll(
//                        "%26", "&");
//        System.out.println(url);
//    }
//
//    @Test
//    void testAddUser() {
//        User user = new User();
//        user.setUsername("dogYupi");
//        user.setUserAccount("123");
//        user.setAvatarUrl("");
//        user.setGender(0);
//        user.setUserPassword("xxx");
//        user.setPhone("123");
//        user.setEmail("456");
//        boolean result = userService.save(user);
//        System.out.println(user.getId());
//        Assertions.assertTrue(result);
//    }
//
//    @Test
//    void testUpdateUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("dogYupi");
//        user.setUserAccount("123");
//        user.setAvatarUrl("");
//        user.setGender(0);
//        user.setUserPassword("xxx");
//        user.setPhone("123");
//        user.setEmail("456");
//        boolean result = userService.updateById(user);
//        Assertions.assertTrue(result);
//    }
//
//    @Test
//    void testDeleteUser() {
//        boolean result = userService.removeById(1L);
//        Assertions.assertTrue(result);
//    }
//
//    @Test
//    void testGetUser() {
//        User user = userService.getById(1L);
//        Assertions.assertNotNull(user);
//    }
//
//    @Test
//    void userRegister() {
//        String userAccount = "yupi";
//        String userPassword = "";
//        String checkPassword = "123456";
//        try {
//            long result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            userAccount = "yu";
//            result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            userAccount = "yupi";
//            userPassword = "123456";
//            result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            userAccount = "yu pi";
//            userPassword = "12345678";
//            result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            checkPassword = "123456789";
//            result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            userAccount = "dogYupi";
//            checkPassword = "12345678";
//            result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            userAccount = "yupi";
//            result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Test
//    public void test() {
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("a");
//        list.add("b");
//        list.add("c");
//        list.add("d");
//        System.out.println(list.subList(0, 2));
//    }
//}