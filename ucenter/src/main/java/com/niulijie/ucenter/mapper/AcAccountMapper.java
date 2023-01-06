package com.niulijie.ucenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.niulijie.ucenter.pojo.entity.AcAccount;
import com.niulijie.ucenter.pojo.present.AO.account.AcAccountListAO;
import com.niulijie.ucenter.pojo.present.VO.account.AcAccountListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcAccountMapper extends BaseMapper<AcAccount> {
    int add(@Param("acAccount") AcAccount acAccount);

    IPage<AcAccountListVO> list(IPage<AcAccountListVO> page, @Param("acAccountListAO") AcAccountListAO acAccountListAO, @Param("deptIds") List<Integer> deptIds);

    List<AcAccountListVO> list(@Param("acAccountListAO") AcAccountListAO acAccountListAO, @Param("deptIds") List<Integer> deptIds);

    List<AcAccountListVO> getAccountBatch(@Param("accountIds") String accountIds);

    //AcAccountListVO getAccountByID(@Param("acAccountGetAO") AcAccountGetAO acAccountGetAO);

    int batchAddAccountDept(@Param("deptList") List<Integer> deptList,@Param("accountId") Integer accountId);

    int batchAddAccountRole(@Param("roleList") List<Integer> roleList,@Param("accountId") Integer accountId, @Param("creator") Integer creator);

    int batchAddAccountGroup(@Param("groupList") List<Integer> groupList,@Param("accountId") Integer accountId, @Param("creator") Integer creator);
}
