package com.shebangs.supplier.ui.home.order.data;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;

import java.util.ArrayList;

/**
 * 发货状态
 */
public class ShipmentStatus {
    public static final int SHIPMENT = 1;      //已发货
    public static final int UN_SHIPMENT = 2;   //未发货
    public static final int NONE = 0;

    private int type;
    private static final String[] value = {SupplierApp.getInstance().getString(R.string.shipment), SupplierApp.getInstance().getString(R.string.no_shipment)};

    public ShipmentStatus(int type) {
        this.type = type;
    }

    public static ShipmentStatus valueOf(String type) {
        for (int i = 0; i < value.length; i++) {
            if (type.equals(value[i])) {
                return new ShipmentStatus((int) Math.pow(2, i));
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

    public void clear() {
        this.type = 0;
    }
}
