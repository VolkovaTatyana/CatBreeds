package com.tatyanavolkova.catbreeds.network;

import com.tatyanavolkova.catbreeds.pojo.Breed;
import com.tatyanavolkova.catbreeds.pojo.ImageObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @GET("breeds")
    Observable<List<Breed>> getBreeds(@HeaderMap Map<String, String> headers);

    @GET
    Observable<List<ImageObject>> getImageObjects(@HeaderMap Map<String, String> headers,
                                                  @Url String url);

/*    @GET("images/search?breed_id={breed_id}")
    Observable<List<ImageObject>> getImageObjects(@HeaderMap Map<String, String> headers,
                                                  @Path(value = "breed_id", encoded = true) String id);*/
}
