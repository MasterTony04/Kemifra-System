package com.fahamutech.adminapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.database.connector.ArticleDataSource;
import com.fahamutech.adminapp.database.noSql.ArticlesNoSqlDatabase;
import com.fahamutech.adminapp.database.noSql.HomeNoSqlDatabase;
import com.fahamutech.adminapp.model.Article;
import com.fahamutech.adminapp.model.Category;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NewArticleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText title;
    private TextInputEditText message;
    private ImageView imageView;
    private Button upload;
    private Button uploadImage;
    private ArticleDataSource dataSource;
    private Spinner spinner;

    private Image imageUrl = null;
    private List<Category> cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimony);
        bindView();
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(R.string.new_article);
        }

        dataSource = new ArticlesNoSqlDatabase(this);

        new HomeNoSqlDatabase(this).getAllCategory(
                object -> {
                    QuerySnapshot snapshots = (QuerySnapshot) object;
                    if (snapshots != null) {
                        List<String> strings = new ArrayList<>();
                        cat = snapshots.toObjects(Category.class);
                        for (Category a : cat) {
                            strings.add(a.getName());
                        }
                        ArrayAdapter<String> adapter
                                = new ArrayAdapter<>(NewArticleActivity.this,
                                android.R.layout.simple_spinner_item, strings);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image img = ImagePicker.getFirstImageOrNull(data);
            Bitmap bitmap = BitmapFactory.decodeFile(img.getPath());
            Glide.with(this).load(bitmap).into(imageView);
            imageUrl = img;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.testimony_title);
        message = findViewById(R.id.testimony_content);
        imageView = findViewById(R.id.testimony_image);
        uploadImage = findViewById(R.id.testimony_u);
        upload = findViewById(R.id.testimony_upload);
        spinner = findViewById(R.id.testimony_spinner);


        upload.setOnClickListener(v -> {
            if (title.getText().toString().isEmpty()) {
                title.requestFocus();
                Snackbar.make(v, "Fill the title", Snackbar.LENGTH_SHORT).show();
            } else if (message.getText().toString().isEmpty()) {
                message.requestFocus();
                Snackbar.make(v, "Fill the message", Snackbar.LENGTH_SHORT).show();
            } else if (imageUrl == null) {
                uploadImage.requestFocus();
                Snackbar.make(v, "Upload image please", Snackbar.LENGTH_SHORT).show();
            } else {
                String catId = "";
                String c = (String) spinner.getSelectedItem();
                for (Category category : cat) {
                    if (category.getName().equals(c)) {
                        catId = category.getId();
                        break;
                    }
                }

                uploadArticle(
                        imageUrl.getPath(),
                        imageUrl.getName(),
                        title.getText().toString(),
                        message.getText().toString(),
                        catId
                );

                Toast.makeText(this, "Creating article...", Toast.LENGTH_SHORT).show();
            }

        });

        uploadImage.setOnClickListener(v -> ImagePicker.create(this)
                .single().theme(R.style.AppTheme)
                .returnMode(ReturnMode.ALL)
                .showCamera(true)
                .limit(1)
                .start());
    }

    private void uploadArticle(String fileLocation, String fileName,
                               String title, String message, String catId) {

        //progress dialog
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Upload Category")
                .content("Please wait...")
                .canceledOnTouchOutside(false)
                .progress(true, 1)
                .positiveText("Close")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .build();
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        //save to firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference reference = storage.getReference();
        StorageReference child = storage.getReference()
                .child("articles" + "/" + year + "/" + month + "/" + fileName);
        child.putFile(Uri.fromFile(new File(fileLocation)))
                .addOnProgressListener(taskSnapshot -> {
                    //dialog.setProgress((int) (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                    Log.e("BYTE TRANS : ", String.valueOf(taskSnapshot.getBytesTransferred()));
                })
                .continueWithTask(task -> {
                    if (!task.isComplete()) {
                        try {
                            Log.e("UPLOAD ERROR ", task.getException().getMessage());
                        } catch (Throwable ignore) {

                        }
                    }
                    return child.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String downloadUri = Objects.requireNonNull(task.getResult()).toString();
                        if (downloadUri == null) downloadUri = "";

                        //fill the url to the firestore
                        dataSource.createArticle(
                                new Article(
                                        catId,
                                        String.valueOf(new Date().getTime()),
                                        message,
                                        "free",
                                        downloadUri,
                                        title),
                                data -> {
                                    //done
                                    Log.e("upload category", "Done upload category");
                                    Toast.makeText(this, "Done create category", Toast.LENGTH_SHORT).show();
                                },
                                data -> {
                                    //fails
                                    Log.e("upload category", "Fail to upload category");
                                    Toast.makeText(this, "Fail to create category", Toast.LENGTH_SHORT).show();
                                });
                        dialog.dismiss();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                });
    }


}
