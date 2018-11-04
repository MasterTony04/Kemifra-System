package com.fahamutech.doctorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.fahamutech.doctorapp.R;
import com.fahamutech.doctorapp.model.Testimony;

public class SimpleImageViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_image_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle("Image Viewer");
            supportActionBar.setSubtitle("Pinch To Zoom");
        }

        Intent intent = getIntent();
        ImageView imageView = findViewById(R.id.image_image);
        if (intent != null) {
            Testimony testimony = (Testimony) intent.getSerializableExtra("_image_");
            Glide.with(this).load(testimony.getImage()).into(imageView);

            //pinch to zoom
            imageView.setOnTouchListener(new ImageMatrixTouchHandler(imageView.getContext()));
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Share project", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

    }

}
