package com.shebangs.supplier.ui.home.order;

import android.os.Bundle;
import android.os.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nicolas.toollibrary.Tool;
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
import com.shebangs.supplier.server.InvokerHandler;
import com.shebangs.supplier.server.common.CommonInterface;
import com.shebangs.supplier.server.order.OrderInterface;
import com.shebangs.supplier.data.OrderClass;
import com.shebangs.supplier.data.OrderInformation;
import com.shebangs.supplier.data.OrderQueryCondition;
import com.shebangs.supplier.data.OrderSort;

import org.json.JSONException;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewOrderViewModel extends ViewModel {
    private static final String TAG = "TAG";
    private List<OrderSort> sortList;               //排序选项
    private List<OrderInformation> orderList;       //订单列表
    private int totalNum;               //被选中的订单数统计
    private float totalPrice;           //被选中的订单价格统计

    private OrderQueryCondition queryCondition;     //订单查询条件

    private MutableLiveData<OperateResult> orderMultipleChoiceOperateResult = new MutableLiveData<>();     //多选操作结果
    private MutableLiveData<OperateResult> orderQueryResult = new MutableLiveData<>();              //订单查询结果
    private MutableLiveData<OperateResult> orderSubmitResult = new MutableLiveData<>();             //订单提交结果
    private MutableLiveData<OperateResult> orderSortResult = new MutableLiveData<>();               //订单排序结果
    private MutableLiveData<OperateResult> orderChoiceStatusChange = new MutableLiveData<>();       //订单选中状态更新结果
    private MutableLiveData<OperateResult> goodsClassQuery = new MutableLiveData<>();               //商品类型查询

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
        OrderInformation order1 = OrderInformation.getTestData();
        order1.orderTime = "2020-03-06 12:30";
        order1.shipmentTime = "";
        order1.branch = "北京分店1";
        order1.warehouse = "成都库房1";
        order1.orderClass.setType(OrderClass.FIRST);
        order1.goodsNum = 140;
        this.orderList.add(order1);

        OrderInformation order2 = OrderInformation.getTestData();
        order2.orderTime = "2014-03-06 12:30";
        order2.shipmentTime = "";
        order2.branch = "南京分店1";
        order2.warehouse = "成都库房1";
        order2.orderClass.setType(OrderClass.ALL);
        order2.goodsNum = 110;
        this.orderList.add(order2);


        OrderInformation order3 = OrderInformation.getTestData();
        order3.orderTime = "2011-03-06 12:30";
        order3.shipmentTime = "";
        order3.branch = "东京分店1";
        order3.warehouse = "成都库房1";
        order3.orderClass.setType(OrderClass.CPFR);
        order3.goodsNum = 170;
        this.orderList.add(order3);


        OrderInformation order4 = OrderInformation.getTestData();
        order4.orderTime = "2016-03-06 12:30";
        order4.shipmentTime = "";
        order4.branch = "西京分店1";
        order4.warehouse = "成都库房1";
        order4.orderClass.setType(OrderClass.FIRST);
        order4.goodsNum = 190;
        this.orderList.add(order4);


        OrderInformation order5 = OrderInformation.getTestData();
        order5.orderTime = "2009-03-06 12:30";
        order5.shipmentTime = "";
        order5.branch = "中华分店1";
        order5.warehouse = "成都库房1";
        order5.orderClass.setType(OrderClass.ALL);
        order5.goodsNum = 20;
        this.orderList.add(order5);

        this.queryCondition = new OrderQueryCondition();
    }

    LiveData<OperateResult> getOrderMultipleChoiceOperateResult() {
        return orderMultipleChoiceOperateResult;
    }

    LiveData<OperateResult> getOrderQueryResult() {
        return orderQueryResult;
    }

    LiveData<OperateResult> getOrderSortResult() {
        return orderSortResult;
    }

    LiveData<OperateResult> getOrderSubmitResult() {
        return orderSubmitResult;
    }

    LiveData<OperateResult> getOrderChoiceStatusChange() {
        return orderChoiceStatusChange;
    }

    public LiveData<OperateResult> getGoodsClassQuery() {
        return goodsClassQuery;
    }

    List<String> getSortList() {
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
    void clearQueryCondition() {
        this.queryCondition.clear();
    }

    List<OrderInformation> getOrderList() {
        return orderList;
    }

    OrderQueryCondition getQueryCondition() {
        return queryCondition;
    }

    /**
     * 获取选中订单的商品总价
     *
     * @return 选中订单的商品总价
     */
    float getTotalPrice() {
        this.totalPrice = 0;
        for (OrderInformation order : this.orderList) {
            if (order.checked) {
                this.totalPrice += order.bid;
            }
        }
        return this.totalPrice;
    }

    /**
     * 获取选中订单的商品总数
     *
     * @return 选中订单的商品总数
     */
    int getTotalNum() {
        this.totalNum = 0;
        for (OrderInformation order : this.orderList) {
            if (order.checked) {
                this.totalNum += order.goodsNum;
            }
        }
        return this.totalNum;
    }

    /**
     * 查询订单
     */
    void queryOrder() {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_SUPPLIER_ORDER;
        vo.url = OrderInterface.OrderQuery;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", "goodsCode");
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 查询商品类型
     */
    public void queryGoodsClass() {
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(InvokerHandler.getInstance().getGoodsClassCommand(null));
    }

    /**
     * 批量提交订单
     */
    void submitOrders() {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_SUPPLIER_ORDER;
        vo.url = OrderInterface.OrderHandler;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_POST;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", "goodsCode");
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
                case CommonInterface.GoodsClassQuery:
                    if (result.success) {
                        Message msg = new Message();
                        msg.obj = result.jsonData;
                        goodsClassQuery.setValue(new OperateResult(new OperateInUserView(msg)));
                    } else {
                        goodsClassQuery.setValue(new OperateResult(new OperateError(-1, result.msg, null)));
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
    void setOrderMultipleChoice(boolean openOrClose) {
        for (OrderInformation information : this.orderList) {
            information.canChecked = openOrClose;
            if (!openOrClose) {              //关闭了可以多选，则之前的选择作废
                information.checked = false;
            }
        }
        orderMultipleChoiceOperateResult.setValue(new OperateResult(new OperateInUserView(null)));
        if (!openOrClose) {
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
    void setOrdersAllChoice(boolean isChecked) {
        if (isChecked) {
            for (OrderInformation order : this.orderList) {
                order.checked = true;
            }
        } else {
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
    void resortOfRule(int position) {
        switch (sortList.get(position)) {
            case SORT_OrderTime:
                Collections.sort(this.orderList, (o1, o2) -> -(o1.orderTime.compareTo(o2.orderTime)));
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_ShipmentTime:
                Collections.sort(this.orderList, (o1, o2) -> o1.shipmentTime.compareTo(o2.shipmentTime));
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_Branch:
                Collections.sort(this.orderList, (o1, o2) -> {
                    String p1 = Tool.converterToFirstSpell(o1.branch);
                    String p2 = Tool.converterToFirstSpell(o2.branch);
                    return Collator.getInstance(Locale.ENGLISH).compare(p1, p2);
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_Warehouse:
                Collections.sort(this.orderList, (o1, o2) -> {
                    String p1 = Tool.converterToFirstSpell(o1.warehouse);
                    String p2 = Tool.converterToFirstSpell(o2.warehouse);
                    return Collator.getInstance(Locale.ENGLISH).compare(p1, p2);
                });
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_OrderClass:
                Collections.sort(this.orderList, (o1, o2) -> Integer.compare(o1.orderClass.getType(), o2.orderClass.getType()));
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_OrderNumRise:
                Collections.sort(this.orderList, (o1, o2) -> -(Integer.compare(o1.goodsNum, o2.goodsNum)));
                this.orderSortResult.setValue(new OperateResult(new OperateInUserView(null)));
                break;
            case SORT_OrderNumDrop:
                Collections.sort(this.orderList, (o1, o2) -> Integer.compare(o1.goodsNum, o2.goodsNum));
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
    void setOrdersChoice(OrderInformation information) {
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
    ArrayList<OrderInformation> getCheckedOrders() {
        ArrayList<OrderInformation> checkedOrders = new ArrayList<>();
        for (OrderInformation order : this.orderList) {
            if (order.checked) {
                checkedOrders.add(order);
            }
        }
        return checkedOrders;
    }
}
