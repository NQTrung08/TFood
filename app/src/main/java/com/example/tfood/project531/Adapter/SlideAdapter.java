package com.example.tfood.project531.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tfood.R;
import com.example.tfood.project531.Model.SlideItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.math.RoundingMode;
import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {
    private List<SlideItem> slideItemList;
     private ViewPager2 viewPager2;

    public SlideAdapter(List<SlideItem> slideItemList, ViewPager2 viewPager2) {
        this.slideItemList = slideItemList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_slide_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImage(slideItemList.get(position));
        if(position == slideItemList.size() - 2){
            viewPager2.post(runnable);

        }

    }

    @Override
    public int getItemCount() {
        return slideItemList.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SlideItem slideItem){
            imageView.setImageResource(slideItem.getImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            slideItemList.addAll(slideItemList);
            notifyDataSetChanged();
        }
    };
}
