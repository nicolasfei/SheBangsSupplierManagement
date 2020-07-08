package com.shebangs.supplier.server.login;


import com.shebangs.supplier.server.Command;
import com.shebangs.supplier.server.CommandTypeEnum;
import com.shebangs.supplier.server.CommandVo;

public class LoginCommand extends Command {

    @Override
    public String execute(CommandVo vo) {
        return super.firstNode.handleMessage(vo);
    }

    @Override
    protected void buildDutyChain() {
        LoginInterface login = new UserLogin();
        LoginInterface information = new SupplierInformation();
        LoginInterface accountInformation = new SupplierAccountInformation();
        LoginInterface logout = new UserLogout();

        login.setNextHandler(information);
        information.setNextHandler(accountInformation);
        accountInformation.setNextHandler(logout);
        logout.setNextHandler(null);

        super.firstNode = login;
    }

    @Override
    public CommandTypeEnum getCommandType() {
        return CommandTypeEnum.COMMAND_SUPPLIER_LOGIN;
    }
}
