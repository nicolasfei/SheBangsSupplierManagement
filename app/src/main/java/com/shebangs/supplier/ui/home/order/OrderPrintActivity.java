package com.shebangs.supplier.ui.home.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nicolas.toollibrary.Utils;
import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.supplier.SupplierKeeper;
import com.shebangs.supplier.ui.BaseActivity;
import com.shebangs.supplier.ui.device.printer.PrinterActivity;
import com.shebangs.supplier.data.OrderPrintAdapter;

/**
 * 订单打印条码页面--single
 */
public class OrderPrintActivity extends BaseActivity {
    private static final String TAG = "OrderPrintActivity";
    private OrderPrintViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_print);
        this.viewModel = new ViewModelProvider(this).get(OrderPrintViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {
            this.viewModel.setOrders(intent.getParcelableArrayListExtra("orders"));
            Log.d(TAG, "onCreate: get orders form start Activity");
            Log.d(TAG, "onCreate: " + this.viewModel.printOrderInformation());
        }
        //供应商编号
        String supplierCode = getString(R.string.supplier) + "\u3000\u3000\u3000" + SupplierKeeper.getInstance().getOnDutySupplier().id;
        TextView supplier = findViewById(R.id.supplier);
        supplier.setText(supplierCode);
        //营业员
        String staffName = getString(R.string.staff) + "\u3000\u3000\u3000" + SupplierKeeper.getInstance().getOnDutySupplier().name;
        TextView staff = findViewById(R.id.staff);
        staff.setText(staffName);
        //订单
        ListView listView = findViewById(R.id.listView);
        OrderPrintAdapter adapter = new OrderPrintAdapter(this, this.viewModel.getOrders());
        listView.setAdapter(adapter);
        //合计
        TextView total = findViewById(R.id.total);
        String totalValue = getString(R.string.total) + getString(R.string.colon) + this.viewModel.getTotalString();
        total.setText(totalValue);
        //打印
        Button print = findViewById(R.id.button2);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printOrder();
            }
        });

        /**
         * 监听打印结果
         */
        this.viewModel.getPrintOrderResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {

                }
                if (operateResult.getError() != null) {
                    Utils.toast(OrderPrintActivity.this, operateResult.getError().getErrorMsg());
                    jump2PrinterActivity();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode > 0) {
                this.viewModel.printOrder();
            } else {
                Utils.toast(this, getString(R.string.printer_no_link));
            }
        }
    }

    /**
     * 跳转到打印机设置页面
     */
    private void jump2PrinterActivity() {
        Intent intent = new Intent(this, PrinterActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * 打印订单
     */
    private void printOrder() {
        this.viewModel.printOrder();
    }
}
