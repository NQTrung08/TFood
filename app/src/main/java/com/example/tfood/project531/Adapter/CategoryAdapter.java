package com.example.tfood.project531.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfood.R;
import com.example.tfood.project531.Activity.ListFoodActivity;
import com.example.tfood.project531.Model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<Category> categoryArrayList;

    public CategoryAdapter(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Category category = categoryArrayList.get(position);
        holder.categoryName.setText(category.getCategoryName());
        String picUrl = category.getPic();

        Glide.with(holder.itemView.getContext()).load(picUrl).into(holder.categoryPic);
        // Thêm sự kiện onClickListener cho item danh mục
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý khi một danh mục được chọn
                // Chuyển sang màn hình ListFoodActivity và gửi thông tin menuId tương ứng
                Context context = view.getContext();
                Intent intent = new Intent(context, ListFoodActivity.class);
                intent.putExtra("selectedCategoryId", position + 1); // Gửi thông tin vị trí (position) của danh mục
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic = itemView.findViewById(R.id.categoryPic);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
