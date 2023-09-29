package com.example.xtusicplayer.Views.SplashScreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.xtusicplayer.R;
import com.example.xtusicplayer.Views.LandingPage.LandingPageActivity;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Casting views
        logo = findViewById(R.id.logo);

        // Fade-in animation
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeInAnimation.setDuration(900);
        logo.startAnimation(fadeInAnimation);

        // Use a Handler to delay permission check
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission();
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, navigate to the nextActivity
                nextActivity();
            } else {
                // Handle permission denied case
                Toast.makeText(this, "Permission is needed to proceed further", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermission() {
        int result = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, navigate to the nextActivity
            nextActivity();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(SplashActivity.this, "Allow Read permission from settings!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(SplashActivity.this, LandingPageActivity.class);
        startActivity(intent);
        finish();
    }
}