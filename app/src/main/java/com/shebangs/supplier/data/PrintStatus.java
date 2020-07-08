package com.shebangs.supplier.data;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;

import java.util.ArrayList;

public class PrintStatus {
    public static final int PRINT = 1;      //已打印
    public static final int UN_PRINT = 2;   //未打印
    public static final int NONE = 0;

    private int type;
    private final String[] value = {SupplierApp.getInstance().getString(R.string.printed), SupplierApp.getInstance().getString(R.string.no_print)};

    public PrintStatus(int type) {
        this.type = type;
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

    public String[] getAllValue() {
        return value;
    }

    public void setValue(int type, boolean isChecked) {
        switch (type) {
            case PRINT:
                if (isChecked) {
                    this.type |= 0x1;
                } else {
                    this.type &= 0x110;
                }
                break;
            case UN_PRINT:
                if (isChecked) {
                    this.type |= 0x10;
                } else {
                    this.type &= 0x101;
                }
                break;
            default:
                break;
        }
    }

    public void clear() {
        this.type = 0;
    }
}
