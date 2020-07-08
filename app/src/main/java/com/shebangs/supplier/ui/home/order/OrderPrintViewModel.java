package com.shebangs.supplier.ui.home.order;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nicolas.printerlibrary.PrinterManager;
import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;
import com.shebangs.supplier.common.OperateError;
import com.shebangs.supplier.common.OperateInUserView;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.ui.device.printer.PrintContent;
import com.shebangs.supplier.data.OrderInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderPrintViewModel extends ViewModel {
    private static final String TAG = "OrderPrintViewModel";
    private ArrayList<OrderInformation> orders;
    private int goodsTotal;     //商品合计
    private int orderTotal;     //订单合计
    private float priceTotal;   //价格合计

    private MutableLiveData<OperateResult> printOrderResult;

    public OrderPrintViewModel() {
        this.printOrderResult = new MutableLiveData<>();
    }

    public MutableLiveData<OperateResult> getPrintOrderResult() {
        return printOrderResult;
    }

    public void setOrders(ArrayList<OrderInformation> orders) {
        this.orders = orders;
        this.goodsTotal = 0;
        this.orderTotal = 0;
        this.priceTotal = 0;
        for (OrderInformation order : this.orders) {
            this.goodsTotal += order.goodsNum;
            this.priceTotal += order.bid;
            this.orderTotal++;
            Log.d(TAG, "setOrders: " + order.goodsID + "---" );//+ order.orderClass.toString());
        }
    }

    public List<OrderInformation> getOrders() {
        return orders;
    }

    public String getTotalString() {
        return SupplierApp.getInstance().getString(R.string.order) + "\u3000x" + this.orderTotal + "\u3000\u3000" +
                SupplierApp.getInstance().getString(R.string.goods) + "\u3000x" + this.goodsTotal + "\u3000\u3000" +
                SupplierApp.getInstance().getString(R.string.price) + "\u3000" + SupplierApp.getInstance().getString(R.string.money) + this.priceTotal;
    }

    /**
     * 打印订单条码
     */
    public void printOrder() {
        try {
            PrinterManager.getInstance(null).printLabel(PrintContent.getOrderReceipt(this.orders));
            printOrderResult.setValue(new OperateResult(new OperateInUserView(null)));
        } catch (IOException e) {
            e.printStackTrace();
            printOrderResult.setValue(new OperateResult(new OperateError(0, SupplierApp.getInstance().getString(R.string.printer_no_link), null)));
        }
    }

    public String printOrderInformation() {
        StringBuilder builder = new StringBuilder();
        for (OrderInformation order : this.orders) {
            builder.append(order.toString());
        }
        return builder.toString();
    }
}
