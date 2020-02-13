package com.tatyanavolkova.catbreeds.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.FragmentManager;
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
import com.tatyanavolkova.catbreeds.data.RetainedFragment;
import com.tatyanavolkova.catbreeds.databinding.ActivityMainBinding;
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

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private BreedsAdapter adapter;
    private List<Breed> breedList;
    private ActivityMainBinding binding;
    private BreedViewModel viewModel;
    private RetainedFragment retainedFragment; //for saving data when rotate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        breedList = new ArrayList<>();
        adapter = new BreedsAdapter();

        FragmentManager fm = getFragmentManager();
        retainedFragment = (RetainedFragment) fm.findFragmentByTag("data");

        if (retainedFragment != null && retainedFragment.getData() != null) {
            setBreedList(retainedFragment.getData());
            adapter.setBreedList(retainedFragment.getData());
        }

        binding.breedsList.setLayoutManager(new GridLayoutManager(this, 1)); //portrait orientation
        binding.breedsList.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(BreedViewModel.class);
        viewModel.getSingleBreedLiveData().observe(this, new Observer<Breed>() {
            @Override
            public void onChanged(Breed breed) {
                breedList.add(breed);
                Collections.sort(breedList);
                adapter.setBreedList(breedList);
                retainedFragment.setData(breedList);
            }
        });

        // create the fragment and data the first time
        if (retainedFragment == null || retainedFragment.getData() == null) {
            // add the fragment
            retainedFragment = new RetainedFragment();
            fm.beginTransaction().add(retainedFragment, "data").commit();
            // load the data from the web
            viewModel.loadData();
        } else {
            setBreedList(retainedFragment.getData());
            adapter.setBreedList(retainedFragment.getData());
        }

        adapter.setOnBreedClickListener(new BreedsAdapter.OnBreedClickListener() {
            @Override
            public void onBreedClick(int position) {
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("breed", breedList.get(position));
                startActivity(detailIntent);
            }
        });
    }

    public void setBreedList(List<Breed> breedList) {
        this.breedList = breedList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        retainedFragment.setData(breedList);
    }
}