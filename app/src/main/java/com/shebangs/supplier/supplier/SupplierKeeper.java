package com.shebangs.supplier.supplier;

public class SupplierKeeper {

    private static SupplierKeeper keeper = new SupplierKeeper();

    private Supplier supplier;

    private SupplierKeeper() {
        this.supplier = new Supplier();
    }

    public static SupplierKeeper getInstance() {
        return keeper;
    }

    public Supplier getOnDutySupplier() {
        return supplier;
    }
}

