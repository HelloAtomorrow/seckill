package com.uestc.service;

import com.uestc.dao.GoodsDao;
import com.uestc.domain.Goods;
import com.uestc.domain.SecKillGoods;
import com.uestc.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<Goods> listGoods() {
        return goodsDao.listGoods();
    }

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goodsVo) {
        SecKillGoods secKillGoods = new SecKillGoods();
        secKillGoods.setGoodId(goodsVo.getId());
        goodsDao.reduceStock(secKillGoods);
    }
}
