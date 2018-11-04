package com.fahamutech.doctorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fahamutech.doctorapp.R;
import com.fahamutech.doctorapp.model.Article;

public class ReadActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView title;
    private TextView content;
    private ImageView image;
    private Article article;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        bindView();

        findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        article = (Article) getIntent().getSerializableExtra("_article");
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            if (article != null) supportActionBar.setTitle("Learn");
        }

        //load article
        if (article != null) {
            iniView(article);
        }

        //testing
        fab.setOnClickListener(this::share);
    }

    private void iniView(Article article) {
        title.setText(article.getTitle());
        content.setText(article.getContent());
        Glide.with(this).load(article.getImage()).into(image);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        content = findViewById(R.id.read_content);
        title = findViewById(R.id.read_title);
        image = findViewById(R.id.read_image);
//        favorite = findViewById(R.id.read_like);
//        share = findViewById(R.id.read_share);
    }

    private void share(View view){
        String message;
        if(article!=null){
            message="Nimejifunza "+article.getTitle()+", apa "+getString(R.string.playapp);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }else {
            Snackbar.make(view, "Share fail...", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

    }

}
