package com.shebangs.supplier.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;
import com.shebangs.supplier.common.OperateError;
import com.shebangs.supplier.common.OperateInUserView;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.server.CommandResponse;
import com.shebangs.supplier.server.CommandTypeEnum;
import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.HttpHandler;
import com.shebangs.supplier.server.Invoker;
import com.shebangs.supplier.server.login.LoginInterface;
import com.shebangs.supplier.supplier.SupplierKeeper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<OperateResult> loginFormState = new MutableLiveData<>();
    private MutableLiveData<OperateResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<OperateResult> userInformationResult = new MutableLiveData<>();
    private MutableLiveData<OperateResult> userAccountInformationResult = new MutableLiveData<>();

    public LoginViewModel() {
    }

    public LiveData<OperateResult> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<OperateResult> getLoginResult() {
        return loginResult;
    }

    public LiveData<OperateResult> getUserInformationResult() {
        return userInformationResult;
    }

    public LiveData<OperateResult> getUserAccountInformationResult() {
        return userAccountInformationResult;
    }

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     */
    public void login(String username, String password) {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_SUPPLIER_LOGIN;
        vo.url = LoginInterface.Login;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userName", username);
        parameters.put("password", password);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);

        //保存用户名，密码
        SupplierKeeper.getInstance().getOnDutySupplier().userName = username;
        SupplierKeeper.getInstance().getOnDutySupplier().passWord = password;
    }

    /**
     * 获取用户信息
     */
    public void getUserInformation() {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_SUPPLIER_LOGIN;
        vo.url = LoginInterface.SupplierInformation;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 获取用户账户数据
     */
    public void getUserAccountInformation(){
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_SUPPLIER_LOGIN;
        vo.url = LoginInterface.SupplierAccountInformation;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    Invoker.OnExecResultCallback callback = new Invoker.OnExecResultCallback() {

        @Override
        public void execResult(CommandResponse result) {
            switch (result.url) {
                case LoginInterface.Login:        //登陆
                    if (!result.success) {
                        loginResult.setValue(new OperateResult(new OperateError(-1, SupplierApp.getInstance().getString(R.string.login_failed) + SupplierApp.getInstance().getString(R.string.colon) + result.msg, null)));
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(result.data);
                            SupplierKeeper.getInstance().setToken(jsonObject.getString("token"));
                            loginResult.setValue(new OperateResult(new OperateInUserView(null)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginResult.setValue(new OperateResult(new OperateError(-1, SupplierApp.getInstance().getString(R.string.login_failed), null)));
                        }
                    }
                    break;
                case LoginInterface.SupplierInformation:
                    if (!result.success) {
                        userInformationResult.setValue(new OperateResult(new OperateError(-1, result.msg, null)));
                    } else {
                        try {
                            SupplierKeeper.getInstance().getOnDutySupplier().setSupplierInformation(result.data);
                            userInformationResult.setValue(new OperateResult(new OperateInUserView(null)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            userInformationResult.setValue(new OperateResult(new OperateError(-1, e.getMessage(), null)));
                        }
                    }
                    break;
                case LoginInterface.SupplierAccountInformation:
                    if (!result.success) {
                        userAccountInformationResult.setValue(new OperateResult(new OperateError(-1, result.msg, null)));
                    } else {
                        try {
                            SupplierKeeper.getInstance().getSupplierAccount().setSupplierAccountInformation(result.data);
                            userAccountInformationResult.setValue(new OperateResult(new OperateInUserView(null)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            userAccountInformationResult.setValue(new OperateResult(new OperateError(-1, e.getMessage(), null)));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new OperateResult(new OperateError(-1, SupplierApp.getInstance().getString(R.string.invalid_username), null)));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new OperateResult(new OperateError(-2, SupplierApp.getInstance().getString(R.string.invalid_password), null)));
        } else {
            loginFormState.setValue(new OperateResult(new OperateInUserView(null)));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
