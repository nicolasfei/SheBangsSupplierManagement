package com.shebangs.supplier.server.order;

import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;

public class OrderHandler extends OrderInterface {
    @Override
    public String getUrlParam() {
        return OrderHandler;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
