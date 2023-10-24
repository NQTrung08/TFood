package com.example.tfood.project531.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.tfood.project531.Activity.ShowDetailActivity;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.Cart;
import com.example.tfood.project531.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    ArrayList<Food> RecommendedArrayList;

    public PopularAdapter(ArrayList<Food> RecommendedArrayList) {
        this.RecommendedArrayList = RecommendedArrayList;
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommended, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        Food food = RecommendedArrayList.get(position);
        holder.foodName.setText(food.getFoodName());
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
                foodDetailIntent.putExtra("FoodId", "0" + String.valueOf(food.getFoodId())); // Gửi ID của sản phẩm
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

                        // Tăng số lượng sản phẩm trong NotificationBadge lên 1

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
        return RecommendedArrayList.size();
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
