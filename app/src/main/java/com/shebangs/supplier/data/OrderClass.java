package com.shebangs.supplier.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;

import java.util.ArrayList;

/**
 * 订单类型
 */
public class OrderClass implements Parcelable {
    public static final int ALL = 1;        //统下单
    public static final int FIRST = 2;      //首单
    public static final int CPFR = 4;       //补货
    public static final int NONE = 0;

    private int type;
    private static final String[] value = {SupplierApp.getInstance().getString(R.string.order_all), SupplierApp.getInstance().getString(R.string.order_first),
            SupplierApp.getInstance().getString(R.string.order_CPFR)};

    public OrderClass(int type) {
        this.type = type;
    }

    protected OrderClass(Parcel in) {
        type = in.readInt();
    }

    public static final Creator<OrderClass> CREATOR = new Creator<OrderClass>() {
        @Override
        public OrderClass createFromParcel(Parcel in) {
            return new OrderClass(in);
        }

        @Override
        public OrderClass[] newArray(int size) {
            return new OrderClass[size];
        }
    };

    public static OrderClass valueOf(String type) {
        switch (type) {
            case "all":
                return new OrderClass(ALL);
            case "first":
                return new OrderClass(FIRST);
            case "cprf":
                return new OrderClass(CPFR);
            default:
                return null;
        }
    }

    public String[] getTypes() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if ((type >> i & 1) == 1) {
                list.add(value[i]);
            }
        }
        return (String[]) list.toArray();
    }

    public int getType() {
        return type;
    }

    public static String[] getAllValue() {
        return value;
    }

    public void setValue(int type, boolean isChecked) {
        switch (type) {
            case ALL:
                if (isChecked) {
                    this.type |= 0x1;
                } else {
                    this.type &= 0x110;
                }
                break;
            case FIRST:
                if (isChecked) {
                    this.type |= 0x10;
                } else {
                    this.type &= 0x101;
                }
                break;
            case CPFR:
                if (isChecked) {
                    this.type |= 0x100;
                } else {
                    this.type &= 0x011;
                }
                break;
        }
    }

    public void clear() {
        this.type = 0;
    }

    @NonNull
    @Override
    public String toString() {
        switch (type) {
            case ALL:
                return value[0];
            case FIRST:
                return value[1];
            case CPFR:
                return value[2];
        }
        return "null";
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
    }
}
