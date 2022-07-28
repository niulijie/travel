package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niulijie.mdm.entity.SysSensitiveWord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 敏感词表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Mapper
public interface SysSensitiveWordMapper extends BaseMapper<SysSensitiveWord> {

    List<String> selectSensitiveWord();

    void insertList(@Param("sysSensitiveWords") List<SysSensitiveWord> sysSensitiveWords);
}
