package com.shebangs.supplier.server.login;

import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;

public class SupplierAccountInformation extends LoginInterface {
    @Override
    public String getUrlParam() {
        return SupplierAccountInformation;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
