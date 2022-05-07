package com.atguigu.admin.mapper;

import com.atguigu.admin.bean.DaPrLocationDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DaPrLocationDictMapper{

    @Update("TRUNCATE table da_pr_location_dict")
    void deleteLocationDict();

    @Select("select * from da_pr_location_dict where id = #{id}")
    public DaPrLocationDict getById(Integer id);
}
