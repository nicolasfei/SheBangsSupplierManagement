package com.shebangs.supplier.data;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shebangs.shebangssuppliermanagement.R;

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
        String warehouse = "<font color=\"black\"><big>" + order.warehouse + "</big></font>";
        holder.warehouse.setText(Html.fromHtml(warehouse, Html.FROM_HTML_MODE_COMPACT));

        String orderTime = context.getString(R.string.order_time) + "\u3000\u3000" + "<font color=\"black\">" + order.orderTime + "</font>";
        holder.orderTime.setText(Html.fromHtml(orderTime, Html.FROM_HTML_MODE_COMPACT));

        String invalidTime = context.getString(R.string.invalid_time) + "\u3000\u3000" + "<font color=\"black\">" + order.invalidTime + "</font>";
        holder.invalidTime.setText(Html.fromHtml(invalidTime, Html.FROM_HTML_MODE_COMPACT));

        String num = context.getString(R.string.num) + "\u3000\u3000" + "<font color=\"red\">" + order.goodsNum + "</font>";
        holder.num.setText(Html.fromHtml(num, Html.FROM_HTML_MODE_COMPACT));

        String shop = context.getString(R.string.branch) + "\u3000\u3000" + "<font color=\"red\">" + order.branch + "</font>";
        holder.shop.setText(Html.fromHtml(shop, Html.FROM_HTML_MODE_COMPACT));

        String orderClass = context.getString(R.string.order_class) + "\u3000\u3000" + "<font color=\"black\">" + order.orderClass.toString() + "</font>";
        holder.orderClass.setText(Html.fromHtml(orderClass, Html.FROM_HTML_MODE_COMPACT));

        String goodsID = context.getString(R.string.old_goods_id) + "\u3000\u3000" + "<font color=\"black\">" + order.goodsID + "</font>";
        holder.goodsID.setText(Html.fromHtml(goodsID, Html.FROM_HTML_MODE_COMPACT));

        String newGoodsID = context.getString(R.string.new_goods_id) + "\u3000\u3000" + "<font color=\"black\">" + order.newGoodsID + "</font>";
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

        String bid = context.getString(R.string.bid) + "\u3000\u3000" + context.getString(R.string.money) + "<font color=\"red\"><big>" + order.bid + "</big></font>";
        holder.bid.setText(Html.fromHtml(bid, Html.FROM_HTML_MODE_COMPACT));

        String printTime;
        if (order.isPrint) {
            printTime = context.getString(R.string.print_time) + "\u3000\u3000" + "<font color=\"black\">" + order.printTime + "</font>";
        } else {
            printTime = context.getString(R.string.no_print);
        }
        holder.printTime.setText(Html.fromHtml(printTime, Html.FROM_HTML_MODE_COMPACT));

        String shipmentTime;
        if (order.status.getType() == OrderStatus.SHIPMENT) {
            shipmentTime = context.getString(R.string.shipment_time) + "\u3000\u3000" + "<font color=\"blue\">" + order.shipmentTime + "</font>";
        } else {
            shipmentTime = "<font color=\"red\">" + order.status.toString() + "</font>";        //context.getString(R.string.no_shipment);
        }
        holder.orderStatus.setText(Html.fromHtml(shipmentTime, Html.FROM_HTML_MODE_COMPACT));

        String remark = context.getString(R.string.remark) + "\u3000\u3000" + order.remark;
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
            for (int i = 2; i < holder.goodsContent.getChildCount(); ) {
                holder.goodsContent.removeViewAt(i);
            }
        }
        for (OrderGoodsAttribute attr : order.goodsAttributes) {
            View layout = LayoutInflater.from(this.context).inflate(R.layout.order_goods_information_item, null, false);
            EditText color = layout.findViewById(R.id.color);
            color.setText(attr.actualColor);
            color.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    attr.actualColor = s.toString();
                }
            });
            EditText size = layout.findViewById(R.id.size);
            size.setText(attr.actualSize);
            size.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    attr.actualSize = s.toString();
                }
            });
            TextView numT = layout.findViewById(R.id.num);
            numT.setText(("x" + attr.actualNum));
            Button reduce = layout.findViewById(R.id.reduce);
            Button add = layout.findViewById(R.id.add);
            reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attr.actualNum--;
                    order.goodsNum--;
                    if (attr.actualNum == 0) {
                        reduce.setBackground(context.getDrawable(R.drawable.ic_reduce_grey));
                        reduce.setClickable(false);
                    }
                    if (attr.actualNum < attr.num && !add.isClickable()) {
                        add.setBackground(context.getDrawable(R.drawable.button_add));
                        add.setClickable(true);
                    }
                    numT.setText(("x" + attr.actualNum));
                    String num = context.getString(R.string.num) + "\u3000\u3000" + "<font color=\"red\">" + order.goodsNum + "</font>";
                    holder.num.setText(Html.fromHtml(num, Html.FROM_HTML_MODE_COMPACT));
                }
            });
            if (attr.actualNum == 0) {
                reduce.setBackground(context.getDrawable(R.drawable.ic_reduce_grey));
                reduce.setClickable(false);
            }
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attr.actualNum++;
                    order.goodsNum++;
                    if (attr.actualNum == attr.num) {
                        add.setBackground(context.getDrawable(R.drawable.ic_add_grey));
                        add.setClickable(false);
                    }
                    if (attr.actualNum > 0 && !reduce.isClickable()) {
                        reduce.setBackground(context.getDrawable(R.drawable.button_reduce));
                        reduce.setClickable(true);
                    }
                    numT.setText(("x" + attr.actualNum));
                    String num = context.getString(R.string.num) + "\u3000\u3000" + "<font color=\"red\">" + order.goodsNum + "</font>";
                    holder.num.setText(Html.fromHtml(num, Html.FROM_HTML_MODE_COMPACT));
                }
            });
            if (attr.actualNum == attr.num) {
                add.setBackground(context.getDrawable(R.drawable.ic_add_grey));
                add.setClickable(false);
            }
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
        private TextView bid, printTime, orderStatus, remark;
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
            this.orderStatus = rootView.findViewById(R.id.orderStatus);
            this.remark = rootView.findViewById(R.id.remark);

            this.choice = rootView.findViewById(R.id.checkBox);
            this.goodsContent = rootView.findViewById(R.id.goodsContent);
        }
    }
}
