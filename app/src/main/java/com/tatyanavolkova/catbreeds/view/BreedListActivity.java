package com.tatyanavolkova.catbreeds.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.tatyanavolkova.catbreeds.R;
import com.tatyanavolkova.catbreeds.data.RetainedFragment;
import com.tatyanavolkova.catbreeds.databinding.ActivityMainBinding;
import com.tatyanavolkova.catbreeds.network.NetworkStateReceiver;
import com.tatyanavolkova.catbreeds.pojo.Breed;
import java.util.ArrayList;
import java.util.List;

public class BreedListActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private final String TAG = this.getClass().getSimpleName();
    private BreedsAdapter adapter;
    private List<Breed> breedList;
    private ActivityMainBinding binding;
    private BreedViewModel viewModel;
    private RetainedFragment retainedFragment; //for saving data when rotate
    private static int page = 0;
    private static boolean isLoading = false;
    private static boolean resumeDownload = false;

    private FragmentManager fm;

    private NetworkStateReceiver networkStateReceiver;

    public static void setResumeDownload(boolean resumeDownload) {
        BreedListActivity.resumeDownload = resumeDownload;
    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 300;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        breedList = new ArrayList<>();
        adapter = new BreedsAdapter();
        binding.breedsList.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        binding.breedsList.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(BreedViewModel.class);
        fm = getFragmentManager();
        retainedFragment = (RetainedFragment) fm.findFragmentByTag("data");

        getData();

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        viewModel.setOnStartLoadingListener(new BreedViewModel.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
                resumeDownload = false;
                retainedFragment.setResumeDownload(false);
            }

            @Override
            public void onLoadingFailed() {
                isLoading = false;
                resumeDownload = true;
                retainedFragment.setResumeDownload(true);
            }
        });

        viewModel.getBreedListLiveData().observe(this, breeds -> {
            if (breedList == null) {
                setBreedList(breeds);
                isLoading = false;
                page++;
            } else if (!breedList.containsAll(breeds)) {
                breedList.addAll(breeds);
                isLoading = false;
                page++;
            }
            adapter.setBreedList(breedList);
            retainedFragment.setData(breedList);
            retainedFragment.setPage(page);
        });

        adapter.setOnBreedClickListener(position -> {
            Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
            detailIntent.putExtra("breed", breedList.get(position));
            startActivity(detailIntent);
        });
    }

    private void getData() {
        if (retainedFragment == null) {
            // create the fragment and data the first time
            retainedFragment = new RetainedFragment();
            // add the fragment
            fm.beginTransaction().add(retainedFragment, "data").commit();
            // load the data from the web
            viewModel.loadData(page);
        } else {
            setBreedList(retainedFragment.getData());
            setPage(retainedFragment.getPage());
            adapter.setBreedList(breedList);
            setResumeDownload(retainedFragment.isResumeDownload());
        }
        adapter.setOnReachEndListener(() -> {
            if (!isLoading) {
                viewModel.loadData(page);
            }
        });
        if (resumeDownload) {
            viewModel.loadData(page);
        }
    }

    public static void setPage(int page) {
        BreedListActivity.page = page;
    }

    public void setBreedList(List<Breed> breedList) {
        this.breedList = breedList;
    }

    @Override
    public void networkAvailable() {
        getData();
    }

    @Override
    public void networkUnavailable() {
        retainedFragment.setData(breedList);
        retainedFragment.setPage(page);
        retainedFragment.setResumeDownload(resumeDownload);
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        retainedFragment.setData(breedList);
        retainedFragment.setPage(page);
        retainedFragment.setResumeDownload(resumeDownload);

        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }
}
