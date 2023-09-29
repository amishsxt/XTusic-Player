package com.example.xtusicplayer.Views.LandingPage;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.xtusicplayer.Model.Callbacks.MusicFetchCallback;
import com.example.xtusicplayer.Model.Callbacks.OnClickCallback;
import com.example.xtusicplayer.Model.MusicRepo.MusicHelper;
import com.example.xtusicplayer.Model.MusicRepo.MyMediaPlayer;
import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.Model.Room.Database.AppDatabase;
import com.example.xtusicplayer.R;
import com.example.xtusicplayer.ViewModel.MusicViewModel;
import com.example.xtusicplayer.ViewModel.PlaylistViewModel;
import com.example.xtusicplayer.Views.LandingPage.HomeFrag.HomeFragment;
import com.example.xtusicplayer.Views.LandingPage.LibraryFrag.LibraryFragment;
import com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist.PlaylistFragment;
import com.example.xtusicplayer.Views.LandingPage.SearchFrag.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LandingPageActivity extends AppCompatActivity implements OnClickCallback {

    private BottomNavigationView bottomNavigationView;
    private ImageView musicCover, pausePlayBtn,nextBtn;
    private FrameLayout musicBar, container;
    private TextView mTittle,mArtist;

    private SeekBar seekBar;
    private MusicEntity currentMusic;
    private PlaylistEntity currentPlaylist;
    private MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    private MusicHelper musicHelper = new MusicHelper();

    private AppDatabase appDatabase;
    private MusicViewModel musicViewModel;
    private PlaylistViewModel playlistViewModel;
    private List<MusicEntity> musicEntityList;
    private List<PlaylistEntity> playlistEntityList;
    private int currentPosition;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private LibraryFragment libraryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        //casting views
        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        seekBar=findViewById(R.id.seekbar);
        mTittle=findViewById(R.id.music_tittle);
        mArtist=findViewById(R.id.artist_name);
        musicCover=findViewById(R.id.music_img);
        pausePlayBtn=findViewById(R.id.pause_play_btn);
        musicBar=findViewById(R.id.status_bar);
        container=findViewById(R.id.container_frame);

        //Setting up database & fetching data
        appDatabase = AppDatabase.getDB(this);
        fetchLocalMusic();

        //init
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        libraryFragment = new LibraryFragment();

        // Show the initial fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_frame, homeFragment)
                .add(R.id.container_frame, searchFragment)
                .add(R.id.container_frame, libraryFragment)
                .hide(searchFragment)
                .hide(libraryFragment)
                .commit();

        // Set up the bottom navigation item click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
            if(topFragment!=homeFragment && topFragment!=searchFragment && topFragment!=libraryFragment){
                getSupportFragmentManager().popBackStack();
            }

            switch (item.getItemId()) {
                case R.id.nav_home:
                    transaction.show(homeFragment);
                    transaction.hide(searchFragment);
                    transaction.hide(libraryFragment);
                    break;
                case R.id.nav_search:
                    transaction.show(searchFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(libraryFragment);
                    break;
                case R.id.nav_library:
                    transaction.show(libraryFragment);
                    transaction.hide(searchFragment);
                    transaction.hide(homeFragment);
                    break;
            }

            transaction.commit();
            return true;
        });

        LandingPageActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());

                    if(mediaPlayer.isPlaying()){
                        pausePlayBtn.setImageResource(R.drawable.pause);
                    }
                    else{
                        pausePlayBtn.setImageResource(R.drawable.play_arrow);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });

        //disabling seekbar touch
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        //musicBar click
        musicBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LandingPageActivity.this, MusicPlayerActivity.class);
                intent.putExtra("currentPlaylist", currentPlaylist);
                intent.putExtra("position",currentPosition);
                startActivityForResult(intent,1);
            }
        });

        if(mediaPlayer!=null && currentPlaylist!=null){
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playNextMusic();
                }
            });
        }
    }

    @Override
    public void shareData(int positon, PlaylistEntity cPlaylist) {

        currentPlaylist = cPlaylist;
        currentPosition = positon;
        currentMusic = currentPlaylist.getMusicIds().get(currentPosition);

        setCurrentMusicData();

        pausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlay();
            }
        });

        //changing music
        setMediaPlayerComplete();

        playMusic();
    }

    // In FirstActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                String resultData = data.getStringExtra("key");

                if(resultData.equals("CreatePlaylistActivity") ){
                    String receivedData = data.getStringExtra("playlistName");

                    if(!receivedData.trim().isEmpty()){

                        int position = appDatabase.playlistDao().countDataRow();

                        PlaylistEntity newPlaylist = new PlaylistEntity();
                        newPlaylist.setName(receivedData);
                        newPlaylist.setMusicIds(new ArrayList<>());

                        appDatabase.playlistDao().insertPlaylist(newPlaylist);

                        // Show a new fragment above the existing ones
                        showNewFragment(new PlaylistFragment(position));
                    }
                }
                else if(resultData.equals("MusicPlayerActivity")){
                    currentPosition = data.getIntExtra("position",0);
                    currentMusic = currentPlaylist.getMusicIds().get(currentPosition);
                    setCurrentMusicData();
                    setMediaPlayerComplete();
                }
            }
        }
    }


    private void setLocalPicture(String uri){

        Picasso.get()
                .load(uri)
                .error(R.drawable.music_img)
                .into(musicCover);

    }

    private void playMusic(){

        setCurrentMusicData();

        musicBar.setVisibility(View.VISIBLE);
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentMusic.getData());
            mediaPlayer.prepare();
            mediaPlayer.start();

            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private void pausePlay(){

        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.start();
        }
    }

    private void playPreviousMusic(){

    }

    private void playNextMusic(){
        mediaPlayer.stop();

        currentPosition = (currentPosition + 1) % currentPlaylist.getMusicIds().size();
        currentMusic = currentPlaylist.getMusicIds().get(currentPosition);

        playMusic();
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    private void showNewFragment(Fragment frag) {

        // Get the fragment manager
        FragmentManager fm = getSupportFragmentManager();

        // Begin a transaction
        FragmentTransaction ft = fm.beginTransaction();

        // Add the new fragment to the container and specify a tag
        ft.replace(R.id.container_frame, frag);
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

    private void fetchLocalMusic(){
        //Fetching songs
        if(appDatabase.musicDao().countDataRow() == 0){
            musicHelper.getAllMP3Files(this, new MusicFetchCallback() {
                @Override
                public void onMusicFetchComplete() {
                    Toast.makeText(LandingPageActivity.this, "Fetched local music's", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMusicFetchFailed(String messgae) {
                    Toast.makeText(LandingPageActivity.this, messgae, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setMediaPlayerComplete(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playNextMusic();
            }
        });
    }

    private void setCurrentMusicData(){
        mTittle.setText(currentMusic.getTittle());
        mArtist.setText(currentMusic.getArtist());
        setLocalPicture(currentMusic.getAlbumArtUrl());
        seekBar.setMax(mediaPlayer.getDuration());
    }
}