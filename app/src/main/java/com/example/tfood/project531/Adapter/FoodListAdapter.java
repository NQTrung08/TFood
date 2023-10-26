package com.example.tfood.project531.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfood.R;
import com.example.tfood.project531.Activity.CartActivity;
import com.example.tfood.project531.Activity.ListFoodActivity;
import com.example.tfood.project531.Activity.ShowDetailActivity;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.Cart;
import com.example.tfood.project531.Model.Food;
import com.example.tfood.project531.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    ArrayList<Food> foodArrayList;
    User currentUser = Common.currentUser;

    public FoodListAdapter(ArrayList<Food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommended, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.ViewHolder holder, int position) {
        Food food = foodArrayList.get(position);

        String foodName = food.getFoodName();
        String foodNamesString = TextUtils.join(", ", new String[]{foodName}); // Kết hợp tất cả tên sản phẩm

        // Kiểm tra chiều dài và cắt nếu cần
        if (foodNamesString.length() > 17) {
            foodNamesString = foodNamesString.substring(0, 15) + " ...";
        }
        holder.foodName.setText(foodNamesString);
//        holder.foodName.setText(food.getFoodName());

        holder.fee.setText("$ " + food.getFee());
        String picFoodUrl = food.getFoodPic();
        Glide.with(holder.itemView.getContext())
                .load(picFoodUrl)
                .into(holder.foodPic);
        // Trong Adapter, khi item food được nhấn
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent foodDetailIntent = new Intent(context, ShowDetailActivity.class);
                if(food.getFoodId() <= 9) {
                    foodDetailIntent.putExtra("FoodId", "0" + food.getFoodId());
                } else {

                    foodDetailIntent.putExtra("FoodId", String.valueOf(food.getFoodId()));

                }// Gửi ID của sản phẩm
                context.startActivity(foodDetailIntent);
            }
        });

        holder.addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
                cartRef.child(String.valueOf(food.getFoodId())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Cart cartItem = snapshot.getValue(Cart.class);
                            if(cartItem != null ){
                                cartItem.setQuantity(cartItem.getQuantity() + 1);
                                cartItem.setTotalPrice(cartItem.getFee() * cartItem.getQuantity());
                                cartRef.child(String.valueOf(food.getFoodId())).setValue(cartItem);
                            }
                        }else {
                            Cart cartItem = new Cart(food.getFoodId(),food.getFoodName(),food.getFoodPic(), food.getFee(), 1,food.getFee());
                            cartRef.child(String.valueOf(food.getFoodId())).setValue(cartItem);
                        }
                        // Hiển thị thông báo "Added to cart"
                        Toast.makeText(view.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, fee;
        ImageView foodPic, addFoodBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            fee = itemView.findViewById(R.id.fee);
            foodPic = itemView.findViewById(R.id.foodPic);
            addFoodBtn = itemView.findViewById(R.id.addFoodBtn);
        }
    }
}
