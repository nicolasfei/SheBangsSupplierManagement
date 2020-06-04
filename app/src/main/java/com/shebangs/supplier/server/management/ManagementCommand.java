package com.shebangs.supplier.server.management;

import com.shebangs.supplier.server.Command;
import com.shebangs.supplier.server.CommandTypeEnum;
import com.shebangs.supplier.server.CommandVo;

import static com.shebangs.supplier.server.CommandTypeEnum.COMMAND_SUPPLIER_MANAGEMENT;

public class ManagementCommand extends Command {

    @Override
    public String execute(CommandVo vo) {
        return super.firstNode.handleMessage(vo);
    }

    @Override
    protected void buildDutyChain() {
        ManagementInterface add = new UserHandlerAdd();
        ManagementInterface del = new UserHandlerDel();
        ManagementInterface update = new UserHandlerUpdate();
        ManagementInterface query = new UserHandlerQuery();
        ManagementInterface userQuery = new UserInformationQuery();
        ManagementInterface loginQuery = new UserLoginQuery();

        add.setNextHandler(del);
        del.setNextHandler(update);
        update.setNextHandler(query);
        query.setNextHandler(userQuery);
        userQuery.setNextHandler(loginQuery);
        loginQuery.setNextHandler(null);

        super.firstNode = add;
    }

    @Override
    public CommandTypeEnum getCommandType() {
        return COMMAND_SUPPLIER_MANAGEMENT;
    }
}
