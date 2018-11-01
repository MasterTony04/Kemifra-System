package com.fahamutech.adminapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.model.Article;

public class ReadActivity extends AppCompatActivity {
    private Toolbar toolbar;
    //private FloatingActionButton fab;
    private TextView title;
    private TextView content;
    private ImageView image;
//    private ImageView favorite;
//    private ImageView share;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        bindView();

        findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        Article article = (Article) getIntent().getSerializableExtra("_article");
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            if (article != null) supportActionBar.setTitle(article.getTitle());
        }

        //load article
        if (article != null) {
            iniView(article);
        }

        //testing
//        fab.setOnClickListener(view -> {
//            Snackbar.make(view, "Share opening...", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//            // Intent intent = new Intent(Intent.ACTION_SEND);
//            //startActivity(new Intent(this, ImageActivity.class));
//        });
    }

    private void iniView(Article article) {
        title.setText(article.getTitle());
        content.setText(article.getContent());
        Glide.with(this).load(article.getImage()).into(image);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        //fab = findViewById(R.id.fab);
        content = findViewById(R.id.read_content);
        title = findViewById(R.id.read_title);
        image = findViewById(R.id.read_image);
//        favorite = findViewById(R.id.read_like);
//        share = findViewById(R.id.read_share);
    }

}
