package com.shebangs.supplier.ui.home.order.data;

public class OrderQueryCondition {
    public String branch;       //分店
    public String warehouse;    //库房
    public String goodsID;      //货号
    public OrderClass orderClass;   //下单类型
    public PrintStatus isPrint;     //是否打印
    public ShipmentStatus status;   //发货状态
    public String orderTime;        //下单时间
    public String shipmentTime;     //发货时间

    public OrderQueryCondition() {
        this.branch = "";
        this.warehouse = "";
        this.goodsID = "";
        this.orderClass = new OrderClass(OrderClass.NONE);
        this.isPrint = new PrintStatus(PrintStatus.NONE);
        this.status = new ShipmentStatus(ShipmentStatus.NONE);
        this.orderTime = "";
        this.shipmentTime = "";
    }

    public void clear() {
        branch = "";            //分店
        warehouse = "";         //库房
        goodsID = "";           //货号
        orderClass.clear();     //下单类型
        isPrint.clear();        //是否打印
        status.clear();         //发货状态
        orderTime = "";         //下单时间
        shipmentTime = "";      //发货时间
    }
}
