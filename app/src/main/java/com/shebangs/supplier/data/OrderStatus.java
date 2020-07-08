package com.shebangs.supplier.data;

import androidx.annotation.NonNull;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;

import java.util.ArrayList;

/**
 * 发货状态
 */
public class OrderStatus {
    public static final int SHIPMENT = 1;      //已发货
    public static final int UN_SHIPMENT = 2;   //未发货
    public static final int NONE = 0;

    private int type;
    private static final String[] value = {SupplierApp.getInstance().getString(R.string.shipment), SupplierApp.getInstance().getString(R.string.no_shipment)};

    public OrderStatus(int type) {
        this.type = type;
    }

    public static OrderStatus valueOf(String type) {
        for (int i = 0; i < value.length; i++) {
            if (type.equals(value[i])) {
                return new OrderStatus((int) Math.pow(2, i));
            }
        }
        return null;
    }

    public int getType() {
        return type;
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

    public static String[] getAllValue() {
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        switch (type) {
            case NONE:
                return "";
            case SHIPMENT:
                return value[0];
            case UN_SHIPMENT:
                return value[1];
        }
        return "";
    }

    public void clear() {
        this.type = 0;
    }
}
