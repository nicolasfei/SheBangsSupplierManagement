package com.shebangs.supplier.server.order;

import com.shebangs.supplier.server.Command;
import com.shebangs.supplier.server.CommandTypeEnum;
import com.shebangs.supplier.server.CommandVo;

import static com.shebangs.supplier.server.CommandTypeEnum.COMMAND_SUPPLIER_ORDER;

public class OrderCommand extends Command {

    @Override
    public String execute(CommandVo vo) {
        return super.firstNode.handleMessage(vo);
    }

    @Override
    protected void buildDutyChain() {
        OrderInterface query = new OrderQuery();
        OrderInterface handler = new OrderHandler();
        query.setNextHandler(handler);
        handler.setNextHandler(null);

        super.firstNode = query;
    }

    @Override
    public CommandTypeEnum getCommandType() {
        return COMMAND_SUPPLIER_ORDER;
    }
}
