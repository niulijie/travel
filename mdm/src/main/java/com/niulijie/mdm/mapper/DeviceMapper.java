package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.niulijie.mdm.dto.response.DeviceByGroupExport;
import com.niulijie.mdm.entity.Device;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhoupengbing
 * @packageName com.niulijie.mdm.mapper
 * @email zhoupengbing@telecomyt.com.cn
 * @description
 * @createTime 2020年02月07日 10:36:00 @Version v1.0
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {


    /**
     * 获取已经注册完设备的用户列表
     *
     * @return
     */
    @Select("select distinct user_id from device_info where status in (1,3)")
    List<Integer> selectUserIdList();

    /**
     * 获取设备的证书时效时间
     *
     * @param imei 设备ID
     * @return
     */
    @Select("select expired_time from cred_info where imei = #{imei} and status = 1")
    Date selectCertExpiredTime(@Param("imei") String imei);

    /**
     * 根据条件查询设备并指定固定的列
     */
     IPage<Map<String,Object>> deviceChooseField (
            @Param("page") IPage<Map<String,Object>> page,
            @Param("userIds") List<Integer> userIds,
            @Param("phone") String phone,
            @Param("name") String name,
            @Param("status")  Integer status,
            @Param("imei")  String imei,
            @Param("fieldName")  String fieldName);

    List<DeviceByGroupExport> deviceAllExportByGroup(@Param("status") List<Integer> status, @Param("userIds")List<Integer> userIds);
}
