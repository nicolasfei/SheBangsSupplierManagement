package com.shebangs.supplier.server.management;

import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;

public class UserLoginQuery extends ManagementInterface {
    @Override
    public String getUrlParam() {
        return UserLoginQuery;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
