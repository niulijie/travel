package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niulijie.mdm.entity.ContVideoLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 喜欢信息表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Mapper
public interface ContVideoLikeMapper extends BaseMapper<ContVideoLike> {

    List<ContVideoLike> selectLikeInfo(@Param("commentIds") Set<Integer> commentIds, @Param("userId") Integer userId);
}
