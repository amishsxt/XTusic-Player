package com.example.xtusicplayer.Views.LandingPage.LibraryFrag.Playlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xtusicplayer.R;

public class CreatePlaylistActivity extends AppCompatActivity {

    private EditText playlistName;
    private TextView cancelBtn,createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        //casting views
        cancelBtn=findViewById(R.id.cancel_btn);
        createBtn=findViewById(R.id.create_btn);
        playlistName=findViewById(R.id.playlist_edit_name);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playlistName.getText().toString().isEmpty()){
                    Toast.makeText(CreatePlaylistActivity.this, "Enter playlist name!", Toast.LENGTH_SHORT).show();
                }
                else {

                    String sr = playlistName.getText().toString();
                    Toast.makeText(CreatePlaylistActivity.this, "Data sent", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("playlistName", playlistName.getText().toString());
                    resultIntent.putExtra("key","CreatePlaylistActivity");
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}