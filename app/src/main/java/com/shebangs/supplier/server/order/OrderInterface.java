package com.shebangs.supplier.server.order;

import com.shebangs.supplier.server.AbstractInterface;

public abstract class OrderInterface extends AbstractInterface {
    //订单查询接口
    public final static String OrderQuery = AbstractInterface.COMMAND_URL+"api/order/query";
    //订单接单---这里是订单打印
    public final static String OrderHandler = AbstractInterface.COMMAND_URL+"api/order/handler";
}
