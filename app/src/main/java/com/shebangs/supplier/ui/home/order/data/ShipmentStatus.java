package com.shebangs.supplier.ui.home.order.data;

/**
 * 发货状态
 */
public enum ShipmentStatus {
    SHIPMENT("已发货"),
    UN_SHIPMENT("未发货");

    private String status;

    ShipmentStatus(String status) {
        this.status = status;
    }

    public static ShipmentStatus myValueOf(String status) {
        switch (status) {
            case "已发货":
                return SHIPMENT;
            case "未发货":
                return UN_SHIPMENT;
            default:
                return null;
        }
    }

    public String getStatus() {
        return status;
    }
}
