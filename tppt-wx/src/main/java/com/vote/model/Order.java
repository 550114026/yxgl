package com.vote.model;

import com.jfinal.plugin.activerecord.Model;

public class Order extends Model<Order> {

    public static final Order dao = new Order();

    public Order findFirstByTradeNo(String out_trade_no){
        String select="select * from orders where out_trade_no=?";
        return findFirst(select, out_trade_no);
    }


}
