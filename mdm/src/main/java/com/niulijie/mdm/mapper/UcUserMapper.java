package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niulijie.mdm.dto.response.BlacklistDetailResp;
import com.niulijie.mdm.dto.response.UserDetailResp;
import com.niulijie.mdm.entity.StatVideoScan;
import com.niulijie.mdm.entity.UcUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 人员信息表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Mapper
public interface UcUserMapper extends BaseMapper<UcUser> {

    Integer updateCollectCountById(@Param("userId") Integer userId, @Param("value") Integer value);

    List<Integer> selectUserIds(String searchKey);

    /**
     *  点赞数统计 +1 操作
     */
    void likeCountIncrease(@Param("publisher") Integer publisher);

    /**
     *  点赞数统计 -1 操作
     */
    void likeCountReduce(@Param("publisher") Integer publisher);

    /**
     * 查询用户详细信息
     * @param publisherList
     * @return
     */
    List<UserDetailResp> getUserDetailById(@Param("publisherList") List<Integer> publisherList);

    /**
     * 查询用户详细信息
     * @param publisherList
     * @return
     */
    List<UserDetailResp> getUserDetailByIds(@Param("publisherList") Set<Integer> publisherList);

    /**
     * 作品数统计 操作
     * @param publisherList
     */
    void videoCountIncrease(@Param("publisherList") Map<Integer, Long> publisherList);

    /**
     * 作品数统计 操作
     * @param publisherList
     */
    void videoCountReduce(@Param("publisherList") Map<Integer, Long> publisherList);

    /**
     * 批量更新操作
     * @param updateStat
     */
    void updateList(@Param("list") List<StatVideoScan> updateStat);

    /**
     * 用户黑名单-查询
     * @param page
     * @param searchKey
     * @return
     */
    IPage<BlacklistDetailResp> selectPageList(@Param("page") Page<BlacklistDetailResp> page, @Param("searchKey") String searchKey);

    List<Integer> selectInfoList(String searchKey);

    List<String> getUserById(Integer publisher);
}
