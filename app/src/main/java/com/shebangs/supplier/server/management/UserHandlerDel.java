package com.shebangs.supplier.server.management;

import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;

public class UserHandlerDel extends ManagementInterface {
    @Override
    public String getUrlParam() {
        return UserHandlerDel;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
