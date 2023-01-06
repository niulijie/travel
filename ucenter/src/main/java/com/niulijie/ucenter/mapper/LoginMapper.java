package com.niulijie.ucenter.mapper;

import com.niulijie.ucenter.pojo.present.VO.login.LoginFirstVO;
import com.niulijie.ucenter.pojo.present.VO.login.LoginSecondVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginMapper {

    LoginFirstVO loginFirst(@Param("accountId") Integer accountId);

    Integer getAccountIdByAccessCode(@Param("accessCode") String accessCode);

    List<String> getRolelistByAccount(@Param("accountId") Integer accountId);

    LoginSecondVO loginSecond(@Param("accountId") Integer accountId, @Param("roleIds") String roleIds);
}
