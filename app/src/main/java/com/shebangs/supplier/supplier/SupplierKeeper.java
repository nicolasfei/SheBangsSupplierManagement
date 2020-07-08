package com.shebangs.supplier.supplier;

import android.util.Log;

import com.nicolas.categoryexhibition.data.Node;
import com.shebangs.supplier.data.BranchInformation;
import com.shebangs.supplier.data.GoodsClass;
import com.shebangs.supplier.data.StorehouseInformation;
import com.shebangs.supplier.server.CommandVo;
import com.shebangs.supplier.server.Invoker;
import com.shebangs.supplier.server.InvokerHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SupplierKeeper {

    private String token;        //token
    private static SupplierKeeper keeper = new SupplierKeeper();

    private Supplier supplier;
    private SupplierAccount account;

    private Node goodsClassTree;        //货物类型树
    private Node storehouseTree;        //库房树
    private List<BranchInformation> branchList = new ArrayList<>();     //分店信息
    private List<String> branchCodeList = new ArrayList<>();            //分店编号信息

    private InformationObserveTask timerTask = new InformationObserveTask();    //信息定时查询任务

    private SupplierKeeper() {
        this.supplier = new Supplier();
        this.account = new SupplierAccount();
    }

    public static SupplierKeeper getInstance() {
        return keeper;
    }

    public Supplier getOnDutySupplier() {
        return supplier;
    }

    public SupplierAccount getSupplierAccount() {
        return account;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Node getGoodsClassTree() {
        synchronized (SupplierKeeper.class) {
            return goodsClassTree;
        }
    }

    public Node getStorehouseTree() {
        synchronized (Supplier.class) {
            return storehouseTree;
        }
    }

    public List<BranchInformation> getBranchList() {
        synchronized (Supplier.class) {
            return branchList;
        }
    }

    public void startTimerTask() {
        this.timerTask.start();
    }

    public void cancelTimerTask() {
        this.timerTask.cancel();
    }

    public List<String> getBranchCodeList() {
        synchronized (Supplier.class) {
            return branchCodeList;
        }
    }

    private class InformationObserveTask {
        private static final String TAG = "InformationObserveTask";
        private long PERIOD = 30 * 60 * 1000;       //30分钟执行一次
        private Timer timer = new Timer();

        private TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //查询商品分类
                CommandVo goodsVo = InvokerHandler.getInstance().getGoodsClassCommand(null);
                String goodClass = Invoker.getInstance().synchronousExec(goodsVo);
                Node goodsTree;
                try {
                    JSONObject object = new JSONObject(goodClass);
                    if (object.getBoolean("success")) {
                        if (object.has("jsonData")) {
                            Log.d(TAG, "run: get goods class:" + object.getString("jsonData"));
                            GoodsClass goods = new GoodsClass();
                            goodsTree = goods.buildTree(object.getString("jsonData"));
                            synchronized (SupplierKeeper.class) {
                                goodsClassTree = goodsTree;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //查询库房
                CommandVo storehouseVo = InvokerHandler.getInstance().getStorehouseInformationCommand();
                String storehouse = Invoker.getInstance().synchronousExec(storehouseVo);
                Node storeTree;
                try {
                    JSONObject object = new JSONObject(storehouse);
                    if (object.getBoolean("success")) {
                        if (object.has("jsonData")) {
                            Log.d(TAG, "run: get storehouse information:" + object.getString("jsonData"));
                            StorehouseInformation store = new StorehouseInformation();
                            storeTree = store.buildTree(object.getString("jsonData"));
                            synchronized (SupplierKeeper.class) {
                                storehouseTree = storeTree;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //查询分店
                int currentPage = 1;
                int pageSize = 200;
                int totalPage = 1000;
                List<BranchInformation> branchInformationList = new ArrayList<>();
                try {
                    for (int i = currentPage; i < (totalPage % pageSize == 0 ? totalPage / pageSize : totalPage / pageSize + 1); i++) {
                        CommandVo branchVo = InvokerHandler.getInstance().getBranchInformationCommand(currentPage, pageSize);
                        String branch = Invoker.getInstance().synchronousExec(branchVo);

                        JSONObject object = new JSONObject(branch);
                        if (object.getBoolean("success")) {
                            JSONArray array = object.getJSONArray("data");
                            for (int j = 0; j < array.length(); j++) {
                                branchInformationList.add(new BranchInformation(array.getString(i)));
                            }
                            totalPage = object.getInt("total"); //获取总页数
                        }
                    }
                    synchronized (SupplierKeeper.class) {
                        branchList.clear();
                        branchList.addAll(branchInformationList);
                        branchCodeList.clear();
                        for (BranchInformation branch : branchInformationList) {
                            branchCodeList.add(branch.fId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        private void start() {
            this.timer.schedule(task, 3000, PERIOD);
        }

        private void cancel() {
            this.timer.cancel();
        }
    }
}

