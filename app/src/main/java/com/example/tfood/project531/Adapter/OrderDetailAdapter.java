package com.example.tfood.project531.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfood.R;
import com.example.tfood.project531.Model.Order;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    ArrayList<Order> orderArrayList;


    public OrderDetailAdapter(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override

    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.ViewHolder holder, int position) {
        Order order = orderArrayList.get(position);
        ArrayList<String> foodNames = order.getFoodNames();
        String foodNamesString = TextUtils.join(", ", foodNames); // Kết hợp tất cả tên sản phẩm

        // Kiểm tra chiều dài và cắt nếu cần
        if (foodNamesString.length() > 17) {
            foodNamesString = foodNamesString.substring(0, 17) + " ...";
        }

        holder.listFoodNameOrder.setText(foodNamesString);
        holder.numberItem.setText(String.valueOf(order.getTotalQuantities()));
        holder.totalFeeOrder.setText(order.getTotalOrder());

    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView listFoodNameOrder, numberItem, totalFeeOrder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listFoodNameOrder = itemView.findViewById(R.id.listFoodNameOrder);
            numberItem = itemView.findViewById(R.id.numberItem);
            totalFeeOrder = itemView.findViewById(R.id.totalFeeOrder);
        }
    }
}
