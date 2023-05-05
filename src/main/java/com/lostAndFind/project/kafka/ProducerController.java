//package com.lostAndFind.project.kafka;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author yc_
// * @version 1.0
// */
//@RestController
//public class ProducerController {
//
//    @Autowired
//    KafkaTemplate<String,String> kakfa;
//
//    @RequestMapping("/send")
//    public String data(String msg){
//        kakfa.send("springboot-kafak-api",msg);
//        System.out.println("发送成功。。。。。。。。。。。。。。。");
//        return "ok";
//    }
//}
