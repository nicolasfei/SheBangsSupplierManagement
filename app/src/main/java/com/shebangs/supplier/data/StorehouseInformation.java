package com.shebangs.supplier.data;


import android.text.TextUtils;

import com.nicolas.categoryexhibition.data.Node;
import com.nicolas.categoryexhibition.data.NodeAttr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * "id": "B1201",
 * "pId": "men",
 * "code": "B1201",
 * "name": "金牛之心12楼1号",
 * "remark": "金牛之心12楼1号",
 * "sort": 100,
 * "valid": "启用"
 */
public class StorehouseInformation extends Node implements Serializable {

    private Node rootNode;

    public StorehouseInformation() {
    }

    public StorehouseInformation(String json) throws JSONException {
        this.rootNode = this.buildTree(json);
    }

    @Override
    public Node buildTree(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        NodeAttr attr = new StorehouseInformationAttr(json);
        List<Node> sons = null;
        if (object.has("children") && !TextUtils.isEmpty(object.getString("children"))) {
            JSONArray array = object.getJSONArray("children");
            sons = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String item = array.getString(i);
                sons.add(new GoodsClass(item));
            }
        }
        Node node = new Node();
        node.setAttr(attr);
        node.setSub(sons);
        return node;
    }

    private class StorehouseInformationAttr extends NodeAttr {
        private static final long serialVersionUID = 6L;
        public String code;     //"B1201",
        public String remark;   //"金牛之心12楼1号",
        public String sort;     //100,
        public String valid;    //"启用"

        private StorehouseInformationAttr(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            super.id = object.getString("id");
            super.pid = object.getString("pId");
            this.code = object.getString("code");
            super.name = object.getString("name");
            this.remark = object.getString("remark");
            this.sort = object.getString("sort");
            this.valid = object.getString("valid");
        }
    }
}
