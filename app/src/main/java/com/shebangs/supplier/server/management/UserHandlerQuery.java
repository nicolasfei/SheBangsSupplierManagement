package com.shebangs.supplier.server.management;

import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;

public class UserHandlerQuery extends ManagementInterface {
    @Override
    public String getUrlParam() {
        return UserHandlerQuery;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
