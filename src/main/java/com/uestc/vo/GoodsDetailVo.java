package com.uestc.vo;

import com.uestc.domain.SeckillUser;

public class GoodsDetailVo {

    private int seckillStatu;

    private long remainSeconds;

    private GoodsVo goodsVo;

    private SeckillUser seckillUser;

    public int getSeckillStatu() {
        return seckillStatu;
    }

    public void setSeckillStatu(int seckillStatu) {
        this.seckillStatu = seckillStatu;
    }

    public long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public SeckillUser getSeckillUser() {
        return seckillUser;
    }

    public void setSeckillUser(SeckillUser seckillUser) {
        this.seckillUser = seckillUser;
    }
}
