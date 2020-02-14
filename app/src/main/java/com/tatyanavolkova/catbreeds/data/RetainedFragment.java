package com.tatyanavolkova.catbreeds.data;

import android.app.Fragment;
import android.os.Bundle;

import com.tatyanavolkova.catbreeds.pojo.Breed;

import java.util.List;

public class RetainedFragment extends Fragment {

    // data object we want to retain
    private List<Breed> data;
    private int page;
    private boolean resumeDownload;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(List<Breed> data) {
        this.data = data;
    }

    public List<Breed> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isResumeDownload() {
        return resumeDownload;
    }

    public void setResumeDownload(boolean resumeDownload) {
        this.resumeDownload = resumeDownload;
    }
}
