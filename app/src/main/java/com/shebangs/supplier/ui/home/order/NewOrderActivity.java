package com.shebangs.supplier.ui.home.order;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.component.SingleFloatingDialog;
import com.shebangs.supplier.ui.BaseActivity;


public class NewOrderActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private SingleFloatingDialog dialog;

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

        this.dialog = new SingleFloatingDialog(this, 60, this.viewModel.getSortList(), new SingleFloatingDialog.OnSortChoiceListener() {
            @Override
            public void OnSortChoice(int position) {
                resortOfRule(position);
            }
        });
    }

    /**
     * 重新排序
     *
     * @param position position
     */
    private void resortOfRule(int position) {

    }

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
                this.dialog.show();
                break;
            case R.id.menu_more:
                this.viewModel.setOrderMultipleChoice();
                break;
            default:
                break;
        }
        return true;
    }
}
