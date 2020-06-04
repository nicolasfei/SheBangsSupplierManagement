package com.shebangs.supplier.hardware.printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrinterDeviceGroup {

    private String groupName;       //组名
    private int groupType;          //组类型
    private List<PrinterDevice> childrens;  //组员
    private boolean scanning;       //是否在扫描打印机
    private boolean isPortOpen;     //连接端口是否被打开

    public static final int SCAN_STATUS = 1;        //扫描状态
    public static final int PORT_STATUS = 2;        //端口状态
    public static final int CHILD_NUM = 3;          //组员数量

    public PrinterDeviceGroup(String groupName, int groupType) {
        this.groupName = groupName;
        this.groupType = groupType;
        if (this.childrens == null) {
            this.childrens = new ArrayList<>();
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public List<PrinterDevice> getChildrens() {
        return childrens;
    }

    public void addChildren(PrinterDevice child) {
        this.childrens.add(child);
    }

    public void addChildren(int pos, PrinterDevice child) {
        this.childrens.add(pos, child);
    }

    public void removeChild(long pos) {
        if (this.childrens.size() >= pos + 1) {
            this.childrens.remove(pos);
        }
    }

    public PrinterDevice getChild(int pos) {
        if (this.childrens.size() >= pos + 1) {
            return this.childrens.get(pos);
        }
        return null;
    }

    public int getChildCount() {
        return this.childrens.size();
    }

    public int getGroupType() {
        return groupType;
    }

    public void setScanning(boolean scanning) {
        this.scanning = scanning;
    }

    public boolean isScanning() {
        return scanning;
    }

    public void setPortOpen(boolean portOpen) {
        isPortOpen = portOpen;
        for (PrinterDevice d : childrens) {
            d.unlinkDevice();
        }
    }

    public boolean isPortOpen() {
        return isPortOpen;
    }

    public void clearChildrens() {
        this.getChildrens().clear();
    }

    /**
     * 清除child的状态--连接状态，连接中状态
     */
    public void clearChildStatus() {
        for (PrinterDevice d : childrens) {
            d.setLinkStatus(false);
            d.setLinking(false);
        }
    }

    /**
     * 排序，同步
     * 先比较连接状态，如果连接状态都是false，则比较连接次数
     */
    public void sync() {
        Collections.sort(childrens, new Comparator<PrinterDevice>() {
            @Override
            public int compare(PrinterDevice o1, PrinterDevice o2) {
                return o1.isLinking() ? -1 : (o2.isLinking() ? 1 : Integer.compare(o1.getLinkCount(), o2.getLinkCount()));
            }
        });
    }

    /**
     * 关闭组中已经连接的设备
     */
    public void closeDevice() {
        for (PrinterDevice device : childrens) {
            if (device.isLink()) {
                device.unlinkDevice();
                break;
            }
        }
    }
}
