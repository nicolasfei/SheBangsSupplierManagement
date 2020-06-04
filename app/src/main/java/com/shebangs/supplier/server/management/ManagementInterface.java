package com.shebangs.supplier.server.management;

import com.shebangs.supplier.server.AbstractInterface;

public abstract class ManagementInterface extends AbstractInterface {
    //用户信息查询接口
    public final static String UserInformationQuery = AbstractInterface.COMMAND_URL + "api/manager/query";
    //用户管理-增
    public final static String UserHandlerAdd = AbstractInterface.COMMAND_URL + "api/manager/add";
    //用户管理-删
    public final static String UserHandlerDel = AbstractInterface.COMMAND_URL + "api/manager/del";
    //用户管理-查
    public final static String UserHandlerUpdate = AbstractInterface.COMMAND_URL + "api/manager/update";
    //用户管理-查
    public final static String UserHandlerQuery = AbstractInterface.COMMAND_URL + "api/manager/query";
    //用户管理-登陆记录查询
    public final static String UserLoginQuery = AbstractInterface.COMMAND_URL + "api/manager/login";
}
