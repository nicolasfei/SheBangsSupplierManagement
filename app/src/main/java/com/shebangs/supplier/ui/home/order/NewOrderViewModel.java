package com.shebangs.supplier.ui.home.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;
import com.shebangs.supplier.common.OperateError;
import com.shebangs.supplier.common.OperateInUserView;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.server.CommandResponse;
import com.shebangs.supplier.server.CommandTypeEnum;
import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;
import com.shebangs.supplier.server.Invoker;
import com.shebangs.supplier.server.order.OrderInterface;
import com.shebangs.supplier.supplier.SupplierKeeper;
import com.shebangs.supplier.ui.home.order.data.OrderInformation;
import com.shebangs.supplier.ui.home.order.data.OrderQueryCondition;
import com.shebangs.supplier.ui.home.order.data.OrderSort;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrderViewModel extends ViewModel {

    private List<OrderSort> sortList;               //排序选项
    private List<OrderInformation> orderList;       //订单列表
    private int totalNum;               //被选中的订单数统计
    private float totalPrice;           //被选中的订单价格统计

    private OrderQueryCondition queryCondition;     //订单查询条件

    private MutableLiveData<OperateResult> orderMultipleChoiceOperateResult = new MutableLiveData<>();     //多选操作结果
    private MutableLiveData<OperateResult> orderQueryResult = new MutableLiveData<>();              //订单查询结果
    private MutableLiveData<OperateResult> orderSubmitResult = new MutableLiveData<>();             //订单查询结果
    private MutableLiveData<OperateResult> orderSortResult = new MutableLiveData<>();               //订单排序结果
    private MutableLiveData<OperateResult> orderChoiceStatusChange = new MutableLiveData<>();       //订单选中状态更新结果

    public NewOrderViewModel() {
        this.sortList = new ArrayList<>();
        this.sortList.add(OrderSort.SORT_OrderTime);
        this.sortList.add(OrderSort.SORT_ShipmentTime);
        this.sortList.add(OrderSort.SORT_Branch);
        this.sortList.add(OrderSort.SORT_Warehouse);
        this.sortList.add(OrderSort.SORT_OrderClass);
        this.sortList.add(OrderSort.SORT_OrderNumRise);
        this.sortList.add(OrderSort.SORT_OrderNumDrop);

        this.orderList = new ArrayList<>();
        this.orderList.add(OrderInformation.getTestData());
        this.orderList.add(OrderInformation.getTestData());
        this.orderList.add(OrderInformation.getTestData());
        this.orderList.add(OrderInformation.getTestData());
        this.orderList.add(OrderInformation.getTestData());

        this.queryCondition = new OrderQueryCondition();
    }

    public MutableLiveData<OperateResult> getOrderMultipleChoiceOperateResult() {
        return orderMultipleChoiceOperateResult;
    }

    public MutableLiveData<OperateResult> getOrderQueryResult() {
        return orderQueryResult;
    }

    public MutableLiveData<OperateResult> getOrderSortResult() {
        return orderSortResult;
    }

    public MutableLiveData<OperateResult> getOrderSubmitResult() {
        return orderSubmitResult;
    }

    public MutableLiveData<OperateResult> getOrderChoiceStatusChange() {
        return orderChoiceStatusChange;
    }

    public List<String> getSortList() {
        List<String> sList = new ArrayList<>();
        sList.add(OrderSort.SORT_OrderTime.getRule());
        sList.add(OrderSort.SORT_ShipmentTime.getRule());
        sList.add(OrderSort.SORT_Branch.getRule());
        sList.add(OrderSort.SORT_Warehouse.getRule());
        sList.add(OrderSort.SORT_OrderClass.getRule());
        sList.add(OrderSort.SORT_OrderNumRise.getRule());
        sList.add(OrderSort.SORT_OrderNumDrop.getRule());
        return sList;
    }

    /**
     * 清空查询条件
     */
    public void clearQueryCondition() {
        this.queryCondition.clear();
    }

    public List<OrderInformation> getOrderList() {
        return orderList;
    }

    public OrderQueryCondition getQueryCondition() {
        return queryCondition;
    }

    /**
     * 获取选中订单的商品总价
     *
     * @return 选中订单的商品总价
     */
    public float getTotalPrice() {
        return totalPrice;
    }

    /**
     * 获取选中订单的商品总数
     *
     * @return 选中订单的商品总数
     */
    public int getTotalNum() {
        return totalNum;
    }

    /**
     * 查询订单
     */
    public void queryOrder() {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_SUPPLIER_ORDER;
        vo.url = OrderInterface.OrderQuery;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", "goodsCode");
        parameters.put("userkey", SupplierKeeper.getInstance().getOnDutySupplier().key);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 批量提交订单
     */
    public void submitOrders() {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_SUPPLIER_ORDER;
        vo.url = OrderInterface.OrderHandler;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_POST;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", "goodsCode");
        parameters.put("userkey", SupplierKeeper.getInstance().getOnDutySupplier().key);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 查询结果返回接口
     */
    private Invoker.OnExecResultCallback callback = new Invoker.OnExecResultCallback() {
        @Override
        public void execResult(CommandResponse result) {
            switch (result.url) {
                case OrderInterface.OrderQuery:
                    if (result.success) {
                        try {
                            OrderInformation item = new OrderInformation(result.data);
                            orderList.add(item);
                            orderQueryResult.setValue(new OperateResult(new OperateInUserView(null)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            orderQueryResult.setValue(new OperateResult(new OperateError(-1,
                                    SupplierApp.getInstance().getString(R.string.format_error), null)));
                        }

                    } else {
                        orderQueryResult.setValue(new OperateResult(new OperateError(result.code, result.msg, null)));
                    }
                    break;
                case OrderInterface.OrderHandler:
                    if (result.success) {
                        orderSubmitResult.setValue(new OperateResult(new OperateInUserView(null)));
                    } else {
                        orderSubmitResult.setValue(new OperateResult(new OperateError(result.code, result.msg, null)));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 设置订单可多选
     */
    public void setOrderMultipleChoice(boolean openOrClose) {
        for (OrderInformation information : this.orderList) {
            information.canChecked = openOrClose;
            if (!openOrClose) {              //关闭了可以多选，则之前的选择作废
                information.checked = false;
            }
        }
        orderMultipleChoiceOperateResult.setValue(new OperateResult(new OperateInUserView(null)));
        if (!openOrClose) {
            this.totalPrice = 0;
            this.totalNum = 0;
            this.orderChoiceStatusChange.setValue(new OperateResult(new OperateInUserView(null)));       //选中订单状态改变
        }
    }

    /**
     * 更新单个订单的状态，订单条码已打印
     *
     * @param position position
     */
    public void updateOrderStatus(int position) {
        this.orderList.get(position).isPrint = true;
    }

    /**
     * 批量更新订单状态
     */
    public void updateOrderStatusInBatch() {

    }

    /**
     * 设置是否全部选中订单
     *
     * @param isChecked true/false
     */
    public void setOrdersAllChoice(boolean isChecked) {
        if (isChecked) {
            this.totalNum = 0;
            this.totalPrice = 0;
            for (OrderInformation order : this.orderList) {
                order.checked = true;
                this.totalNum += order.goodsNum;
                this.totalPrice += order.bid;
            }
        } else {
            this.totalNum = 0;
            this.totalPrice = 0;
            for (OrderInformation order : this.orderList) {
                order.checked = false;
            }
        }
        this.orderChoiceStatusChange.setValue(new OperateResult(new OperateInUserView(null)));
    }

    /**
     * 排序
     *
     * @param position 排序规则
     */
    public void resortOfRule(int position) {
        switch (sortList.get(position)) {
            case SORT_OrderTime:
                Collections.sort(this.orderList, new Comparator<OrderInformation>() {
                    @Override
                    public int compare(OrderInformation o1, OrderInformation o2) {
                        return o1.orderTime.compareTo(o2.orderTime);
                    }
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_ShipmentTime:
                Collections.sort(this.orderList, new Comparator<OrderInformation>() {
                    @Override
                    public int compare(OrderInformation o1, OrderInformation o2) {
                        return o1.shipmentTime.compareTo(o2.shipmentTime);
                    }
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_Branch:
                Collections.sort(this.orderList, new Comparator<OrderInformation>() {
                    @Override
                    public int compare(OrderInformation o1, OrderInformation o2) {
                        return o1.branch.compareTo(o2.branch);
                    }
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_Warehouse:
                Collections.sort(this.orderList, new Comparator<OrderInformation>() {
                    @Override
                    public int compare(OrderInformation o1, OrderInformation o2) {
                        return o1.warehouse.compareTo(o2.warehouse);
                    }
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_OrderClass:
                Collections.sort(this.orderList, new Comparator<OrderInformation>() {
                    @Override
                    public int compare(OrderInformation o1, OrderInformation o2) {
                        return Integer.compare(o1.orderClass.getType(), o2.orderClass.getType());
                    }
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_OrderNumRise:
                Collections.sort(this.orderList, new Comparator<OrderInformation>() {
                    @Override
                    public int compare(OrderInformation o1, OrderInformation o2) {
                        return Integer.compare(o1.goodsNum, o2.goodsNum);
                    }
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_OrderNumDrop:
                Collections.sort(this.orderList, new Comparator<OrderInformation>() {
                    @Override
                    public int compare(OrderInformation o1, OrderInformation o2) {
                        return Integer.compare(o2.goodsNum, o1.goodsNum);
                    }
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            default:
                break;
        }
    }

    /**
     * 订单被选中/取消
     *
     * @param information 订单
     */
    public void setOrdersChoice(OrderInformation information) {
        if (information.checked) {
            this.totalNum += information.goodsNum;
            this.totalPrice += information.bid;
        } else {
            this.totalNum -= information.goodsNum;
            this.totalPrice -= information.bid;
        }
        this.orderChoiceStatusChange.setValue(new OperateResult(new OperateInUserView(null)));
    }

    /**
     * 获取已经选中的订单
     *
     * @return 选中的订单
     */
    public ArrayList<OrderInformation> getCheckedOrders() {
        ArrayList<OrderInformation> checkedOrders = new ArrayList<>();
        for (OrderInformation order : this.orderList) {
            if (order.checked) {
                checkedOrders.add(order);
            }
        }
        return checkedOrders;
    }
}
