package com.shebangs.supplier.ui.home.order.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单商品属性
 */
public class OrderGoodsAttribute implements Parcelable {
    public String color;        //颜色
    public String size;         //尺码
    public int num;             //件数

    public OrderGoodsAttribute() {

    }

    protected OrderGoodsAttribute(Parcel in) {
        color = in.readString();
        size = in.readString();
        num = in.readInt();
    }

    public static final Creator<OrderGoodsAttribute> CREATOR = new Creator<OrderGoodsAttribute>() {
        @Override
        public OrderGoodsAttribute createFromParcel(Parcel in) {
            return new OrderGoodsAttribute(in);
        }

        @Override
        public OrderGoodsAttribute[] newArray(int size) {
            return new OrderGoodsAttribute[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        dest.writeString(size);
        dest.writeInt(num);
    }
}
