package com.fahamutech.doctorapp.forum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.fahamutech.doctorapp.R;

public class ForumImageViwer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_image_viwer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle("Image Viewer");
            supportActionBar.setSubtitle("Pinch To Zoom");
        }

        Intent intent = getIntent();
        ImageView imageView = findViewById(R.id.forum_image_image);
        if (intent != null) {
            String string = intent.getStringExtra("_image_");
            Glide.with(this).load(string).into(imageView);

            //pinch to zoom
            imageView.setOnTouchListener(new ImageMatrixTouchHandler(imageView.getContext()));
        }

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
    }

}
