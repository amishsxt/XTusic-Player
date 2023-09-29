package com.example.xtusicplayer.Views.LandingPage;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.xtusicplayer.Model.MusicRepo.MusicHelper;
import com.example.xtusicplayer.Model.MusicRepo.MyMediaPlayer;
import com.example.xtusicplayer.Model.Room.DataEntities.MusicEntity;
import com.example.xtusicplayer.Model.Room.DataEntities.PlaylistEntity;
import com.example.xtusicplayer.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {

    private ImageView musicImage,backBtn,pausePlayBtn,nextBtn,previousBtn;
    private TextView musicName,artistName,startTime,endTime;
    private SeekBar seekBar;
    private CardView moreBtn;
    private MusicEntity currentMusic;
    private PlaylistEntity currentPlaylist;
    private MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    private MusicHelper musicHelper = new MusicHelper();

    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //casting views
        musicImage=findViewById(R.id.music_img);
        backBtn=findViewById(R.id.back_btn);
        pausePlayBtn=findViewById(R.id.pause_play_btn);
        nextBtn=findViewById(R.id.next_song_btn);
        previousBtn=findViewById(R.id.previous_song_btn);
        seekBar=findViewById(R.id.seekbar);
        moreBtn=findViewById(R.id.more_option_btn);
        musicName=findViewById(R.id.music_name);
        artistName=findViewById(R.id.artist_name);
        startTime=findViewById(R.id.start_time);
        endTime=findViewById(R.id.end_time);

        //init
        currentPlaylist= (PlaylistEntity) getIntent().getSerializableExtra("currentPlaylist");
        currentPosition = getIntent().getIntExtra("position",0);
        currentMusic= currentPlaylist.getMusicIds().get(currentPosition);

        if(currentMusic != null){
            setCurrentMusicData();
        }

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                startTime.setText(formattedTime(mediaPlayer.getCurrentPosition()/1000));

                if(mediaPlayer.isPlaying()){
                    pausePlayBtn.setImageResource(R.drawable.pause_circle);
                }
                else{
                    pausePlayBtn.setImageResource(R.drawable.play_circle);
                }
                new Handler().postDelayed(this,100);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playNextMusic();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlay();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextMusic();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousMusic();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void playMusic(){
        setCurrentMusicData();
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
        mediaPlayer.stop();

        currentPosition -= 1;
        if(currentPosition>=0){
            currentPosition = currentPosition % currentPlaylist.getMusicIds().size();
        }
        else{
            currentPosition = 0;
        }

        currentMusic = currentPlaylist.getMusicIds().get(currentPosition);

        playMusic();
    }

    private void playNextMusic(){
        mediaPlayer.stop();

        currentPosition = (currentPosition + 1) % currentPlaylist.getMusicIds().size();
        currentMusic = currentPlaylist.getMusicIds().get(currentPosition);

        playMusic();
    }

    private String formattedTime(int position){
        String totalOut = "";
        String totalNew = "";
        String sec = String.valueOf(position%60);
        String min = String.valueOf(position/60);
        totalOut = min + ":" + sec;
        totalNew = min + ":" + "0" + sec;

        if(sec.length()==1){
            return totalNew;
        }
        else{
            return totalOut;
        }
    }

    private void setLocalPicture(String uri){

        Picasso.get()
                .load(uri)
                .error(R.drawable.music_img)
                .into(musicImage);

    }

    private void setCurrentMusicData(){
        musicName.setText(currentMusic.getTittle());
        artistName.setText(currentMusic.getArtist());
        setLocalPicture(currentMusic.getAlbumArtUrl());
        endTime.setText(formattedTime((int)currentMusic.getDuration()/1000));
        seekBar.setMax(mediaPlayer.getDuration());

        sendDataBack();
    }

    private void sendDataBack(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("key","MusicPlayerActivity");
        resultIntent.putExtra("position", currentPosition);
        setResult(Activity.RESULT_OK, resultIntent);
    }

}