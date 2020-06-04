package com.shebangs.supplier.ui.home.order.data;

/**
 * 订单类型
 */
public enum OrderClass {
    ALL("all"),             //统下单
    FIRST("first"),         //首单
    CPFR("cprf");           //补货

    private String type;

    OrderClass(String type) {
        this.type = type;
    }

    public static OrderClass myValueOf(String type) {
        switch (type) {
            case "all":
                return ALL;
            case "first":
                return FIRST;
            case "cprf":
                return CPFR;
            default:
                return null;
        }
    }

    public String getType() {
        return type;
    }
}
