package com.shebangs.supplier.hardware.printer.bluetooth;

import com.shebangs.supplier.hardware.printer.PrinterDevice;

public class BluePrinterDevice extends PrinterDevice {

    private static final String TAG = "BluePrinterDevice";

    public BluePrinterDevice(int linkType, int printerType, Object deviceAttr, String deviceName) {
        super(linkType, printerType, deviceAttr, deviceName);
    }

    @Override
    public void linkDevice() {
        //打开端口
        ThreadPool threadPool = ThreadPool.getInstantiation();
        threadPool.addSerialTask(new Runnable() {
            @Override
            public void run() {
                DeviceConnFactoryManager[] managers = DeviceConnFactoryManager.getDeviceConnFactoryManagers();
                for (int i = 0; i < managers.length; i++) {
                    if (managers[i] == null || managers[i].mPort == null) {
                        DeviceConnFactoryManager manager = new DeviceConnFactoryManager.Build()
                                .setId(i)
                                //设置连接方式
                                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                                //设置连接的蓝牙mac地址
                                .setMacAddress(getAddress())
                                .build();
                        manager.openPort();
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void unlinkDevice() {
        /**
         * 重新连接回收上次连接的对象，避免内存泄漏
         */
        DeviceConnFactoryManager[] managers = DeviceConnFactoryManager.getDeviceConnFactoryManagers();
        String address = getAddress();
        for (DeviceConnFactoryManager m : managers) {
            if (m != null && m.getConnState()) {
                if (m.getMacAddress().equals(address)) {
                    m.mPort.closePort();
                    break;
                }
            }
        }
    }

    @Override
    public void checkDeviceIsLinked() {
        DeviceConnFactoryManager[] managers = DeviceConnFactoryManager.getDeviceConnFactoryManagers();
        String address = getAddress();
        for (DeviceConnFactoryManager m : managers) {
            if (m != null && m.mPort != null && m.getConnState() && m.getMacAddress().equals(address)) {
                super.setLinkStatus(true);
                break;
            }
        }
    }

    @Override
    public String getAddress() {
        return (String) super.getDeviceAttr();
    }
}
