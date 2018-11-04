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
import com.fahamutech.adminapp.activities.CategoryContent;
import com.fahamutech.adminapp.database.noSql.CategoryNoSqlDatabase;
import com.fahamutech.adminapp.model.Category;
import com.fahamutech.adminapp.session.Session;
import com.fahamutech.adminapp.vholder.CatViewHolder;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatViewHolder> {

    private List<Category> lists;
    private Context context;
    private Session session;

    public CatAdapter(List<Category> lists, Context context) {
        this.lists = lists;
        this.context = context;
        this.session = new Session(context);
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context)
                .inflate(R.layout.cat_view, parent, false);
        return new CatViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        holder.getTitle().setText(lists.get(position).getName());
        holder.getSubtitle().setText(lists.get(position).getDescription());
        Glide.with(context).load(lists.get(position).getImage()).into(holder.getImageView());

        holder.getView().setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryContent.class);
            intent.putExtra("_category", lists.get(position));
            session.saveLastCategory(lists.get(position).getId());
            session.saveLastTitle(lists.get(position).getName());
            context.startActivity(intent);
        });

        holder.getView().setOnLongClickListener(v -> {
            deleteDialog(v, lists.get(position).getId());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
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
                    new CategoryNoSqlDatabase(itemView.getContext()).deleteCategory(
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
