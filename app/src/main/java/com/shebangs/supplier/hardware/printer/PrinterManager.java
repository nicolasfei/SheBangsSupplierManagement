package com.shebangs.supplier.hardware.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;
import com.shebangs.supplier.hardware.printer.bluetooth.BluePrinterDevice;
import com.shebangs.supplier.hardware.printer.bluetooth.DeviceConnFactoryManager;
import com.shebangs.supplier.hardware.printer.bluetooth.PrinterCommand;
import com.shebangs.supplier.hardware.printer.bluetooth.ThreadPool;
import com.shebangs.supplier.tool.Utils;
import com.shebangs.supplier.ui.device.printer.PrintContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.bluetooth.BluetoothClass.Device.Major.IMAGING;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.shebangs.supplier.hardware.printer.PrinterDevice.LINK_TYPE_BLUETOOTH;
import static com.shebangs.supplier.hardware.printer.bluetooth.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;

/**
 * 打印机设备的管理类
 * 打印机名字管理
 * 管理已经连接的打印机
 * 自动连接打印机
 */
public class PrinterManager {

    private static final String TAG = "PrinterManager";
    private static PrinterManager manager = new PrinterManager();

    //打印机组
    private List<PrinterDeviceGroup> printerGroup;

    //蓝牙打印机
    private PrinterBusiness blueToothPrinterBusiness;

    //打印机监听
    private OnPrinterStatusUpdateListener listener;

    //打印机引用
    private DeviceConnFactoryManager escManager = null;
    private DeviceConnFactoryManager tscManager = null;

    /**
     * 读取已经连接过的打印机
     * 自动连接到打印机（连接次数最多的打印机）
     */
    private PrinterManager() {
        this.printerGroup = new ArrayList<>();
        //初始化蓝牙业务模块
        this.blueToothPrinterBusiness = new BlueToothPrinterBusiness();
        this.blueToothPrinterBusiness.initialization();
        this.printerGroup.add(this.blueToothPrinterBusiness.getBluePrinterGroup());
        //初始化WiFi业务模块

        //注册打印机接口监听
        registerPrinterInterfaceListener();
        //开启自动连接任务
        this.blueToothPrinterBusiness.autoLinkTaskStart();
    }

    public static PrinterManager getInstance() {
        return manager;
    }

    /**
     * 注册打印机接口监听
     */
    private void registerPrinterInterfaceListener() {
        //注册打印机连接管理广播
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(ACTION_USB_DEVICE_DETACHED);//USB线拔出
        filter2.addAction(ACTION_QUERY_PRINTER_STATE);//查询打印机缓冲区状态广播，用于一票一控
        filter2.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);//与打印机连接状态
        filter2.addAction(ACTION_USB_DEVICE_ATTACHED);//USB线插入
        SupplierApp.getInstance().registerReceiver(this.mLinkPrinterReceiver, filter2);
    }

    /**
     * 获取打印机组
     *
     * @return 蓝牙打印机组
     */
    public List<PrinterDeviceGroup> getPrinterGroup() {
        return printerGroup;
    }

    /**
     * 扫描打印机
     */
    public void scanPrinter(int type) {
        switch (type) {
            case LINK_TYPE_BLUETOOTH:
                this.blueToothPrinterBusiness.scanPrinter();
                break;
            case PrinterDevice.LINK_TYPE_WIFI:
                break;
            default:
                break;
        }
    }

    /**
     * 设置打印机组接口状态
     *
     * @param groupPosition groupPosition
     * @param status        true/false
     */
    public void setPrinterGroupInterface(int groupPosition, boolean status) {
        switch (printerGroup.get(groupPosition).getGroupType()) {
            case LINK_TYPE_BLUETOOTH:
                blueToothPrinterBusiness.setInterfaceStatus(status);
                break;
            case PrinterDevice.LINK_TYPE_WIFI:
                break;
            default:
                break;
        }
    }

    /**
     * 连接打印机
     *
     * @param groupPosition 打印机组Position
     * @param childPosition 打印机Position
     */
    public void linkPrinter(int groupPosition, int childPosition) {
        switch (printerGroup.get(groupPosition).getGroupType()) {
            case LINK_TYPE_BLUETOOTH:
                blueToothPrinterBusiness.linkPrinter(childPosition);
                break;
            case PrinterDevice.LINK_TYPE_WIFI:
                break;
            default:
                break;
        }
    }

    /**
     * 自动连接打印机任务
     */
    private Runnable autoLinkTask = new Runnable() {
        @Override
        public void run() {

        }
    };

    /**
     * 打印收银小票
     *
     * @param information 收银小票信息
     * @throws IOException 打印机异常
     */
//    public void printSaleBill(SaleGoodsInformation information) throws IOException {
//        escManager = null;
//        for (DeviceConnFactoryManager item : DeviceConnFactoryManager.getDeviceConnFactoryManagers()) {
//            if (item != null && item.getConnState() && item.getCurrentPrinterCommand() == PrinterCommand.ESC) {
//                escManager = item;
//                break;
//            }
//        }
//        if (escManager != null) {
//            ThreadPool threadPool = ThreadPool.getInstantiation();
//            threadPool.addSerialTask(new Runnable() {
//                @Override
//                public void run() {
//                    escManager.sendDataImmediately(PrintContent.getSaleReceipt(information));
//                }
//            });
//        } else {
//            throw new IOException(SupplierApp.getInstance().getString(R.string.str_bill_printer_no_conn));
//        }
//    }

    /**
     * 打印标签（条码）
     *
     * @param label 条码信息
     * @throws IOException 打印机异常
     */
    public void printSaleLabel(final String label) throws IOException {
        tscManager = null;
        for (DeviceConnFactoryManager item : DeviceConnFactoryManager.getDeviceConnFactoryManagers()) {
            if (item != null && item.getConnState() && item.getCurrentPrinterCommand() == PrinterCommand.TSC) {
                tscManager = item;
                break;
            }
        }
        if (tscManager != null) {
            ThreadPool threadPool = ThreadPool.getInstantiation();
            threadPool.addSerialTask(new Runnable() {
                @Override
                public void run() {
                    tscManager.sendDataImmediately(PrintContent.getLabel(label));
                }
            });
        } else {
            throw new IOException(SupplierApp.getInstance().getString(R.string.str_code_printer_no_conn));
        }
    }

    /**
     * 连接打印机状态广播
     */
    private BroadcastReceiver mLinkPrinterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);
            switch (action) {
//                //USB请求访问权限
//                case ACTION_USB_PERMISSION:
//                    synchronized (this) {
//                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                            if (device != null) {//用户点击授权
//                                usbConn(device);
//                            }
//                        } else {//用户点击不授权,则无权限访问USB
//                            Log.e(TAG,"No access to USB");
//                        }
//                    }
//                    break;
//                //Usb连接断开广播
//                case ACTION_USB_DEVICE_DETACHED:
//                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (usbDevice.equals( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].usbDevice())){
//                        mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
//                    }
//                    break;
                //连接状态
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    String linkedAddress = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[deviceId].getMacAddress();
                    String devName = intent.getStringExtra(DeviceConnFactoryManager.DEVICE_NAME);
                    PrinterCommand command = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[deviceId].getCurrentPrinterCommand();
                    DeviceConnFactoryManager.CONN_METHOD connM = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[deviceId].connMethod;
                    Log.d(TAG, "---------------------------------------------onReceive: state is " + state);
                    switch (connM) {
                        case BLUETOOTH:
                            switch (state) {
                                case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                                    Log.d(TAG, "-------onReceive: DeviceConnFactoryManager.CONN_STATE_DISCONNECT:" + devName + ":" + linkedAddress + "````" + deviceId + "--" + command);
                                    blueToothPrinterBusiness.updatePrinterStatus(linkedAddress, false);
                                    break;
                                case DeviceConnFactoryManager.CONN_STATE_FAILED:
                                    Log.d(TAG, "-------onReceive: DeviceConnFactoryManager.CONN_STATE_FAILED" + devName + ":" + linkedAddress + "````" + deviceId + "--" + command);
                                    blueToothPrinterBusiness.updatePrinterStatus(linkedAddress, false);
                                    break;
                                case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                                    Log.d(TAG, "-------onReceive: DeviceConnFactoryManager.CONN_STATE_CONNECTING " + devName + ":" + linkedAddress + "````" + deviceId + "--" + command);
                                    blueToothPrinterBusiness.updatePrinterLinking(linkedAddress, true);
                                    break;
                                case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                                    Log.d(TAG, "-------onReceive: DeviceConnFactoryManager.CONN_STATE_CONNECTED" + devName + ":" + linkedAddress + "````" + deviceId + "--" + command);
                                    blueToothPrinterBusiness.updatePrinterAttr(linkedAddress, command == PrinterCommand.ESC ? PrinterDevice.PRINTER_TYPE_CODE : PrinterDevice.PRINTER_TYPE_BILL,
                                            intent.getStringExtra(DeviceConnFactoryManager.DEVICE_NAME));
                                    blueToothPrinterBusiness.updatePrinterStatus(linkedAddress, true);
                                    break;
                                default:
                                    break;
                            }
                        case WIFI:
                        case USB:
                        case SERIAL_PORT:
                            break;
                        default:
                            break;
                    }
                    break;
//                //连续打印，一票一控，防止打印机乱码
//                case ACTION_QUERY_PRINTER_STATE:
//                    if (counts >=0) {
//                        if(continuityprint) {
//                            printcount++;
//                            Utils.toast(MainActivity.this, getString(R.string.str_continuityprinter) + " " + printcount);
//                        }
//                        if(counts!=0) {
//                            sendContinuityPrint();
//                        }else {
//                            continuityprint=false;
//                        }
//                    }
//                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 打印机组状态更新
     *
     * @param groupType 组类别
     */
    private void printerGroupStatusUpdate(int groupType, int type, boolean change) {
        for (int i = 0; i < printerGroup.size(); i++) {
            if (printerGroup.get(i).getGroupType() == groupType) {
                if (this.listener != null) {
                    this.listener.printerGroupStatusUpdate(i, type, change);
                }
                break;
            }
        }
    }

    /**
     * 打印机状态更新
     */
    private void printerStatusUpdate(int groupType, int childPosition, int type, boolean change) {
        for (int i = 0; i < printerGroup.size(); i++) {
            if (printerGroup.get(i).getGroupType() == groupType) {
                if (this.listener != null) {
                    this.listener.printerStatusUpdate(i, childPosition, type, change);
                }
                break;
            }
        }
    }

    /**
     * 接口update
     *
     * @param groupType interfaceType
     * @param status    on/off
     */
    private void printerInterfaceUpdate(int groupType, boolean status) {
        for (int i = 0; i < printerGroup.size(); i++) {
            if (printerGroup.get(i).getGroupType() == groupType) {
                if (this.listener != null) {
                    this.listener.printerGroupStatusUpdate(i, PrinterDeviceGroup.PORT_STATUS, status);
                }
                break;
            }
        }
    }

    /**
     * 设置监听接口
     *
     * @param listener 监听接口
     */
    public void setOnPrinterStatusUpdateListener(OnPrinterStatusUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * 打印机状态更新监听
     */
    public interface OnPrinterStatusUpdateListener {
        /**
         * 打印机状态更新
         *
         * @param groupPosition 组ID
         * @param childPosition 子ID
         * @param statusType    状态类型
         * @param change        状态类型
         */
        void printerStatusUpdate(int groupPosition, int childPosition, int statusType, boolean change);

        /**
         * 打印机组的状态更新
         *
         * @param groupPosition 组ID
         * @param statusType    状态类型
         * @param change        状态改变
         */
        void printerGroupStatusUpdate(int groupPosition, int statusType, boolean change);
    }

    /**
     * 注销
     */
    public void unManager() {
        SupplierApp.getInstance().unregisterReceiver(mLinkPrinterReceiver);
        blueToothPrinterBusiness.close();
    }

    /**
     * 处理蓝牙打印业务
     */
    private class BlueToothPrinterBusiness extends PrinterBusiness {

        private final static String BT_PB = "bt_pb";

        private BlueToothPrinterBusiness() {
            //打开蓝牙
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                BluetoothAdapter.getDefaultAdapter().enable();
            }

            //初始化蓝牙设备组--这个是内部使用的
            super.bluePrinterGroup = new PrinterDeviceGroup(SupplierApp.getInstance().getString(R.string.printer_bluetooth_title), LINK_TYPE_BLUETOOTH);
            super.bluePrinterGroup.setScanning(BluetoothAdapter.getDefaultAdapter().isDiscovering());
            super.bluePrinterGroup.setPortOpen(BluetoothAdapter.getDefaultAdapter().enable());

            //注册蓝牙发现广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            SupplierApp.getInstance().registerReceiver(this.mFindBlueToothReceiver, filter);
        }

        @Override
        protected void initialization() {
            SharedPreferences preferences = SupplierApp.getInstance().getSharedPreferences(BT_PB, Context.MODE_PRIVATE);
            Map<String, String> devices = (Map<String, String>) preferences.getAll();
            for (Map.Entry<String, String> set : devices.entrySet()) {
                /**
                 * key:打印机地址
                 * attr[0]:打印机设备类型
                 * attr[1]:打印机名字
                 * attr[2]:打印机别名
                 * attr[3]:打印机连接次数
                 *
                 * 与写入反向
                 */
                String[] attr = set.getValue().split(",");
                Log.d(TAG, "initialization: " + attr[0] + "--" + attr[1] + "--" + attr[2] + "--" + attr[3] + "--");
                PrinterDevice bDev = new BluePrinterDevice(LINK_TYPE_BLUETOOTH, Integer.parseInt(attr[0]), set.getKey(), attr[1]);
                bDev.setDeviceAlias(attr[2]);
                bDev.setLinkCount(Integer.parseInt(attr[3]));
                super.bluePrinterGroup.addChildren(bDev);
            }
        }

        @Override
        public PrinterDeviceGroup getBluePrinterGroup() {
            return super.getBluePrinterGroup();
        }

        /**
         * 扫描蓝牙打印机
         */
        @Override
        protected void scanPrinter() {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter.isEnabled() && !adapter.isDiscovering()) {
                adapter.startDiscovery();
            }
        }

        /**
         * 设置接口状态
         *
         * @param status true/false
         */
        @Override
        protected void setInterfaceStatus(boolean status) {
            Log.e(TAG, "setInterfaceStatus: status is " + status);
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (status && !adapter.isEnabled()) {
                adapter.enable();
                Log.e(TAG, "setInterfaceStatus: adapter.enable()");
            }
            if (!status && adapter.isEnabled()) {
                adapter.disable();
                Log.e(TAG, "setInterfaceStatus: adapter.disable()");
            }
        }

        /**
         * 连接蓝牙打印机
         */
        @Override
        protected void linkPrinter(int position) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter.isEnabled() && adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            //先断开组中已经连接的设备
            bluePrinterGroup.closeDevice();
            //连接
            PrinterDevice device = bluePrinterGroup.getChild(position);
            device.linkDevice();
        }

        /**
         * 更新打印机连接状态
         *
         * @param macAddress macAddress
         * @param isLinked   连接状态
         */
        @Override
        protected void updatePrinterStatus(String macAddress, boolean isLinked) {
            //先找到设备
            for (int i = 0; i < bluePrinterGroup.getChildrens().size(); i++) {
                PrinterDevice device = bluePrinterGroup.getChildrens().get(i);
                if (device.getAddress().equals(macAddress)) {
                    Log.d(TAG, "updatePrinterStatus: " + device.getDeviceName() + "---" + macAddress + "--" + isLinked);
                    //更新状态
                    device.setLinking(false);
                    device.setLinkStatus(isLinked);
                    //如果连接成功，则需要更新设备的属性--连接次数，别名等
                    if (isLinked) {
                        //连接次数+1
                        device.addLinkCount();
                        updateDeviceAttr(device);
                    }
                    //通知上层
                    printerStatusUpdate(LINK_TYPE_BLUETOOTH, i, PrinterDevice.LINKED_STATUS, isLinked);
                    break;
                }
            }
        }

        /**
         * 更新打印机属性
         *
         * @param address     Address
         * @param printerType 打印机类型
         * @param alias       别名
         */
        @Override
        protected void updatePrinterAttr(String address, int printerType, String alias) {
            //先找到设备
            for (int i = 0; i < bluePrinterGroup.getChildrens().size(); i++) {
                PrinterDevice device = bluePrinterGroup.getChildrens().get(i);
                if (device.getAddress().equals(address)) {
                    device.setDeviceAlias(alias);
                    device.setPrinterType(printerType);
                    updateDeviceAttr(device);
                    break;
                }
            }
        }

        /**
         * 更新连接中状态
         *
         * @param address   地址
         * @param isLinking linking
         */
        @Override
        protected void updatePrinterLinking(String address, boolean isLinking) {
            //先找到设备
            for (PrinterDevice device : bluePrinterGroup.getChildrens()) {
                if (device.getAddress().equals(address)) {
                    //更新状态
                    device.setLinking(isLinking);
                    //排序
                    bluePrinterGroup.sync();
                    //通知上层--由于从新排序了，这里更新组状态
                    printerGroupStatusUpdate(LINK_TYPE_BLUETOOTH, PrinterDevice.LINKING_STATUS, isLinking);
                    break;
                }
            }
        }

        /**
         * 后台自动连接
         */
        @Override
        protected void autoLinkTaskStart() {
            if (!isAutoLinkStart) {
                isAutoLinkStart = true;
                timer.schedule(autoLinkTask, 2000, 20000);
            }
        }

        private boolean isAutoLinkStart = false;
        private final Timer timer = new Timer();
        private TimerTask autoLinkTask = new TimerTask() {
            @Override
            public void run() {
                if (BlueToothPrinterBusiness.super.bluePrinterGroup.getChildrens().size() == 0) {
                    return;
                }
                PrinterDevice linkedDev = null;
                for (PrinterDevice device : BlueToothPrinterBusiness.super.bluePrinterGroup.getChildrens()) {
                    if (device.isLink()) {
                        linkedDev = device;
                        break;
                    }
                }

                /**
                 * 没有打印机连接上,自动连接连接次数最大的打印机
                 */
                if (linkedDev == null) {
                    BlueToothPrinterBusiness.super.bluePrinterGroup.sync();
                    linkedDev = BlueToothPrinterBusiness.super.bluePrinterGroup.getChildrens().get(0);
                    linkedDev.linkDevice();
                }
            }
        };

        /**
         * 更新设备属性到 SharedPreferences
         *
         * @param device PrinterDevice
         */
        private void updateDeviceAttr(PrinterDevice device) {
            SharedPreferences preferences = SupplierApp.getInstance().getSharedPreferences(BT_PB, Context.MODE_PRIVATE);
            //添加设备到已连接过列表，如果已存在则更新
            SharedPreferences.Editor editor = preferences.edit();
            StringBuilder attr = new StringBuilder();
            /**
             * key:打印机地址
             * attr[0]:打印机设备类型
             * attr[1]:打印机名字
             * attr[2]:打印机别名
             * attr[3]:打印机连接次数
             */
            Log.d(TAG, "updateDeviceAttr: " + device.getPrinterType() + "-" + device.getDeviceName() + "-" +
                    device.getAlias() + "-" + device.getLinkCount());
            attr.append(device.getPrinterType());
            attr.append(",");
            attr.append(device.getDeviceName());
            attr.append(",");
            attr.append(device.getAlias());
            attr.append(",");
            attr.append(device.getLinkCount());
            editor.putString(device.getAddress(), attr.toString());
            editor.apply();
        }

        /**
         * 注销
         */
        @Override
        protected void close() {
            SupplierApp.getInstance().unregisterReceiver(mFindBlueToothReceiver);
            timer.cancel();     //停止自动连接任务
        }

        /**
         * 扫描蓝牙设备广播
         */
        private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(TAG, "onReceive: MajorDeviceClass is " + device.getBluetoothClass().getMajorDeviceClass() + " " + device.getBluetoothClass().getDeviceClass());
                    //判断是否是打印机设备
                    boolean isAdd = false;
                    if (device.getBluetoothClass().getMajorDeviceClass() == IMAGING) {
                        for (PrinterDevice d : bluePrinterGroup.getChildrens()) {
                            if (d.getAddress().equals(device.getAddress())) {
                                isAdd = true;
                                break;
                            }
                        }

                        if (!isAdd) {
                            PrinterDevice blueDevice = new BluePrinterDevice(LINK_TYPE_BLUETOOTH, PrinterDevice.UNKNOWN, device.getAddress(), device.getName());
                            bluePrinterGroup.addChildren(blueDevice);
                            printerGroupStatusUpdate(LINK_TYPE_BLUETOOTH, PrinterDeviceGroup.CHILD_NUM, true);      //更新组状态
                        }
                    }
                    // When discovery is start, change the Activity title
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    bluePrinterGroup.setScanning(true);
                    printerGroupStatusUpdate(LINK_TYPE_BLUETOOTH, PrinterDeviceGroup.SCAN_STATUS, true);
                    // When discovery is finished, change the Activity title
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    //停止蓝牙搜索
                    bluePrinterGroup.setScanning(false);
                    printerGroupStatusUpdate(LINK_TYPE_BLUETOOTH, PrinterDeviceGroup.SCAN_STATUS, false);
                    Utils.toast(SupplierApp.getInstance(), SupplierApp.getInstance().getString(R.string.printer_scan_finish));
                    // When bluetooth adapter change status
                } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    int bluetooth_state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);
                    Log.e(TAG, "onReceive: bluetooth_state " + bluetooth_state);
                    switch (bluetooth_state) {
                        case BluetoothAdapter.STATE_OFF://关闭
                            //断开设备连接
                            bluePrinterGroup.closeDevice();
                            Log.e(TAG, "onReceive: bluePrinterGroup.closeDevice()");
                            //重置设备列表
                            bluePrinterGroup.clearChildrens();
                            bluePrinterGroup.setScanning(false);
                            bluePrinterGroup.setPortOpen(false);
                            Log.e(TAG, "onReceive: bluePrinterGroup.clearChildrens()");
                            Log.e(TAG, "onReceive: bluePrinterGroup.setScanning(false)");
                            Log.e(TAG, "onReceive: bluePrinterGroup.setPortOpen(false)");
                            //通知上层
                            printerInterfaceUpdate(LINK_TYPE_BLUETOOTH, false);
                            Log.e(TAG, "onReceive: printerInterfaceUpdate(LINK_TYPE_BLUETOOTH, false)");
                            break;
                        case BluetoothAdapter.STATE_ON://开启
                            //初始化已连接过的设备
                            initialization();
                            Log.e(TAG, "onReceive: initialization()");
                            //设置端口
                            bluePrinterGroup.setPortOpen(true);
                            Log.e(TAG, "onReceive: bluePrinterGroup.setPortOpen(true)");
                            //通知上层
                            printerInterfaceUpdate(LINK_TYPE_BLUETOOTH, true);
                            Log.e(TAG, "onReceive: printerInterfaceUpdate(LINK_TYPE_BLUETOOTH, true)");
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }
}
