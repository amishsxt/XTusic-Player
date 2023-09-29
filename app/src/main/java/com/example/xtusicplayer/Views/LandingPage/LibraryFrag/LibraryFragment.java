package com.example.xtusicplayer.Views.LandingPage.LibraryFrag;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.Model.Room.Database.AppDatabase;
import com.example.xtusicplayer.R;
import com.example.xtusicplayer.ViewModel.PlaylistViewModel;
import com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist.CreatePlaylistActivity;
import com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist.PlaylistFragment;

import java.util.List;

public class LibraryFragment extends Fragment {

    private LinearLayout sortByBtn;
    private TextView sortByText;
    private ImageView addPlaylistBtn;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private AllPlaylistAdapter allPlaylistAdapter;
    private List<PlaylistEntity> playlistEntityList;
    private PlaylistViewModel playlistViewModel;
    private AppDatabase appDatabase;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        //casting views
        sortByBtn=view.findViewById(R.id.sort_by_btn);
        sortByText=view.findViewById(R.id.sort_by_text);
        addPlaylistBtn=view.findViewById(R.id.add_btn);
        recyclerView=view.findViewById(R.id.recyler_view);
        progressBar=view.findViewById(R.id.progress_bar);

        //init
        appDatabase = AppDatabase.getDB(getContext());
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

        //vertical Recycler Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        hideProgressBar();

        //fetching playlist data
        playlistViewModel.getPlaylistEntityList().observe(getViewLifecycleOwner(), new Observer<List<PlaylistEntity>>() {
            @Override
            public void onChanged(List<PlaylistEntity> playlistEntities) {
                playlistEntityList = playlistEntities;
                setAdapter();
            }
        });

        sortByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        addPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePlaylistActivity.class);
                startActivityForResult(intent, 1);

            }
        });


        return view;
    }

    private void setAdapter(){
        showProgressBar();
        allPlaylistAdapter = new AllPlaylistAdapter(getContext(), playlistEntityList, new AllPlaylistAdapter.OnItemClickListener() {
            @Override
            public void moreOptViewClick(int position) {
                moreOptShowDialog(playlistEntityList.get(position));
            }

            @Override
            public void layoutViewClick(int position) {
                // Get the fragment manager
                FragmentManager fm = getActivity().getSupportFragmentManager();

                // Begin a transaction
                FragmentTransaction ft = fm.beginTransaction();

                // Add the new fragment to the container and specify a tag
                ft.replace(R.id.container_frame, new PlaylistFragment(position));
                ft.addToBackStack(null);

                // Commit the transaction
                ft.commit();
            }
        });

        recyclerView.setAdapter(allPlaylistAdapter);
        hideProgressBar();
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sort_bottomsheet_layout);

        TextView sortNameAsce = dialog.findViewById(R.id.name_asce);
        TextView sortNameDesc = dialog.findViewById(R.id.name_desc);

        sortNameAsce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(getActivity(), sortNameAsce.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        sortNameDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(getActivity(), sortNameDesc.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void moreOptShowDialog(PlaylistEntity pEntity){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.playlist_more_opt_layout);

        TextView playlistName = dialog.findViewById(R.id.playlist_name);
        LinearLayout deleteBtn = dialog.findViewById(R.id.delete_btn);
        playlistName.setText(pEntity.getName());

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appDatabase.playlistDao().deletePlaylist(pEntity);

                //updating data
                playlistEntityList.remove(pEntity);
                allPlaylistAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}