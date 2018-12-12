package com.huarui.entity;

/**
 * 秒杀商品实体类
 * @author  lihui
 */
public class Seckill {

    private long seckillId;
    private String name;

    public Seckill(){

    }

    public Seckill(String name, long seckillId) {
        this.name = name;
        this.seckillId = seckillId;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Seckill{" +
                "seckillId=" + seckillId +
                ", name='" + name + '\'' +
                '}';
    }
}