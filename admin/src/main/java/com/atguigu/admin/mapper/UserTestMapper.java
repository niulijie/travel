package com.atguigu.admin.mapper;

import com.atguigu.admin.bean.UserTest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface UserTestMapper {

    public UserTest getUserTest(Long userId);

    @Insert("insert into user_test (name,age,grade,sex,create_time,update_time) values (#{name},#{age},#{grade},#{sex},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    public void insert(UserTest userTest);
}
