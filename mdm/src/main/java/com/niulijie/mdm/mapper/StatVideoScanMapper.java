package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.niulijie.mdm.dto.response.VideoSkimUserInfo;
import com.niulijie.mdm.entity.StatVideoScan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (StatVideoScan)表数据库访问层
 *
 * @author dufa
 * @since 2022-07-14 10:05:35
 */
 @Mapper
public interface StatVideoScanMapper extends BaseMapper<StatVideoScan>{

    IPage<VideoSkimUserInfo> selectPageInfo(@Param("page") IPage<VideoSkimUserInfo> page, @Param("videoIds") List<Integer> videoIds,
                                            @Param("searchKey") String searchKey, @Param("deptIds") List<Integer> deptIds);

    void insertList(@Param("statVideoScanList") List<StatVideoScan> statVideoScanList);

    void updateList(@Param("list") List<StatVideoScan> updateStat);
}
