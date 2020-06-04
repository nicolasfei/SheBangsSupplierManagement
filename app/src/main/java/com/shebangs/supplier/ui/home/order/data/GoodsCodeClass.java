package com.shebangs.supplier.ui.home.order.data;

/**
 * 货号类型
 */
public enum GoodsCodeClass {
    NORMAL("正常"),
    TRIAL_SALE("试卖"),
    AGENCY_SALE("代卖"),
    OTHER("特殊需求");

    private String type;

    GoodsCodeClass(String type) {
        this.type = type;
    }

    public static GoodsCodeClass myValueOf(String type) {
        switch (type) {
            case "正常":
                return NORMAL;
            case "试卖":
                return TRIAL_SALE;
            case "代卖":
                return AGENCY_SALE;
            case "特殊需求":
                return OTHER;

            default:
                return null;
        }
    }

    public String getType() {
        return type;
    }
}
