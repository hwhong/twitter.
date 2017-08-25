package com.example.hwhong.twitter.Search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.hwhong.twitter.R;
import com.example.hwhong.twitter.Utils.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    public final static String KEY = "SettingsFragment";

    public SearchFragment() {

    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    public static SearchFragment createInstance(int itemsCount) {
        SearchFragment settingsFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, itemsCount);
        settingsFragment.setArguments(bundle);
        return settingsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_search, container, false);
        setupRecyclerView(recyclerView);
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        itemList.add("#twittedev");
        itemList.add("software");
        itemList.add("engineering");
        itemList.add("android");
        itemList.add("development");
        itemList.add("androidO");

        return itemList;
    }

}
