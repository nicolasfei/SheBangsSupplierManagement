package com.shebangs.supplier.server.login;

import com.shebangs.supplier.server.AbstractInterface;

public abstract class LoginInterface extends AbstractInterface {
    //登陆接口
    public final static String Login = AbstractInterface.COMMAND_URL + "Supplier/Login";
    //登出接口
    public final static String Logout = AbstractInterface.COMMAND_URL + "Supplier/LoginOut";
    //供应商信息接口
    public final static String SupplierInformation = AbstractInterface.COMMAND_URL + "Supplier/SupplierById";
    //供应商账号信息接口
    public final static String SupplierAccountInformation = AbstractInterface.COMMAND_URL + "Supplier/SupplierAccountById";
}
