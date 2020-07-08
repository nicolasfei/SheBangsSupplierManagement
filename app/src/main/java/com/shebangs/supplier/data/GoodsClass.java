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
 * "id": "10",
 * "pId": "3",
 * "name": "上衣",
 * "sort": 100,
 * "valid": "启用"
 */
public class GoodsClass extends Node {
    private Node rootNode;

    public GoodsClass(String json) throws JSONException {
        this.rootNode = this.buildTree(json);
    }

    public GoodsClass() {

    }

    @Override
    public Node buildTree(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        NodeAttr attr = new GoodsClassAttr(json);
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

    public class GoodsClassAttr extends NodeAttr implements Serializable {
        private static final long serialVersionUID = 5L;
        //    public String id;//"10",
        //    public String pId;//"3",
        //    public String name;//"上衣",
        public String sort;//100,
        public String valid;//"启用"

        private GoodsClassAttr(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            super.id = object.getString("id");
            super.pid = object.getString("pId");
            super.name = object.getString("name");
            this.sort = object.getString("sort");
            this.valid = object.getString("valid");
        }
    }
}
