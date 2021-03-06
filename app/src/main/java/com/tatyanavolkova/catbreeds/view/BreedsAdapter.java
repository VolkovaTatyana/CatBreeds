package com.tatyanavolkova.catbreeds.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tatyanavolkova.catbreeds.R;
import com.tatyanavolkova.catbreeds.databinding.BreedItemBinding;
import com.tatyanavolkova.catbreeds.pojo.Breed;

import java.util.ArrayList;
import java.util.List;

public class BreedsAdapter extends RecyclerView.Adapter<BreedsAdapter.BreedViewHolder> {

    private final String TAG = "BreedsAdapter";

    private List<Breed> breedList;
    private OnBreedClickListener onBreedClickListener;
    private OnReachEndListener onReachEndListener;
    private final int MAX_SIZE = 67;
    private Context context;

    @NonNull
    @Override
    public BreedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BreedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.breed_item,
                parent, false);
        context = parent.getContext();
        return new BreedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BreedViewHolder holder, int position) {
        if (breedList.size() >= 10 && breedList.size() != MAX_SIZE && position == breedList.size() - 3 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }
        holder.binding.breedName.setText(breedList.get(position).getName());
        if (breedList.get(position).getImageUrl() != null) {
            downloadImageWithRequestListener(holder, breedList.get(position).getImageUrl());
        }
    }

    @Override
    public int getItemCount() {
        return breedList == null ? 0 : breedList.size();
    }

    public void setBreedList(List<Breed> breedList) {
        this.breedList = breedList;
        notifyDataSetChanged();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void downloadImageWithRequestListener(BreedViewHolder holder, String imageURL) {
        Glide.with(context)
                .load(imageURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "ERROR " + e.getMessage());
                        holder.binding.loadingIndicator.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.binding.loadingIndicator.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.binding.smallImage);
    }

    public void setOnBreedClickListener(OnBreedClickListener onBreedClickListener) {
        this.onBreedClickListener = onBreedClickListener;
    }

    interface OnBreedClickListener {
        void onBreedClick(int position);
    }

    interface OnReachEndListener {
        void onReachEnd();
    }

    class BreedViewHolder extends RecyclerView.ViewHolder {
        final BreedItemBinding binding;

        public BreedViewHolder(@NonNull BreedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            View view = binding.getRoot();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBreedClickListener != null) {
                        onBreedClickListener.onBreedClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
