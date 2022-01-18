package com.niulijie.springboot.mapper;

import com.niulijie.springboot.entity.UserTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (UserTest)表数据库访问层
 *
 * @author niulijie
 * @since 2021-12-21 16:56:35
 */
@Mapper
public interface UserTestMapper extends BaseMapper<UserTest> {

    /**
     * 批量插入
     * @param reportList
     */
    void insertList(@Param("reportList") List<UserTest> reportList);
}
