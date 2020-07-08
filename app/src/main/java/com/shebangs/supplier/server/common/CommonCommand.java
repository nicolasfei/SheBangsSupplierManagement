package com.shebangs.supplier.server.common;

import com.shebangs.supplier.server.Command;
import com.shebangs.supplier.server.CommandTypeEnum;
import com.shebangs.supplier.server.CommandVo;

public class CommonCommand extends Command {
    @Override
    public String execute(CommandVo vo) {
        return super.firstNode.handleMessage(vo);
    }

    @Override
    protected void buildDutyChain() {
        CommonInterface goodsClassQuery = new GoodsClassQuery();
        CommonInterface branchQuery = new BranchQuery();
        CommonInterface storehouseQuery = new StorehouseQuery();

        goodsClassQuery.setNextHandler(branchQuery);
        branchQuery.setNextHandler(storehouseQuery);
        storehouseQuery.setNextHandler(null);

        super.firstNode = goodsClassQuery;
    }

    @Override
    public CommandTypeEnum getCommandType() {
        return CommandTypeEnum.COMMAND_COMMON;
    }
}
