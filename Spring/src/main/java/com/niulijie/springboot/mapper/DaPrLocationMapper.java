package com.niulijie.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niulijie.springboot.entity.DaPrLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (DaPrLocation)表数据库访问层
 *
 * @author dufa
 * @since 2021-12-25 17:08:56
 */
@Mapper
public interface DaPrLocationMapper extends BaseMapper<DaPrLocation> {

    void insertList(@Param("daPrLocations") List<DaPrLocation> daPrLocations);

    @Update("TRUNCATE table da_pr_location")
    void deleteLocation();
}
