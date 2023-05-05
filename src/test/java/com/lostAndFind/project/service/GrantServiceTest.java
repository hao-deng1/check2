//package com.lostAndFind.project.service;
//
//import com.lostAndFind.project.model.vo.GrantVo;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 赋权测试
// *
// * @author djk
// */
//@SpringBootTest
//public class GrantServiceTest {
//    @Resource
//    UserService userService;
//
//    @Resource
//    GrantService grantService;
//    @Test
//    public void grant1Test(){
//        GrantVo grantVo = new GrantVo();
//        grantVo.setUserId(2L);
//        List<Long> list = new ArrayList<>();
//        list.add(2L);
//        grantVo.setRoleIds(list);
//        grantService.grant(grantVo);
//    }
//}
