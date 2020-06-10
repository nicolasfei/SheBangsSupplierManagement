package com.shebangs.supplier.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.nicolas.library.CustomDatePicker;
import com.nicolas.library.DateFormatUtils;
import com.shebangs.shebangssuppliermanagement.R;

public class SheBangsDialog {
    private static final String TAG = "SheBangsDialog";


    /**
     * 单选框对话框
     *
     * @param title    对话框标题
     * @param context  Context
     * @param items    单选项
     * @param listener 选择监听
     */
    public static void showSingleChoiceDialog(String title, Context context, String[] items, OnChoiceItemListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onChoiceItem(items[which]);
                            dialog.dismiss();
                        }
                    }
                })
                .create().show();
    }

    /**
     * 单选框对话框
     *
     * @param title    对话框标题
     * @param context  Context
     * @param items    单选项
     * @param listener 选择监听
     */
    public static void showSingleChoiceDialog(int title, Context context, String[] items, OnChoiceItemListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(title))
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onChoiceItem(items[which]);
                            dialog.dismiss();
                        }
                    }
                })
                .create().show();
    }

    /**
     * 输入对话框
     *
     * @param title     标题
     * @param inputType 输入类型
     * @param context   Context
     * @param listener  监听
     */
    public static void showEditInputDialog(String title, int inputType, Context context, OnInputFinishListener listener) {
        EditText edit = new EditText(context);
        edit.setSingleLine();
        edit.setInputType(inputType);
        edit.setHint(title);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(edit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldValue = edit.getText().toString();
                        if (!TextUtils.isEmpty(oldValue)) {
                            if (listener != null) {
                                listener.onInputFinish(oldValue);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    /**
     * 输入对话框
     *
     * @param title     标题
     * @param inputType 输入类型
     * @param context   Context
     * @param listener  监听
     */
    public static void showEditInputDialog(int title, int inputType, Context context, OnInputFinishListener listener) {
        EditText edit = new EditText(context);
        edit.setSingleLine();
        edit.setInputType(inputType);
        edit.setHint(title);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(edit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldValue = edit.getText().toString();
                        if (!TextUtils.isEmpty(oldValue)) {
                            if (listener != null) {
                                listener.onInputFinish(oldValue);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    private static CustomDatePicker mTimerPicker;       //时间日期对话框

    /**
     * 显示时间日期对话框
     *
     * @param context  context
     * @param listener listener
     */
    public static void showDateTimePickerDialog(Context context, OnDateTimePickListener listener) {
        String beginTime = "1970-01-01 12:00";
        String nowTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis() + 63072000000L, true);
        Log.d(TAG, "showDateTimePickerDialog: endTime is " + endTime + " start time is " + beginTime);
        if (mTimerPicker == null) {
            // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
            mTimerPicker = new CustomDatePicker(context, new CustomDatePicker.Callback() {
                @Override
                public void onTimeSelected(long timestamp) {
                    if (listener != null) {
                        listener.OnDateTimePick(DateFormatUtils.long2Str(timestamp, true));
                    }
                    mTimerPicker.onDestroy();
                    mTimerPicker = null;
                }
            }, beginTime, endTime);
            // 允许点击屏幕或物理返回键关闭
            mTimerPicker.setCancelable(true);
            // 显示时和分
            mTimerPicker.setCanShowPreciseTime(true);
            // 允许循环滚动
            mTimerPicker.setScrollLoop(false);
            // 允许滚动动画
            mTimerPicker.setCanShowAnim(true);
        }
        mTimerPicker.show("yyyy-MM-dd HH:mm");
        mTimerPicker.setSelectedTime(nowTime, true);
    }

    private static ProgressDialog dialog;

    /**
     * Progress 对话框
     *
     * @param context        Context
     * @param progressString progressString
     */
    public static void showProgressDialog(Context context, String progressString) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage(progressString);
            dialog.create();
        }
        dialog.show();
    }

    /**
     * 取消对话框
     */
    public static void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
    }

    /**
     * 单选返回
     */
    public interface OnChoiceItemListener {
        void onChoiceItem(String itemName);
    }

    /**
     * 字符输入返回
     */
    public interface OnInputFinishListener {
        void onInputFinish(String itemName);
    }

    /**
     * 时间日期选择返回
     */
    public interface OnDateTimePickListener {
        void OnDateTimePick(String dateTime);
    }
}
