package com.uestc.dao;

import com.uestc.domain.Goods;
import com.uestc.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("SELECT * FROM goods")
    public List<Goods> listGoods();

    @Select("SELECT g.*, sg.seckill_price, sg.stock_count, sg.start_date, sg.end_date " +
            "FROM seckill_goods sg " +
            "LEFT JOIN goods g " +
            "ON sg.good_id = g.id")
    public List<GoodsVo> listGoodsVo();
}
