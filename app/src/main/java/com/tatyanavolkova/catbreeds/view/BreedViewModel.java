package com.tatyanavolkova.catbreeds.view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BreedViewModel extends AndroidViewModel {

    private final String TAG = "BreedViewModel";

    private CompositeDisposable compositeDisposable;
    private MutableLiveData<Breed> singleBreedLiveData;
    private HashMap<String, String> headers = new HashMap<>();
    private ApiService apiService;

    public BreedViewModel(@NonNull Application application) {
        super(application);

        ApiFactory apiFactory = ApiFactory.getInstance();
        apiService = apiFactory.getApiService();

        compositeDisposable = new CompositeDisposable();
        singleBreedLiveData = new MutableLiveData<>();
        //Authentication params to use in header
        headers.put(ApiFactory.getHeaderKey(), ApiFactory.getApiKey());
    }

    public MutableLiveData<Breed> getSingleBreedLiveData() {
        return singleBreedLiveData;
    }

    private void loadImageObject(Breed breed) {
        StringBuilder stringBuilder = new StringBuilder("https://api.thecatapi.com/v1/images/search?size=small&breed_id=");
        stringBuilder.append(breed.getId());
        Disposable disposableImage =
                apiService.getImageObjects(headers, stringBuilder.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<ImageObject>>() {
                            @Override
                            public void accept(List<ImageObject> imageObjects) throws Exception {
                                if (imageObjects != null) {
                                    Log.e(TAG, "loadImageObject, success " + imageObjects.get(0).toString());
                                    breed.setImageUrl(imageObjects.get(0).getUrl());
                                    singleBreedLiveData.setValue(breed);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "loadImageObject, ERROR " + throwable.getMessage() + " " + throwable.getLocalizedMessage());
                            }
                        });
        compositeDisposable.add(disposableImage);
    }

    public void loadData() {
        Disposable disposable =
                apiService.getBreeds(headers)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Breed>>() {
                            @Override
                            public void accept(List<Breed> breeds) throws Exception {
                                if (breeds != null) {
                                    for (Breed breed : breeds) {
                                        loadImageObject(breed);
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "loadData, ERROR " + throwable.getMessage());
                            }
                        });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
