package com.shebangs.supplier.ui.home.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.ui.home.order.data.OrderInformation;
import com.shebangs.supplier.ui.home.order.data.OrderQueryCondition;

import java.util.ArrayList;
import java.util.List;

public class NewOrderViewModel extends ViewModel {

    private List<String> sortList;              //排序选项
    private List<OrderInformation> orderList;   //订单列表

    private MutableLiveData<OperateResult> orderMultipleChoiceResult = new MutableLiveData<>();
    private MutableLiveData<OperateResult> orderQueryResult = new MutableLiveData<>();

    public NewOrderViewModel() {
        this.sortList = new ArrayList<>();
        this.sortList.add(SupplierApp.getInstance().getString(R.string.sort_order_time));
        this.sortList.add(SupplierApp.getInstance().getString(R.string.sort_shipment_time));
        this.sortList.add(SupplierApp.getInstance().getString(R.string.sort_branch));
        this.sortList.add(SupplierApp.getInstance().getString(R.string.sort_warehouse));
        this.sortList.add(SupplierApp.getInstance().getString(R.string.sort_order_class));
        this.sortList.add(SupplierApp.getInstance().getString(R.string.sort_order_num_rise));
        this.sortList.add(SupplierApp.getInstance().getString(R.string.sort_order_num_drop));

        this.orderList = new ArrayList<>();
    }

    public MutableLiveData<OperateResult> getOrderMultipleChoiceResult() {
        return orderMultipleChoiceResult;
    }

    public MutableLiveData<OperateResult> getOrderQueryResult() {
        return orderQueryResult;
    }

    public List<String> getSortList() {
        return sortList;
    }

    public List<OrderInformation> getOrderList() {
        return orderList;
    }

    /**
     * 查询订单
     *
     * @param condition 查询条件
     */
    public void queryOrder(OrderQueryCondition condition) {

    }

    /**
     * 设置订单可多选
     */
    public void setOrderMultipleChoice() {

    }

    /**
     * 更新单个订单的状态，订单条码已打印
     *
     * @param position position
     */
    public void updateOrderStatus(int position) {

    }

    /**
     * 批量更新订单状态
     */
    public void updateOrderStatusInBatch() {

    }
}
