package com.fahamutech.adminapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.activities.SimpleImageViewer;
import com.fahamutech.adminapp.database.noSql.CategoryNoSqlDatabase;
import com.fahamutech.adminapp.database.noSql.TestmonyNoSqlDatabase;
import com.fahamutech.adminapp.model.Testimony;
import com.fahamutech.adminapp.vholder.TestViewHolder;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.util.List;

public class TestimonyAdapter extends RecyclerView.Adapter<TestViewHolder> {

    private List<Testimony> testimonies;
    private Context context;

    public TestimonyAdapter(Context context, List<Testimony> testimonies) {
        this.context = context;
        this.testimonies = testimonies;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater
                .from(context).inflate(R.layout.testimony_item, parent, false);
        return new TestViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Glide.with(context).load(testimonies.get(position).getImage()).into(holder.getImageView());
        holder.getImageView().setOnClickListener(v -> {
            Intent intent = new Intent(context, SimpleImageViewer.class);
            intent.putExtra("_image_", testimonies.get(position));
            context.startActivity(intent);
        });

        holder.getImageView().setOnLongClickListener(v -> {
            deleteDialog(v, testimonies.get(position).getId());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return testimonies.size();
    }

    private void deleteDialog(View itemView, String docId) {
        //vibrate();
        new MaterialStyledDialog.Builder(context)
                .setDescription("Confirm Deletion")
                .setStyle(Style.HEADER_WITH_ICON)
                .setCancelable(false)
                .autoDismiss(true)
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveText("Delete")
                .setNegativeText("Cancel")
                .onPositive((dialog, which) -> {
                    //TODO: delete from firestore
                    new TestmonyNoSqlDatabase(itemView.getContext()).deleteTestimony(
                            docId,
                            data -> {
                                Log.e("category delete", "Done delete category");
                            },
                            data -> {
                                Log.e("category delete", "category fail to be deleted");
                            });

                    //Log.e("TAG******","Umefua");
                    dialog.dismiss();
                    Snackbar.make(itemView, "Category deleted...", Toast.LENGTH_SHORT).show();
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                    Snackbar.make(itemView, "Canceled...", Toast.LENGTH_SHORT).show();
                }).show();
    }
}
