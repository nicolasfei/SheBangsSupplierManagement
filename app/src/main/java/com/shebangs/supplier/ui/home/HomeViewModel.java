package com.shebangs.supplier.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;
import com.shebangs.supplier.common.ModuleNavigation;
import com.shebangs.supplier.common.OperateInUserView;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.ui.home.order.NewOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<OperateResult> updateNavNumResult;
    private List<ModuleNavigation> content = new ArrayList<>();

    public HomeViewModel() {
        this.updateNavNumResult = new MutableLiveData<>();
        //content.add(new ModuleNavigation(true, getString(R.string.nav_cashier_title), 0, null));
        content.add(new ModuleNavigation(false, SupplierApp.getInstance().getString(R.string.nav_order_order), R.drawable.ic_sale_order_blue, NewOrderActivity.class));
        content.add(new ModuleNavigation(false, SupplierApp.getInstance().getString(R.string.nav_order_query), R.drawable.ic_order_query, null));
        content.add(new ModuleNavigation(false, SupplierApp.getInstance().getString(R.string.nav_order_return), R.drawable.ic_return_goods, null));
        content.add(new ModuleNavigation(false, SupplierApp.getInstance().getString(R.string.nav_order_credit), R.drawable.ic_credit, null));
    }

    public LiveData<OperateResult> getUpdateNavNumResult() {
        return updateNavNumResult;
    }

    /**
     * 更新ModuleNavigation通知数字
     *
     * @param position position
     * @param num      num
     */
    public void updateModuleNoticeNum(int position, int num) {
        ModuleNavigation item = content.get(position);
        if (item.getNavigationNum() != num) {
            item.setNavigationNum(num);
            updateNavNumResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
    }

    public ModuleNavigation getModuleNavigation(int position) {
        return content.get(position);
    }

    public List<ModuleNavigation> getModuleNavigationList() {
        return content;
    }
}