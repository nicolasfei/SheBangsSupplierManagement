package com.shebangs.supplier.app;

import android.app.Application;

public class SupplierApp extends Application {

    private static SupplierApp app;

    public static SupplierApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
