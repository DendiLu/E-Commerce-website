package com.qingcheng.dao;

import com.qingcheng.pojo.business.Ad;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdMapper extends Mapper<Ad> {
    @Select("select distinct position from tb_ad")
    public List<String> getAllAdPosition();
}
