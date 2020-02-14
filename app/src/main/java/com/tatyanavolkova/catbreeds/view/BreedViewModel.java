package com.tatyanavolkova.catbreeds.view;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tatyanavolkova.catbreeds.R;
import com.tatyanavolkova.catbreeds.network.ApiFactory;
import com.tatyanavolkova.catbreeds.network.ApiService;
import com.tatyanavolkova.catbreeds.pojo.Breed;
import com.tatyanavolkova.catbreeds.pojo.ImageObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BreedViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();

    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Breed>> breedListLiveData;
    private HashMap<String, String> headers = new HashMap<>();
    private ApiService apiService;
    private Disposable disposable;
    private Context context;

    private OnStartLoadingListener onStartLoadingListener;

    public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
        this.onStartLoadingListener = onStartLoadingListener;
    }

    public BreedViewModel(@NonNull Application application) {
        super(application);

        ApiFactory apiFactory = ApiFactory.getInstance();
        apiService = apiFactory.getApiService();
        context = application.getApplicationContext();
        compositeDisposable = new CompositeDisposable();
        breedListLiveData = new MutableLiveData<>();
        //Authentication params to use in header
        headers.put(ApiFactory.getHeaderKey(), ApiFactory.getApiKey());
    }

    public MutableLiveData<List<Breed>> getBreedListLiveData() {
        return breedListLiveData;
    }

    public void loadData(int page) {
        Log.e(TAG, "loadData, addDefaultNetworkActiveListener " + page);
        if (onStartLoadingListener != null) {
            onStartLoadingListener.onStartLoading();
        }
        StringBuilder stringBuilder = new StringBuilder("https://api.thecatapi.com/v1/breeds?limit=10&page=");
        stringBuilder.append(page); //# of page
        disposable =
                apiService.getBreeds(headers, stringBuilder.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(breeds -> {
                            if (breeds != null) {
                                for (Breed breed : breeds) {
                                    loadImageObject(breed, breeds);
                                }
                                Log.e(TAG, "loadData " + breeds.toString());
                            }
                        }, throwable -> {
                            Log.e(TAG, "loadData, ERROR " + throwable.getMessage());
                            Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                        });
        compositeDisposable.add(disposable);
    }

    private void loadImageObject(Breed breed, List<Breed> breeds) {

        StringBuilder stringBuilder = new StringBuilder("https://api.thecatapi.com/v1/images/search?size=small&breed_id=");
        stringBuilder.append(breed.getId());
        Disposable disposableImage =
                apiService.getImageObjects(headers, stringBuilder.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(imageObjects -> {
                            if (imageObjects != null) {
                                breed.setImageUrl(imageObjects.get(0).getUrl());
                                breedListLiveData.setValue(breeds);
                            }
                        }, throwable -> {
                            Log.e(TAG, "loadImageObject, ERROR " + throwable.getMessage() + " " + throwable.getLocalizedMessage());
                            Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                        });
        compositeDisposable.add(disposableImage);
    }

    public interface OnStartLoadingListener {
        void onStartLoading();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
