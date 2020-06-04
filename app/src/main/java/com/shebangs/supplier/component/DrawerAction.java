package com.shebangs.supplier.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.ActionProvider;

import com.shebangs.shebangssuppliermanagement.R;

public class DrawerAction extends ActionProvider {

    private Context mContext;

    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public DrawerAction(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View onCreateActionView() {
        View root = LayoutInflater.from(this.mContext).inflate(R.layout.new_order_screen, null, false);
        return root;
    }
}
