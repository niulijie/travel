package com.atguigu.admin.service;

import com.atguigu.admin.bean.DaPrLocationDict;
import com.atguigu.admin.mapper.DaPrLocationDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaPrLocationDictService {

    @Autowired
    DaPrLocationDictMapper daPrLocationDictMapper;

    public DaPrLocationDict getDaPrLocationDict(Integer id) {
        return daPrLocationDictMapper.getById(id);
    }
}
