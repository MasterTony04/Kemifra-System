package com.fahamutech.adminapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.database.noSql.ArticlesNoSqlDatabase;
import com.fahamutech.adminapp.database.noSql.CategoryNoSqlDatabase;
import com.fahamutech.adminapp.model.Article;
import com.fahamutech.adminapp.vholder.ArtViewHolder;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.util.List;

public class DelArtAdapter extends RecyclerView.Adapter<ArtViewHolder> {

    private List<Article> articles;
    private Context context;

    public DelArtAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.article_view, parent, false);
        return new ArtViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtViewHolder holder, int position) {
        holder.getTitle().setText(articles.get(position).getTitle());
        holder.getDescription().setText(articles.get(position).getContent());
        Glide.with(context)
                .load(articles.get(position).getImage())
                //.apply(new RequestOptions().circleCrop())
                .into(holder.getImage());
        holder.getView().setOnLongClickListener(v -> {
            deleteDialog(v, articles.get(position).getId());
            return true;
        });
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
                    new ArticlesNoSqlDatabase(itemView.getContext()).deleteArticle(
                            docId,
                            data -> {
                                Log.e("category delete", "Done delete category");
                            },
                            data -> {
                                Log.e("category delete", "category fail to be deleted");
                            });

                    dialog.dismiss();
                    Snackbar.make(itemView, "Category deleted...", Toast.LENGTH_SHORT).show();
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                    Snackbar.make(itemView, "Canceled...", Toast.LENGTH_SHORT).show();
                }).show();
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
