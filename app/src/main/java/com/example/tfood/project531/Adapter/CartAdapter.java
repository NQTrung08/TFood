package com.example.tfood.project531.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfood.R;
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
        holder.foodName_cart.setText(cartArrayList.get(position).getFoodName());
        holder.feeEachItem.setText("$" + cartArrayList.get(position).getFee());
        holder.totalEachItem.setText("$" + cartArrayList.get(position).getTotalPrice());
        holder.quantity_cart.setText(String.valueOf(cartArrayList.get(position).getQuantity()));
        String picUrl = cart.getFoodPic();
        Glide.with(holder.itemView.getContext()).load(picUrl).into(holder.foodPic_cart);

        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy mục giỏ hàng tương ứng với vị trí trong danh sách
                Cart cartItem = cartArrayList.get(position);

                // Tăng số lượng sản phẩm cho mục giỏ hàng này
                int newQuantity = cartItem.getQuantity() + 1;
                cartItem.setQuantity(newQuantity);

                // Cập nhật tổng giá của mục giỏ hàng
                double newTotalPrice = newQuantity * cartItem.getFee();
                cartItem.setTotalPrice(newTotalPrice);

                // Cập nhật dữ liệu trong danh sách giỏ hàng
                cartArrayList.set(position, cartItem);
                // Cập nhật giao diện người dùng
                notifyDataSetChanged(); // Cập nhật lại RecyclerView
                // Sau đó, bạn cũng có thể cập nhật dữ liệu trong cơ sở dữ liệu Firebase nếu cần
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

                // Kiểm tra số lượng sản phẩm trước khi giảm
                if (cartItem.getQuantity() > 1) {
                    // Giảm số lượng sản phẩm cho mục giỏ hàng này
                    int newQuantity = cartItem.getQuantity() - 1;
                    cartItem.setQuantity(newQuantity);

                    // Cập nhật tổng giá của mục giỏ hàng
                    double newTotalPrice = newQuantity * cartItem.getFee();
                    cartItem.setTotalPrice(newTotalPrice);

                    // Cập nhật dữ liệu trong danh sách giỏ hàng
                    cartArrayList.set(position, cartItem);

                    // Cập nhật giao diện người dùng
                    notifyDataSetChanged(); // Cập nhật lại RecyclerView

                    // Thực hiện cập nhật dữ liệu trong Firebase
                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
                    cartRef.child(String.valueOf(cartItem.getFoodId())).setValue(cartItem);
                } else {
                    // Nếu số lượng bằng 1, xóa mục khỏi giỏ hàng
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
