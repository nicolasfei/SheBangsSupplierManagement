package com.shebangs.supplier.ui.home.order.data;

/**
 * 返货信息
 */
public class ReturnGoodsInformation {
    public String goodsClass;       //货物类型
    public String branchID;         //分店编号
    public int num;                 //返货数量
    public float price;             //返货价格
    public float totalPrice;        //返货总价格
    public String time;             //返货时间
    public String code;             //条码
    public boolean checked;         //是否审核
    public String checkTime;        //审核时间
    public String remark;           //备注
}
