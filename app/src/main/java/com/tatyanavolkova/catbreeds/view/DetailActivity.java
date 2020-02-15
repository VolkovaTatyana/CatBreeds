package com.tatyanavolkova.catbreeds.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tatyanavolkova.catbreeds.R;
import com.tatyanavolkova.catbreeds.databinding.ActivityDetailBinding;
import com.tatyanavolkova.catbreeds.network.ApiFactory;
import com.tatyanavolkova.catbreeds.network.ApiService;
import com.tatyanavolkova.catbreeds.pojo.Breed;
import com.tatyanavolkova.catbreeds.pojo.ImageObject;
import com.tatyanavolkova.catbreeds.pojo.Weight;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Breed catBreed;
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("breed")) {
            Bundle b = intent.getExtras();
            if (b != null) {
                catBreed = (Breed) b.getSerializable("breed");
                setData();
            }
        }
    }

    public void setData() {
        binding.breedName.setText(catBreed.getName());
        downloadImageWithRequestListener(catBreed.getImageUrl());
        //set margins
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.text_view_margin), 0, getResources().getDimensionPixelSize(R.dimen.text_view_margin));
        //reflection
        Field[] classFields = Breed.class.getDeclaredFields();
        for (Field field : classFields) {
            field.setAccessible(true);
            StringBuilder stringFieldValue = new StringBuilder();
            if (field.getName().equals("name") || field.getName().equals("imageUrl")) continue;
            stringFieldValue.append(field.getName());
            stringFieldValue.append(": ");
            if (field.getType().equals(String.class)) {
                try {
                    String fieldvalue = (String) field.get(catBreed);
                    if (fieldvalue == null || fieldvalue.isEmpty()) {
                        continue;
                    }
                    stringFieldValue.append(fieldvalue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (field.getType().equals(Weight.class)) {
                try {
                    if (field.get(catBreed) != null)
                        stringFieldValue.append(field.get(catBreed).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int intFieldValue = field.getInt(catBreed);
                    if (intFieldValue == 0) {
                        continue;
                    }
                    stringFieldValue.append(field.getInt(catBreed));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            TextView tv = new TextView(this, null, 0, R.style.DetailsTextViewStyle);
            tv.setText(stringFieldValue.toString());
            tv.setLayoutParams(params);//added margins

            binding.details.addView(tv);
        }
    }

    public void downloadImageWithRequestListener(String imageURL) {
        Glide.with(getApplicationContext())
                .load(imageURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "ERROR "+ e.getMessage());
                        binding.imageLoadingIndicator.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.imageLoadingIndicator.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.imageCat);
    }
}
