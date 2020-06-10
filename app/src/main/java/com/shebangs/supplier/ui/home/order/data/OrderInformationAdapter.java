package com.shebangs.supplier.ui.home.order.data;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shebangs.shebangssuppliermanagement.R;
import com.shebangs.supplier.app.SupplierApp;

import java.util.List;

public class OrderInformationAdapter extends BaseAdapter {
    private static final String TAG = "OrderInformationAdapter";
    private List<OrderInformation> informationList;
    private Context context;

    private OnOrderChoiceListener orderChoiceListener;

    public OrderInformationAdapter(Context context, List<OrderInformation> list) {
        this.context = context;
        this.informationList = list;
    }

    @Override
    public int getCount() {
        return this.informationList == null ? 0 : this.informationList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.informationList == null ? null : this.informationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.order_information_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderInformation order = informationList.get(position);
        String warehouse = order.warehouse + "\n" + SupplierApp.getInstance().getString(R.string.supplier_admonish);
        holder.warehouse.setText(warehouse);

        String orderTime = context.getString(R.string.order_time) + context.getString(R.string.colon) + order.orderTime;
        holder.orderTime.setText(Html.fromHtml(orderTime, Html.FROM_HTML_MODE_COMPACT));

        String invalidTime = context.getString(R.string.invalid_time) + context.getString(R.string.colon) + order.invalidTime;
        holder.invalidTime.setText(Html.fromHtml(invalidTime, Html.FROM_HTML_MODE_COMPACT));

        String num = context.getString(R.string.num) + context.getString(R.string.colon) + order.goodsNum;
        holder.num.setText(Html.fromHtml(num, Html.FROM_HTML_MODE_COMPACT));

        String shop = context.getString(R.string.branch) + context.getString(R.string.colon) + order.branch;
        holder.shop.setText(Html.fromHtml(shop, Html.FROM_HTML_MODE_COMPACT));

        String orderClass = context.getString(R.string.order_class) + context.getString(R.string.colon) + order.orderClass.toString();
        holder.orderClass.setText(Html.fromHtml(orderClass, Html.FROM_HTML_MODE_COMPACT));

        String goodsID = context.getString(R.string.old_goods_id) + context.getString(R.string.colon) + order.goodsID;
        holder.goodsID.setText(Html.fromHtml(goodsID, Html.FROM_HTML_MODE_COMPACT));

        String newGoodsID = context.getString(R.string.new_goods_id) + context.getString(R.string.colon) + order.newGoodsID;
        holder.newGoodsID.setText(Html.fromHtml(newGoodsID, Html.FROM_HTML_MODE_COMPACT));
        holder.newGoodsID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.expansion) {       //展开状态，关闭
                    holder.goodsContent.setVisibility(View.GONE);
                    order.expansion = false;
                    holder.newGoodsID.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getDrawable(R.drawable.ic_sj_down), null);
                } else {
                    holder.goodsContent.setVisibility(View.VISIBLE);
                    order.expansion = true;
                    holder.newGoodsID.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getDrawable(R.drawable.ic_sj_up), null);
                }
            }
        });

        String bid = context.getString(R.string.bid) + context.getString(R.string.colon) + order.bid;
        holder.bid.setText(Html.fromHtml(bid, Html.FROM_HTML_MODE_COMPACT));

        String printTime;
        if (order.isPrint) {
            printTime = context.getString(R.string.print_time) + context.getString(R.string.colon) + order.printTime;
        } else {
            printTime = context.getString(R.string.no_print);
        }
        holder.printTime.setText(Html.fromHtml(printTime, Html.FROM_HTML_MODE_COMPACT));

        String shipmentTime;
        if (order.status.getType() == ShipmentStatus.SHIPMENT) {
            shipmentTime = context.getString(R.string.shipment_time) + context.getString(R.string.colon) + order.shipmentTime;
        } else {
            shipmentTime = context.getString(R.string.no_shipment);
        }
        holder.shipmentTime.setText(Html.fromHtml(shipmentTime, Html.FROM_HTML_MODE_COMPACT));

        String remark = context.getString(R.string.remark) + context.getString(R.string.colon) + order.remark;
        holder.remark.setText(Html.fromHtml(remark, Html.FROM_HTML_MODE_COMPACT));

        holder.choice.setVisibility(order.canChecked ? View.VISIBLE : View.INVISIBLE);
        holder.choice.setChecked(order.checked);
        holder.choice.setOnCheckedChangeListener((buttonView, isChecked) -> {
            order.checked = isChecked;
            if (orderChoiceListener != null) {
                orderChoiceListener.orderChoice(order);
            }
        });

        //添加商品的属性-----颜色,尺码,数量
        if (holder.goodsContent.getChildCount() > 2) {      //先移除之前加载的数据
            for (int i = 2; i < holder.goodsContent.getChildCount();) {
                holder.goodsContent.removeViewAt(i);
            }
        }
        for (OrderGoodsAttribute attr : order.goodsAttributes) {
            View layout = LayoutInflater.from(this.context).inflate(R.layout.order_goods_information_item, null, false);
            TextView color = layout.findViewById(R.id.color);
            color.setText(attr.color);
            TextView size = layout.findViewById(R.id.size);
            size.setText(attr.size);
            TextView numT = layout.findViewById(R.id.num);
            numT.setText(("x" + attr.num));
            holder.goodsContent.addView(layout);
        }

        holder.goodsContent.setVisibility(order.expansion ? View.VISIBLE : View.GONE);

        return convertView;
    }

    public void setOnOrderChoiceListener(OnOrderChoiceListener listener) {
        this.orderChoiceListener = listener;
    }

    public interface OnOrderChoiceListener {
        void orderChoice(OrderInformation information);
    }

    private static class ViewHolder {
        private TextView warehouse, orderTime, invalidTime, num;
        private TextView shop, orderClass, goodsID, newGoodsID;
        private TextView bid, printTime, shipmentTime, remark;
        private CheckBox choice;
        private LinearLayout goodsContent;

        private ViewHolder(View rootView) {
            this.warehouse = rootView.findViewById(R.id.warehouse);
            this.orderTime = rootView.findViewById(R.id.orderTime);
            this.invalidTime = rootView.findViewById(R.id.invalidTime);
            this.num = rootView.findViewById(R.id.num);

            this.shop = rootView.findViewById(R.id.shop);
            this.orderClass = rootView.findViewById(R.id.orderClass);
            this.goodsID = rootView.findViewById(R.id.goodsID);
            this.newGoodsID = rootView.findViewById(R.id.newGoodsID);

            this.bid = rootView.findViewById(R.id.bid);
            this.printTime = rootView.findViewById(R.id.printTime);
            this.shipmentTime = rootView.findViewById(R.id.shipmentTime);
            this.remark = rootView.findViewById(R.id.remark);

            this.choice = rootView.findViewById(R.id.checkBox);
            this.goodsContent = rootView.findViewById(R.id.goodsContent);
        }
    }
}
