package com.shebangs.supplier.data;

import java.util.ArrayList;
import java.util.List;

public class OrderQueryCondition {
    public String branch;           //分店
    public List<String> warehouse;  //库房
    public String goodsID;          //货号
    public List<String> goodsClass; //商品类型
    public OrderClass orderClass;   //下单类型
    public PrintStatus isPrint;     //是否打印
    public OrderStatus status;      //发货状态
    public String orderTime;        //下单时间
    public String shipmentTime;     //发货时间

    public OrderQueryCondition() {
        this.branch = "";
        this.warehouse = new ArrayList<>();
        this.goodsID = "";
        this.goodsClass = new ArrayList<>();
        this.orderClass = new OrderClass(OrderClass.NONE);
        this.isPrint = new PrintStatus(PrintStatus.NONE);
        this.status = new OrderStatus(OrderStatus.NONE);
        this.orderTime = "";
        this.shipmentTime = "";
    }

    public void clear() {
        branch = "";            //分店
        warehouse.clear();      //库房
        goodsID = "";           //货号
        orderClass.clear();     //下单类型
        goodsClass.clear();     //商品类型
        isPrint.clear();        //是否打印
        status.clear();         //发货状态
        orderTime = "";         //下单时间
        shipmentTime = "";      //发货时间
    }
}
