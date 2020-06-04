package com.shebangs.supplier.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.MainActivity;
import com.shebangs.supplier.common.OperateResult;
import com.shebangs.supplier.supplier.SupplierKeeper;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ProgressDialog loginDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        /**
         * 监听登陆信息输入
         */
        loginViewModel.getLoginFormState().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(@Nullable OperateResult loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                if (loginFormState.getError() != null) {
                    switch (loginFormState.getError().getErrorCode()) {
                        case -1:
                            usernameEditText.setError(loginFormState.getError().getErrorMsg());
                            break;
                        case -2:
                            passwordEditText.setError(loginFormState.getError().getErrorMsg());
                            break;
                    }
                    loginButton.setEnabled(false);
                }
                if (loginFormState.getSuccess() != null) {
                    loginButton.setEnabled(true);
                }
            }
        });

        /**
         * 监听登陆结果
         */
        loginViewModel.getLoginResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(@Nullable OperateResult loginResult) {
                if (loginResult == null) {
                    dismissLoginDialog();
                    showLoginFailed(R.string.login_failed);
                    return;
                }
                if (loginResult.getError() != null) {
                    dismissLoginDialog();
                    showLoginFailed(loginResult.getError().getErrorMsg());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser();
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    showLoginDialog(getString(R.string.login_ing));
                    String userName = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    loginViewModel.login(userName, password);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
                showLoginDialog(getString(R.string.login_ing));
                String userName = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginViewModel.login(userName, password);
            }
        });
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome) + SupplierKeeper.getInstance().getOnDutySupplier().name;
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showLoginFailed( String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showLoginDialog(String loginMsg) {
        if (loginDialog == null) {
            loginDialog = new ProgressDialog(this);
            loginDialog.setCancelable(false);
            loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        loginDialog.setMessage(loginMsg);
        if (!loginDialog.isShowing()) {
            loginDialog.show();
        }
    }

    private void dismissLoginDialog() {
        if (loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }
}
