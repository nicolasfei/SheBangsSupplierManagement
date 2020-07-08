package com.shebangs.supplier.server.common;

import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;

public class StorehouseQuery extends CommonInterface {
    @Override
    public String getUrlParam() {
        return StorehouseQuery;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
