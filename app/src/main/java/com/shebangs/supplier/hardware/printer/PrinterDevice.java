package com.shebangs.supplier.hardware.printer;

/**
 * 描述一个打印机设备
 */
public abstract class PrinterDevice {

    //属性
    private int linkType;                   //连接方式--蓝牙连接，WiFi连接
    private int printerType;                //打印机类型--票据打印机，条码打印机
    private Object deviceAttr;              //蓝牙打印机，WiFi打印机
    private boolean linkStatus = false;     //连接状态
    private boolean linking = false;        //连接中。。。
    private int linkCount = 0;              //设备被连接次数
    private String deviceName;              //设备名
    private String alias;                   //别名

    public static final int LINKED_STATUS = 1;      //连接状态
    public static final int LINKING_STATUS = 2;     //连接中状态

    /**
     * @link printerType
     */
    public static final int UNKNOWN = 0;    //未知
    /**
     * @link linkType
     * 蓝牙连接方式
     */
    public static final int LINK_TYPE_BLUETOOTH = 1;
    /**
     * @link linkType
     * WiFi连接方式
     */
    public static final int LINK_TYPE_WIFI = 2;
    /**
     * @link printerType
     * 票据打印机
     */
    public static final int PRINTER_TYPE_BILL = 1;
    /**
     * @link printerType
     * 条码打印机
     */
    public static final int PRINTER_TYPE_CODE = 2;

    public PrinterDevice(int linkType, int printerType, Object deviceAttr, String deviceName) {
        this.linkType = linkType;
        this.printerType = printerType;
        this.deviceAttr = deviceAttr;
        this.deviceName = deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceAlias(String alias) {
        this.alias = alias;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getAlias() {
        return alias;
    }

    public void setLinkStatus(boolean linkStatus) {
        this.linkStatus = linkStatus;
    }

    /**
     * 设置打印类型
     *
     * @param printerType 票据/标签
     */
    public void setPrinterType(int printerType) {
        this.printerType = printerType;
    }

    /**
     * 设备是否已经连接
     *
     * @return yes/no
     */
    public boolean isLink() {
        return linkStatus;
    }

    /**
     * 获取打印机类型
     *
     * @return 票据/标签
     */
    public int getPrinterType() {
        return printerType;
    }

    /**
     * 获取设备的属性
     *
     * @return 属性
     */
    public Object getDeviceAttr() {
        return deviceAttr;
    }

    /**
     * 获取设备连接类型
     *
     * @return 连接类型
     */
    public int getLinkType() {
        return linkType;
    }


    /**
     * 设置设备是否正在连接
     *
     * @param linking yes/no
     */
    public void setLinking(boolean linking) {
        this.linking = linking;
    }

    /**
     * 设备是否正在连接
     *
     * @return yes/no
     */
    public boolean isLinking() {
        return linking;
    }

    /**
     * 连接设备
     */
    public abstract void linkDevice();

    /**
     * 断开连接
     */
    public abstract void unlinkDevice();

    /**
     * 检测设备是否已经连接
     */
    public abstract void checkDeviceIsLinked();

    /**
     * 获取设备地址，由子类实现
     *
     * @return 标识设备的唯一地址
     */
    public abstract String getAddress();

    /**
     * 设置连接次数
     *
     * @param count 连接次数
     */
    public void setLinkCount(int count) {
        this.linkCount = count;
    }

    /**
     * 获取连接次数
     *
     * @return 连接次数
     */
    public int getLinkCount() {
        return linkCount;
    }

    /**
     * 增加连接次数
     */
    public void addLinkCount() {
        this.linkCount++;
    }
}
