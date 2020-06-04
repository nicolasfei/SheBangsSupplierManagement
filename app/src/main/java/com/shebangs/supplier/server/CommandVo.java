package com.shebangs.supplier.server;

import java.util.Map;

public class CommandVo {

    public String url;
    public Map<String,String> parameters;
    public String requestMode;
    public String contentType;
    public CommandTypeEnum typeEnum;
}
