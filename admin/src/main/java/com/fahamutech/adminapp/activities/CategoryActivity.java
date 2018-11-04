package com.fahamutech.adminapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.database.connector.CategoryDataSource;
import com.fahamutech.adminapp.database.noSql.CategoryNoSqlDatabase;
import com.fahamutech.adminapp.model.Category;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextInputEditText title;
    private TextInputEditText description;
    private ImageView imageView;
    private Button uploadButton;
    private CategoryDataSource categoryDataSource;

    private Image imageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        bindView();

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("New Category");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        categoryDataSource = new CategoryNoSqlDatabase(this);
        fab.setOnClickListener(view -> createCategory());
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            //List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            //image.getPath();
            //Log.e("TAG IMAGE", image.getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
            Glide.with(this).load(bitmap).into(imageView);
            imageUrl = image;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadCategory(String fileLocation, String fileName, String name, String description) {

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
                .child("category" + "/" + year + "/" + month + "/" + fileName);
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
                        categoryDataSource.createCategory(new Category(name, description, downloadUri),
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

                    } else {
                        // Handle failures
                        //fill the url to the firestore
                        categoryDataSource.createCategory(new Category(name, description, ""),
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
                        Log.e("Image failed upload : ",
                                Objects.requireNonNull(task.getException()).getMessage());
                        dialog.dismiss();
                    }

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.cat_new_name);
        description = findViewById(R.id.cat_new_description);
        fab = findViewById(R.id.fab);
        imageView = findViewById(R.id.cat_new_image);
        uploadButton = findViewById(R.id.cat_new_upload);

        uploadButton.setOnClickListener(v -> {
            ImagePicker.create(this)
                    .single()
                    .theme(R.style.AppTheme)
                    .showCamera(true)
                    .returnMode(ReturnMode.ALL)
                    .start();
        });
    }

    private void createCategory() {
        if (title.getText().toString().isEmpty()) {
            title.requestFocus();
            snackMessage("Fill the title of the category");
        } else if (description.getText().toString().isEmpty()) {
            description.requestFocus();
            snackMessage("Fill the description of the category");
        } else if (imageUrl == null) {
            uploadButton.requestFocus();
            snackMessage("Upload image for the category");
        } else {
            uploadCategory(imageUrl.getPath(), imageUrl.getName(), title.getText().toString(),
                    description.getText().toString());
            snackMessage("Creating category...");
        }
    }

    private void snackMessage(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();
    }

}
