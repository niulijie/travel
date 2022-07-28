package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niulijie.mdm.dto.response.UserDept;
import com.niulijie.mdm.dto.response.UserDeptInfo;
import com.niulijie.mdm.entity.UcDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UcDeptMapper extends BaseMapper<UcDept> {


    /**
     * 获取当前部门以及下级部门id (单个)
     */
    List<Integer> getDeptLevelId( @Param("deptId") Integer deptId);

    List<UserDept> selectDeptInfo(@Param("userIds") Set<Integer> userIdInfos);

    UserDeptInfo selectDeptInfoByUserId(Integer userId);
}