package com.tatyanavolkova.catbreeds.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tatyanavolkova.catbreeds.R;
import com.tatyanavolkova.catbreeds.databinding.ActivityMainBinding;
import com.tatyanavolkova.catbreeds.network.ApiFactory;
import com.tatyanavolkova.catbreeds.network.ApiService;
import com.tatyanavolkova.catbreeds.pojo.Breed;
import com.tatyanavolkova.catbreeds.pojo.ImageObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private BreedsAdapter adapter;
    private List<Breed> breedList;
    private List<ImageObject> imageObjects;
    private ActivityMainBinding binding;
    private CompositeDisposable compositeDisposable;
    private BreedViewModel viewModel;

    public void setImageObjects(List<ImageObject> imageObjects) {
        this.imageObjects = imageObjects;
    }

    public void setBreedList(List<Breed> breedList) {
        this.breedList = breedList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new BreedsAdapter();
        binding.breedsList.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(BreedViewModel.class);
        viewModel.getBreedLiveData().observe(this, new Observer<List<Breed>>() {
            @Override
            public void onChanged(List<Breed> breeds) {
                adapter.setBreedList(breeds);
                setBreedList(breeds);
            }
        });
        viewModel.loadData();

        adapter.setOnBreedClickListener(new BreedsAdapter.OnBreedClickListener() {
            @Override
            public void onBreedClick(int position) {
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("breed", breedList.get(position));
                startActivity(detailIntent);
            }
        });


/*        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();*/

       /* //Authentication param to use in header
        HashMap<String, String> headers = new HashMap<>();
        headers.put(ApiFactory.getHeaderKey(), ApiFactory.getApiKey());
        Disposable disposable =
        apiService.getBreeds(headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Breed>>() {
                    @Override
                    public void accept(List<Breed> breeds) throws Exception {
                        if (breeds != null) {
                            adapter.setBreedList(breeds);
//                            setBreedList(breeds);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR 1", throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);*/

        /*Disposable disposable1 =
        apiService.getImageObjects(headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ImageObject>>() {
                    @Override
                    public void accept(List<ImageObject> imageObjects) throws Exception {
                        if (imageObjects != null) {
                            Log.e("Success 1", imageObjects.get(0).toString());
                            downloadImageWithRequestListener(imageObjects.get(0).getImageUrl());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR 2", throwable.getMessage() + " " + throwable.getLocalizedMessage());
                    }
                });
        compositeDisposable.add(disposable1);*/
    }

    public void downloadImageWithRequestListener(String imageURL) {
        Glide.with(getApplicationContext())
                .load(imageURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("ERROR", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .apply(new RequestOptions())
                .into(image);
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }
}
