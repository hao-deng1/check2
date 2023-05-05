package com.lostAndFind.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lostAndFind.project.model.entity.LoginUser;
import com.lostAndFind.project.model.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
* @author a
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2022-09-16 17:28:13
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 微信小程序进入添加信息
     * @param openid 微信小程序进入添加信息
     */
    @Insert("insert into user(openid) values (#{openid})")
    void insertOpenId(@Param("openid") String openid);

}




