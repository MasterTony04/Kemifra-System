package com.fahamutech.adminapp.vholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahamutech.adminapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView description;
    private CircleImageView image;
    private View view;

    public ArtViewHolder(View itemView) {
        super(itemView);
        this.view=itemView;
        title = itemView.findViewById(R.id.article_title);
        description = itemView.findViewById(R.id.article_description);
        image = itemView.findViewById(R.id.article_image);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public ImageView getImage() {
        return image;
    }

    public View getView() {
        return view;
    }
}
