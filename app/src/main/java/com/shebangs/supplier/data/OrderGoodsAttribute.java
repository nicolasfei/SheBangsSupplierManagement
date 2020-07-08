package com.shebangs.supplier.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单商品属性
 */
public class OrderGoodsAttribute implements Parcelable {
    public String color;        //下单颜色
    public String size;         //下单尺码
    public int num;             //下单件数

    public String actualColor;        //实际颜色
    public String actualSize;         //实际尺码
    public int actualNum;             //实际件数

    public OrderGoodsAttribute(String color, String size, int num) {
        this.color = color;
        this.size = size;
        this.num = num;

        this.actualColor = color;
        this.actualSize = size;
        this.actualNum = num;
    }

    protected OrderGoodsAttribute(Parcel in) {
        color = in.readString();
        size = in.readString();
        num = in.readInt();
        actualColor = in.readString();
        actualSize = in.readString();
        actualNum = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        dest.writeString(size);
        dest.writeInt(num);
        dest.writeString(actualColor);
        dest.writeString(actualSize);
        dest.writeInt(actualNum);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
