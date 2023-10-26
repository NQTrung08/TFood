package com.example.tfood.project531.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfood.R;
import com.example.tfood.project531.Activity.CartActivity;
import com.example.tfood.project531.Activity.ShowDetailActivity;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.Cart;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<Cart> cartArrayList;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public CartAdapter(ArrayList<Cart> cartArrayList) {
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart = cartArrayList.get(position);

        String foodName = cart.getFoodName();
        String foodNamesString = TextUtils.join(", ", new String[]{foodName}); // Kết hợp tất cả tên sản phẩm

        // Kiểm tra chiều dài và cắt nếu cần
        if (foodNamesString.length() > 17) {
            foodNamesString = foodNamesString.substring(0, 15) + " ...";
        }
        holder.foodName_cart.setText(foodNamesString);
        holder.feeEachItem.setText("$" + cartArrayList.get(position).getFee());
        holder.totalEachItem.setText("$" + cartArrayList.get(position).getTotalPrice());
        holder.quantity_cart.setText(String.valueOf(cartArrayList.get(position).getQuantity()));
        String picUrl = cart.getFoodPic();
        Glide.with(holder.itemView.getContext()).load(picUrl).into(holder.foodPic_cart);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent foodDetailIntent = new Intent(context, ShowDetailActivity.class);
                if(cart.getFoodId() <= 9) {
                    foodDetailIntent.putExtra("FoodId", "0" + cart.getFoodId());
                } else {

                    foodDetailIntent.putExtra("FoodId", String.valueOf(cart.getFoodId()));

                }// Gửi ID của sản phẩm
                context.startActivity(foodDetailIntent);
            }
        });

        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy mục giỏ hàng tương ứng với vị trí trong danh sách
                Cart cartItem = cartArrayList.get(position);

                int newQuantity = cartItem.getQuantity() + 1;
                cartItem.setQuantity(newQuantity);

                // Cập nhật tổng giá của mục giỏ hàng
                double newTotalPrice = newQuantity * cartItem.getFee();
                cartItem.setTotalPrice(newTotalPrice);

                // Cập nhật dữ liệu trong danh sách giỏ hàng
                cartArrayList.set(position, cartItem);
                notifyDataSetChanged(); // Cập nhật lại RecyclerView
                // Thực hiện cập nhật dữ liệu trong Firebase
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
                cartRef.child(String.valueOf(cartItem.getFoodId())).setValue(cartItem);
            }
        });

        holder.minus_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy mục giỏ hàng tương ứng với vị trí trong danh sách
                Cart cartItem = cartArrayList.get(position);
                if (cartItem.getQuantity() > 1) {

                    int newQuantity = cartItem.getQuantity() - 1;
                    cartItem.setQuantity(newQuantity);

                    // Cập nhật tổng giá của mục giỏ hàng
                    double newTotalPrice = newQuantity * cartItem.getFee();
                    cartItem.setTotalPrice(newTotalPrice);

                    // Cập nhật dữ liệu trong danh sách giỏ hàng
                    cartArrayList.set(position, cartItem);
                    notifyDataSetChanged(); // Cập nhật lại RecyclerView

                    // Thực hiện cập nhật dữ liệu trong Firebase
                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
                    cartRef.child(String.valueOf(cartItem.getFoodId())).setValue(cartItem);
                } else {

                    cartArrayList.remove(position);
                    notifyDataSetChanged(); // Cập nhật lại RecyclerView

                    // Thực hiện xóa dữ liệu trong Firebase
                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
                    cartRef.child(String.valueOf(cartItem.getFoodId())).removeValue();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView feeEachItem, totalEachItem, foodName_cart, quantity_cart, minus_cart, add_cart;
        ImageView foodPic_cart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName_cart = itemView.findViewById(R.id.foodName_cart);
            foodPic_cart = itemView.findViewById(R.id.foodPic_cart);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            quantity_cart = itemView.findViewById(R.id.quantity_cart);
            minus_cart = itemView.findViewById(R.id.minus_cart);
            add_cart = itemView.findViewById(R.id.add_cart);


        }
    }
}
