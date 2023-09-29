package com.example.xtusicplayer.Views.LandingPage.HomeFrag;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.Model.Callbacks.OnClickCallback;
import com.example.xtusicplayer.Model.MusicRepo.MusicHelper;
import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.R;
import com.example.xtusicplayer.ViewModel.MusicViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private AllMusicAdapter allMusicAdapter;
    private ProgressBar progressBar;
    private MusicEntity currentMusic;
    private OnClickCallback onClickCallback;
    private boolean firstTime = true;
    private MusicHelper musicHelper = new MusicHelper();
    private MusicViewModel musicViewModel;
    private List<MusicEntity> musicList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //casting views
        recyclerView=view.findViewById(R.id.all_recyler_view);
        progressBar=view.findViewById(R.id.progress_bar);

        //vertical Recycler Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        //init
        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);

        //Fetching songs
        showProgressBar();
        musicViewModel.getMusicEntityList().observe(getViewLifecycleOwner(), new Observer<List<MusicEntity>>() {
            @Override
            public void onChanged(List<MusicEntity> musicEntities) {
                musicList = musicEntities;
                setAdapter();
                hideProgressBar();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    private void setAdapter(){

        if(musicList != null){
            allMusicAdapter = new AllMusicAdapter(getContext(),musicList, new AllMusicAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    onClickCallback = (OnClickCallback) getActivity();
                    onClickCallback.shareData(position, new PlaylistEntity("currentPlaylist",musicList));
                }
            });

            recyclerView.setAdapter(allMusicAdapter);
        }
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        showProgressBar();

        // Get a reference to the hosting Activity
        Activity hostActivity = (Activity) adapterView.getContext();

        // Offload time-consuming tasks to a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Update the UI on the UI thread
                hostActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the adapter of the data changes
                        allMusicAdapter.notifyDataSetChanged();

                        // Hide ProgressBar on the UI thread
                        hideProgressBar();
                    }
                });
            }
        }).start();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}