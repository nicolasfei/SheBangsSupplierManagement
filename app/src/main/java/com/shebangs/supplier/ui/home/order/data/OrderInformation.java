package com.shebangs.supplier.ui.home.order.data;

import java.util.List;

/**
 * 订单信息类
 */
public class OrderInformation {
    public String branch;       //分店
    public String warehouse;    //库房
    public String goodsID;      //货号
    public String newGoodsID;   //新货号
    public float bid;           //进价
    public int goodsNum;        //发货数量
    public OrderClass orderClass;   //下单类型
    public String orderTime;        //下单时间
    public String invalidTime;      //截至时间
    public boolean isPrint;         //是否打印
    public String printTime;        //打印时间
    public ShipmentStatus status;   //发货状态
    public String shipmentTime;     //发货时间
    public String remark;           //备注
    public List<OrderGoodsAttribute> goodsAttributes;       //商品属性
}
