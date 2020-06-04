package com.shebangs.supplier.hardware.printer;

public abstract class PrinterBusiness {
    private PrinterDeviceGroup printerGroup;        //打印机组
    private String printerGroupName;                //打印机组名
    private int printerGroupType;                   //打印机组类型
    protected PrinterDeviceGroup bluePrinterGroup;  //蓝牙打印机组

    public PrinterBusiness() {
    }

    public PrinterDeviceGroup getBluePrinterGroup() {
        return bluePrinterGroup;
    }

    /**
     * 初始化
     * 初始化打印机组
     * 初始化已经连接过的设备到printerGroup
     */
    protected abstract void initialization();

    /**
     * 扫描打印机
     */
    protected abstract void scanPrinter();

    /**
     * 设置接口状态
     *
     * @param status true/false
     */
    protected abstract void setInterfaceStatus(boolean status);

    /**
     * 连接打印机
     *
     * @param position 要连接打印的position
     */
    protected abstract void linkPrinter(int position);

    /**
     * 更新打印机连接状态
     *
     * @param address  Address
     * @param isLinked 连接状态
     */
    protected abstract void updatePrinterStatus(String address, boolean isLinked);

    /**
     * 更新打印机属性
     *
     * @param address     Address
     * @param printerType 打印机类型
     * @param alias       别名
     */
    protected abstract void updatePrinterAttr(String address, int printerType, String alias);

    /**
     * 更新连接中状态
     *
     * @param address   地址
     * @param isLinking linking
     */
    protected abstract void updatePrinterLinking(String address, boolean isLinking);

    /**
     * 自动连接
     */
    protected abstract void autoLinkTaskStart();

    /**
     * 注销
     */
    protected abstract void close();

}
