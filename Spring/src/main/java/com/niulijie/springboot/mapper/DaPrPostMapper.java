package com.niulijie.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niulijie.springboot.entity.DaPrPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (DaPrPost)表数据库访问层
 *
 * @author dufa
 * @since 2021-12-25 17:08:54
 */
@Mapper
public interface DaPrPostMapper extends BaseMapper<DaPrPost> {

    void insertList(@Param("reportList") List<DaPrPost> reportList);

    @Update("TRUNCATE table da_pr_post")
    void deletePost();
}
