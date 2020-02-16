package com.tatyanavolkova.catbreeds.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
//            StringBuilder stringFieldValue = new StringBuilder();
            SpannableStringBuilder stringFieldValue = new SpannableStringBuilder();
            if (field.getName().equals("name") || field.getName().equals("imageUrl")) continue;
            stringFieldValue.append(splitStringByUpperCaseLetter(field.getName()));
            stringFieldValue.append(": ");
            if (field.getType().equals(String.class)) {
                try {
                    String fieldvalue = (String) field.get(catBreed);
                    if (fieldvalue == null || fieldvalue.isEmpty()) {
                        continue;
                    } else if (fieldvalue.startsWith("http")) { //to make active links
                        SpannableString ss = makeFieldvalueClickable(fieldvalue);
                        stringFieldValue.append(ss);
                    } else {
                        stringFieldValue.append(fieldvalue);
                    }
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
                    stringFieldValue.append(String.valueOf(field.getInt(catBreed)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            TextView tv = new TextView(this, null, 0, R.style.DetailsTextViewStyle);
            tv.setText(stringFieldValue);
            tv.setLayoutParams(params);//added margins
            tv.setMovementMethod(LinkMovementMethod.getInstance()); //to make clicks work

            binding.details.addView(tv);
        }
    }

    private SpannableString makeFieldvalueClickable(String fieldvalue) {
        SpannableString ss = new SpannableString(fieldvalue);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Uri address = Uri.parse(fieldvalue);
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                if (openLinkIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(openLinkIntent);
                } else {
                    Log.e(TAG, "openLinkIntent Error");
                }
            }
        };

        ss.setSpan(clickableSpan, 0, fieldvalue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public void downloadImageWithRequestListener(String imageURL) {
        Glide.with(getApplicationContext())
                .load(imageURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "ERROR " + e.getMessage());
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

    public static String splitStringByUpperCaseLetter(String inputString) {
        StringBuilder result = new StringBuilder();
        result.append(Character.toUpperCase(inputString.charAt(0)));
        for (int i = 1; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append(" ");
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
