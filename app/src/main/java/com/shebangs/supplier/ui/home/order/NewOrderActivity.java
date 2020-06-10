package com.shebangs.supplier.ui.home.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nicolas.library.PullToRefreshListView;
import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.common.SheBangsDialog;
import com.shebangs.supplier.common.SheBangsDialog.OnChoiceItemListener;
import com.shebangs.supplier.component.SingleFloatingDialog;
import com.shebangs.supplier.tool.Utils;
import com.shebangs.supplier.ui.BaseActivity;
import com.shebangs.supplier.ui.home.order.data.OrderClass;
import com.shebangs.supplier.ui.home.order.data.OrderInformation;
import com.shebangs.supplier.ui.home.order.data.OrderInformationAdapter;
import com.shebangs.supplier.ui.home.order.data.PrintStatus;
import com.shebangs.supplier.ui.home.order.data.ShipmentStatus;


public class NewOrderActivity extends BaseActivity {
    private static final String TAG = "NewOrderActivity";
    private DrawerLayout drawerLayout;
    private SingleFloatingDialog dialog;
    private CheckBox checkAll;
    private TextView total;
    private Button submit;
    private PullToRefreshListView listView;
    private OrderInformationAdapter adapter;

    //以下为查询条件
    private TextView orderTime, goodsID, whichWarehouse, branch, shipmentTime, shipmentStatus;
    private CheckBox orderClassAll, orderClassFirst, orderClassCPFR;     //下单类型
    private CheckBox printYet, printNot;                 //是否打印
    private Button queryClear, query;

    private NewOrderViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        this.viewModel = new ViewModelProvider(this).get(NewOrderViewModel.class);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //排序选择项对话框
        this.dialog = new SingleFloatingDialog(this, 60, this.viewModel.getSortList(), new SingleFloatingDialog.OnSortChoiceListener() {
            @Override
            public void OnSortChoice(int position) {
                resortOfRule(position);
            }
        });
        //listView
        this.listView = findViewById(R.id.pullToRefreshListView);
        this.adapter = new OrderInformationAdapter(this, this.viewModel.getOrderList());
        this.adapter.setOnOrderChoiceListener(new OrderInformationAdapter.OnOrderChoiceListener() {
            @Override
            public void orderChoice(OrderInformation information) {
                viewModel.setOrdersChoice(information);
            }
        });
        this.listView.setAdapter(this.adapter);
        this.listView.setOnLoadingMoreListener(this.loadingMoreListener);
        this.listView.setOnRefreshListener(this.refreshListener);
        //全选
        this.checkAll = findViewById(R.id.all);
        this.checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setOrdersAllChoice(isChecked);
            }
        });
        //合计
        this.total = findViewById(R.id.total);
        this.total.setText(getString(R.string.total));
        //提交
        this.submit = findViewById(R.id.submit);
        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先打印订单条码，跳转到订单条码打印页面
                jumpToPrintOrderActivity();
            }
        });
        //setClickable(false)方法一定要放在setOnClickListener()方法之后。不然没有效果
        this.submit.setClickable(false);
        /**
         * 查询条件
         */
        this.orderTime = findViewById(R.id.orderTime);
        this.goodsID = findViewById(R.id.goodsID);
        this.whichWarehouse = findViewById(R.id.whichWarehouse);
        this.branch = findViewById(R.id.branch);
        this.shipmentTime = findViewById(R.id.shipmentTime);
        this.shipmentStatus = findViewById(R.id.shipmentStatus);

        this.orderClassAll = findViewById(R.id.Chip1);
        this.orderClassFirst = findViewById(R.id.Chip2);
        this.orderClassCPFR = findViewById(R.id.Chip3);
        this.printYet = findViewById(R.id.c1);
        this.printNot = findViewById(R.id.c2);

        this.queryClear = findViewById(R.id.clear);
        this.query = findViewById(R.id.yes);

        this.orderTime.setOnClickListener(this.onClickListener);
        this.goodsID.setOnClickListener(this.onClickListener);
        this.whichWarehouse.setOnClickListener(this.onClickListener);
        this.branch.setOnClickListener(this.onClickListener);
        this.shipmentTime.setOnClickListener(this.onClickListener);
        this.shipmentStatus.setOnClickListener(this.onClickListener);
        this.queryClear.setOnClickListener(this.onClickListener);
        this.query.setOnClickListener(this.onClickListener);

        this.orderClassAll.setOnCheckedChangeListener(this.checkedChangeListener);
        this.orderClassFirst.setOnCheckedChangeListener(this.checkedChangeListener);
        this.orderClassCPFR.setOnCheckedChangeListener(this.checkedChangeListener);
        this.printYet.setOnCheckedChangeListener(this.checkedChangeListener);
        this.printNot.setOnCheckedChangeListener(this.checkedChangeListener);

        /**
         * 监听是否可以对订单进行多选
         */
        this.viewModel.getOrderMultipleChoiceOperateResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });

        /**
         * 监听排序结果
         */
        this.viewModel.getOrderSortResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                SheBangsDialog.dismissProgressDialog();
                if (operateResult.getSuccess() != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });

        /**
         * 监听订单选中状态改变
         */
        this.viewModel.getOrderChoiceStatusChange().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateOrderCheckStatus();       //更新订单的选中状态
                }
            }
        });
        /**
         * 监听查询结果
         */
        this.viewModel.getOrderQueryResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                SheBangsDialog.dismissProgressDialog();
                if (operateResult.getSuccess() != null) {
                    adapter.notifyDataSetChanged();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(NewOrderActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });
        /**
         * 数据提交到服务器结果监听
         */
        this.viewModel.getOrderSubmitResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                SheBangsDialog.dismissProgressDialog();
                if (operateResult.getSuccess() != null) {
                    adapter.notifyDataSetChanged();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(NewOrderActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.orderTime:
                    SheBangsDialog.showDateTimePickerDialog(NewOrderActivity.this, new SheBangsDialog.OnDateTimePickListener() {
                        @Override
                        public void OnDateTimePick(String dateTime) {
                            orderTime.setText(dateTime);
                            viewModel.getQueryCondition().orderTime = dateTime;
                        }
                    });
                    break;
                case R.id.goodsID://货号
                    SheBangsDialog.showSingleChoiceDialog(R.string.goodsID, NewOrderActivity.this, new String[]{"货号1", "货号2"}, new OnChoiceItemListener() {
                        @Override
                        public void onChoiceItem(String itemName) {
                            String value = NewOrderActivity.this.getString(R.string.goodsID) + "\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + itemName;
                            goodsID.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
                            viewModel.getQueryCondition().goodsID = itemName;
                        }
                    });
                    break;
                case R.id.whichWarehouse:
                    SheBangsDialog.showSingleChoiceDialog(R.string.warehouse, NewOrderActivity.this, new String[]{"库房1", "库房2"}, new OnChoiceItemListener() {
                        @Override
                        public void onChoiceItem(String itemName) {
                            String value = NewOrderActivity.this.getString(R.string.warehouse) + "\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + itemName;
                            whichWarehouse.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
                            viewModel.getQueryCondition().warehouse = itemName;
                        }
                    });
                    break;
                case R.id.branch:
                    SheBangsDialog.showSingleChoiceDialog(R.string.branch, NewOrderActivity.this, new String[]{"分店1", "分店2"}, new OnChoiceItemListener() {
                        @Override
                        public void onChoiceItem(String itemName) {
                            String value = NewOrderActivity.this.getString(R.string.branch) + "\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + itemName;
                            branch.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
                            viewModel.getQueryCondition().branch = itemName;
                        }
                    });
                    break;
                case R.id.shipmentTime:
                    SheBangsDialog.showDateTimePickerDialog(NewOrderActivity.this, new SheBangsDialog.OnDateTimePickListener() {
                        @Override
                        public void OnDateTimePick(String dateTime) {
                            shipmentTime.setText(dateTime);
                            viewModel.getQueryCondition().shipmentTime = dateTime;
                        }
                    });
                    break;
                case R.id.shipmentStatus:
                    SheBangsDialog.showSingleChoiceDialog(R.string.branch, NewOrderActivity.this, ShipmentStatus.getAllValue(), new OnChoiceItemListener() {
                        @Override
                        public void onChoiceItem(String itemName) {
                            String value = NewOrderActivity.this.getString(R.string.shipment_status) + "\u3000\u3000\u3000\u3000" + itemName;
                            shipmentStatus.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
                            viewModel.getQueryCondition().status = ShipmentStatus.valueOf(itemName);
                        }
                    });
                    break;
                case R.id.clear:
                    orderTime.setText("");
                    goodsID.setText(R.string.goodsID);
                    whichWarehouse.setText(R.string.warehouse);
                    branch.setText(R.string.branch);
                    shipmentTime.setText("");
                    shipmentStatus.setText(R.string.shipment_status);
                    orderClassAll.setChecked(false);
                    orderClassFirst.setChecked(false);
                    orderClassCPFR.setChecked(false);
                    printNot.setChecked(false);
                    printYet.setChecked(false);
                    viewModel.clearQueryCondition();
                    break;
                case R.id.yes:
                    queryOrderForCondition();
                    break;
            }
        }
    };

    /**
     * 查询订单
     */
    private void queryOrderForCondition() {
        drawerLayout.closeDrawer(Gravity.RIGHT, true);
        viewModel.queryOrder();
        SheBangsDialog.showProgressDialog(this, getString(R.string.querying));
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.Chip1:
                    viewModel.getQueryCondition().orderClass.setValue(OrderClass.ALL, isChecked);
                    break;
                case R.id.Chip2:
                    viewModel.getQueryCondition().orderClass.setValue(OrderClass.FIRST, isChecked);
                    break;
                case R.id.Chip3:
                    viewModel.getQueryCondition().orderClass.setValue(OrderClass.CPFR, isChecked);
                    break;
                case R.id.c1:
                    viewModel.getQueryCondition().isPrint.setValue(PrintStatus.PRINT, isChecked);
                    break;
                case R.id.c2:
                    viewModel.getQueryCondition().isPrint.setValue(PrintStatus.UN_PRINT, isChecked);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 跳转到订单条码打印页面
     */
    private void jumpToPrintOrderActivity() {
        Intent intent = new Intent(NewOrderActivity.this, null);
        intent.putParcelableArrayListExtra("orders", viewModel.getCheckedOrders());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {     //打印成功
                submitOrderToServer();
            } else if (resultCode == -1) {

            }
        }
    }

    /**
     * 订单提交
     */
    private void submitOrderToServer() {
        SheBangsDialog.showProgressDialog(this, getString(R.string.submitting));
        viewModel.submitOrders();       //提交订单更新数据到服务器
    }

    /**
     * 订单被选中/被取消后更新界面
     */
    private void updateOrderCheckStatus() {
        this.adapter.notifyDataSetChanged();

        String oderTotal;
        if (this.viewModel.getTotalNum() > 0) {
            this.submit.setClickable(true);
            this.submit.setBackground(getDrawable(R.drawable.shape_rectangle_red));
            oderTotal = getString(R.string.total) + getString(R.string.colon) + getString(R.string.num) + "\u3000\u3000x" + this.viewModel.getTotalNum() +
                    "\u3000\u3000\u3000\u3000" + getString(R.string.price) + "\u3000\u3000" + getString(R.string.money) + this.viewModel.getTotalPrice();
        } else {
            this.submit.setClickable(false);
            this.submit.setBackground(getDrawable(R.drawable.shape_rectangle_grey));
            oderTotal = getString(R.string.total);
        }
        this.total.setText(oderTotal);
    }

    /**
     * 重新排序
     *
     * @param position position
     */
    private void resortOfRule(int position) {
        SheBangsDialog.showProgressDialog(this, "");
        viewModel.resortOfRule(position);
    }

    /**
     * 订单加载更多
     */
    private PullToRefreshListView.OnLoadingMoreListener loadingMoreListener = new PullToRefreshListView.OnLoadingMoreListener() {
        @Override
        public void onLoadingMore() {
            Log.d(TAG, "onLoadingMore: ");
        }
    };

    /**
     * 订单刷新
     */
    private PullToRefreshListView.OnRefreshListener refreshListener = new PullToRefreshListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.d(TAG, "onRefresh: ");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_order_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.app_bar_search));
        searchView.setQueryHint(getString(R.string.search_hint));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_screen:
                this.drawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.menu_sort:
                showSortDialog();
                break;
            case R.id.menu_more:
                showOrderMultipleChoiceBox();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 显示全部选择box
     */
    private void showOrderMultipleChoiceBox() {
        if (this.checkAll.getVisibility() == View.VISIBLE) {
            this.checkAll.setVisibility(View.INVISIBLE);
            this.viewModel.setOrderMultipleChoice(false);
        } else {
            this.checkAll.setVisibility(View.VISIBLE);
            this.viewModel.setOrderMultipleChoice(true);
        }
    }

    /**
     * 显示排序对话框
     */
    private void showSortDialog() {
        this.dialog.show();
    }
}
