package com.uestc.dao;

import com.uestc.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SeckillUserDao {

    @Select("SELECT * FROM seckill_user WHERE id = #{id}")
    public SeckillUser getById(@Param("id")long id);
}
