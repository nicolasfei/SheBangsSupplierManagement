package com.shebangs.supplier.ui.home.order.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单信息类
 */
public class OrderInformation implements Parcelable {

    public static final String sort_order_time = SupplierApp.getInstance().getString(R.string.sort_order_time);
    public static final String sort_shipment_time = SupplierApp.getInstance().getString(R.string.sort_shipment_time);
    public static final String sort_branch = SupplierApp.getInstance().getString(R.string.sort_branch);
    public static final String sort_warehouse = SupplierApp.getInstance().getString(R.string.sort_warehouse);
    public static final String sort_order_class = SupplierApp.getInstance().getString(R.string.sort_order_class);
    public static final String sort_order_num_rise = SupplierApp.getInstance().getString(R.string.sort_order_num_rise);
    public static final String sort_order_num_drop = SupplierApp.getInstance().getString(R.string.sort_order_num_drop);

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
    public boolean canChecked;       //是否显示可选择checkbox
    public List<OrderGoodsAttribute> goodsAttributes;       //商品属性
    public boolean checked;         //是否被选中
    public boolean expansion;       //是否展开显示细节

    public OrderInformation(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
    }

    public OrderInformation() {
    }

    public static OrderInformation getTestData() {
        OrderInformation order = new OrderInformation();
        order.branch = "测试分店1";       //分店
        order.warehouse = "测试库房1";    //库房
        order.goodsID = "9527";      //货号
        order.newGoodsID = "001122F";   //新货号
        order.bid = 25.5f;           //进价
        order.goodsNum = 1000;        //发货数量
        order.orderClass = new OrderClass(OrderClass.ALL);   //下单类型
        order.orderTime = "2020-03-06 12:30";        //下单时间
        order.invalidTime = "2020-03-08 12:30";      //截至时间
        order.isPrint = false;         //是否打印
        order.printTime = "";        //打印时间
        order.status = new ShipmentStatus(ShipmentStatus.UN_SHIPMENT);   //发货状态
        order.shipmentTime = "";     //发货时间
        order.remark = "测试备注，测试备注";           //备注
        order.canChecked = false;       //是否显示可选择checkbox
        order.goodsAttributes = new ArrayList<>();       //商品属性
        OrderGoodsAttribute attr1 = new OrderGoodsAttribute();
        attr1.color = "红色";
        attr1.size = "M";
        attr1.num = 300;
        order.goodsAttributes.add(attr1);
        OrderGoodsAttribute attr2 = new OrderGoodsAttribute();
        attr2.color = "粉色";
        attr2.size = "S";
        attr2.num = 300;
        order.goodsAttributes.add(attr2);
        OrderGoodsAttribute attr3 = new OrderGoodsAttribute();
        attr3.color = "灰色";
        attr3.size = "M";
        attr3.num = 400;
        order.goodsAttributes.add(attr3);
        order.checked = false;         //是否被选中
        order.expansion = false;    //默认不展开
        return order;
    }

    protected OrderInformation(Parcel in) {
        branch = in.readString();
        warehouse = in.readString();
        goodsID = in.readString();
        newGoodsID = in.readString();
        bid = in.readFloat();
        goodsNum = in.readInt();
        orderTime = in.readString();
        invalidTime = in.readString();
        isPrint = in.readByte() != 0;
        printTime = in.readString();
        shipmentTime = in.readString();
        remark = in.readString();
        canChecked = in.readByte() != 0;
        checked = in.readByte() != 0;
    }

    public static final Creator<OrderInformation> CREATOR = new Creator<OrderInformation>() {
        @Override
        public OrderInformation createFromParcel(Parcel in) {
            return new OrderInformation(in);
        }

        @Override
        public OrderInformation[] newArray(int size) {
            return new OrderInformation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(branch);
        dest.writeString(warehouse);
        dest.writeString(goodsID);
        dest.writeString(newGoodsID);
        dest.writeFloat(bid);
        dest.writeInt(goodsNum);
        dest.writeString(orderTime);
        dest.writeString(invalidTime);
        dest.writeByte((byte) (isPrint ? 1 : 0));
        dest.writeString(printTime);
        dest.writeString(shipmentTime);
        dest.writeString(remark);
        dest.writeByte((byte) (canChecked ? 1 : 0));
        dest.writeByte((byte) (checked ? 1 : 0));
    }
}
