package com.shebangs.supplier.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.common.ModuleNavigation;
import com.shebangs.supplier.common.ModuleNavigationAdapter;
import com.shebangs.supplier.common.OperateResult;

public class HomeFragment extends Fragment implements ModuleNavigationAdapter.OnItemClickListener{

    private HomeViewModel viewModel;

    private Context context;
    private RecyclerView mRecyclerView;
    private ModuleNavigationAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        //设置跨度，即时一行里面包含几个元素
        GridLayoutManager manager = new GridLayoutManager(context, 3);

        // 通过 isTitle 的标志来判断是否是 title
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return viewModel.getModuleNavigationList().get(position).isTitle ? 3 : 1;       //如果是标题则跨3个跨度，即标题占一行
            }
        });

        //设置item分割线
//        mRecyclerView.addItemDecoration(new RecycleGridDivider(context));
        //设置RecyclerView布局管理器
        mRecyclerView.setLayoutManager(manager);
        //设置适配器
        adapter = new ModuleNavigationAdapter(context, this);
        adapter.setContent(viewModel.getModuleNavigationList());
        mRecyclerView.setAdapter(adapter);
        /**
         * 监听按钮数据更新
         */
        viewModel.getUpdateNavNumResult().observe(requireActivity(), new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess()!=null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        ModuleNavigation navigation = viewModel.getModuleNavigationList().get(position);
        if (navigation.isTitle || navigation.navActivity == null) {
            return;
        }
        Intent intent = new Intent(context, navigation.navActivity);
        startActivity(intent);
    }
}
