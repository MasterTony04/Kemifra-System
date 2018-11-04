package com.fahamutech.adminapp.vholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahamutech.adminapp.R;

public class CatViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView title;
    private TextView subtitle;
    private View view;

    public CatViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        imageView = itemView.findViewById(R.id.cat_image);
        title = itemView.findViewById(R.id.cat_title);
        subtitle = itemView.findViewById(R.id.cat_subtitle);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(TextView subtitle) {
        this.subtitle = subtitle;
    }

    public View getView() {
        return view;
    }
}
