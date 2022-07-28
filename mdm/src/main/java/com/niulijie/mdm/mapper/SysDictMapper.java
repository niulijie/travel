package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niulijie.mdm.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统参数表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    List<String> selectAllKey(@Param("commentShow") String commentShow, @Param("commentEnable") String commentEnable);
}
