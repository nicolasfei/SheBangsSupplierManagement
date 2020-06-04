package com.shebangs.supplier.ui.home.order.data;

public class OrderQueryCondition {
    public String branch;       //分店
    public String warehouse;    //库房
    public String goodsID;      //货号
    public OrderClass orderClass;   //下单类型
    public boolean isPrint;         //是否打印
    public ShipmentStatus status;   //发货状态
    public String orderTime;        //下单时间
    public String shipmentTime;     //发货时间
}
