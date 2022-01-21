package com.niulijie.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niulijie.springboot.entity.DaPrLocationDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (DaPrLocationDict)表数据库访问层
 *
 * @author dufa
 * @since 2022-01-11 17:23:10
 */
@Mapper
public interface DaPrLocationDictMapper extends BaseMapper<DaPrLocationDict> {

    void insertList(@Param("daPrLocations") List<DaPrLocationDict> daPrLocations);

    @Update("TRUNCATE table da_pr_location_dict")
    void deleteLocationDict();
}
