package com.tatyanavolkova.catbreeds.view;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tatyanavolkova.catbreeds.network.ApiFactory;
import com.tatyanavolkova.catbreeds.network.ApiService;
import com.tatyanavolkova.catbreeds.pojo.Breed;
import com.tatyanavolkova.catbreeds.pojo.ImageObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BreedViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;
    private MutableLiveData <List<Breed>> breedLiveData;
    private List<Breed> breedListWithImageUrl;
    private HashMap<String, String> headers = new HashMap<>();
    private ApiService apiService;

    public BreedViewModel(@NonNull Application application) {
        super(application);

        ApiFactory apiFactory = ApiFactory.getInstance();
        apiService = apiFactory.getApiService();

        compositeDisposable = new CompositeDisposable();
        breedLiveData = new MutableLiveData<>();
        breedListWithImageUrl = new ArrayList<>();
        //Authentication params to use in header
        headers.put(ApiFactory.getHeaderKey(), ApiFactory.getApiKey());
    }

    public MutableLiveData<List<Breed>> getBreedLiveData() {
        return breedLiveData;
    }

    private void loadImageObject(Breed breed) {
        StringBuilder stringBuilder = new StringBuilder("https://api.thecatapi.com/v1/images/search?breed_id=");
        stringBuilder.append(breed.getId());
        Disposable disposableImage =
                apiService.getImageObjects(headers, stringBuilder.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<ImageObject>>() {
                            @Override
                            public void accept(List<ImageObject> imageObjects) throws Exception {
                                if (imageObjects != null) {
                                    Log.e("Success 1", imageObjects.get(0).toString());

                                    breed.setImageUrl(imageObjects.get(0).getUrl());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("ERROR 2", throwable.getMessage() + " " + throwable.getLocalizedMessage());
                            }
                        });
        compositeDisposable.add(disposableImage);
    }

    public void loadData(){
        Disposable disposable =
                apiService.getBreeds(headers)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Breed>>() {
                            @Override
                            public void accept(List<Breed> breeds) throws Exception {
                                if (breeds != null) {
                                    breedLiveData.setValue(breeds);
                                    for (Breed breed : breeds) {
                                        loadImageObject(breed);
                                    }

                                    if (!breedListWithImageUrl.isEmpty()) {

                                    }
//                                    adapter.setBreedList(breeds);
//                            setBreedList(breeds);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("ERROR 1", throwable.getMessage());
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
