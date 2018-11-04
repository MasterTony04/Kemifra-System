package com.fahamutech.doctorappadmin.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fahamutech.doctorappadmin.R;
import com.fahamutech.doctorappadmin.activities.SimpleImageViewer;
import com.fahamutech.doctorappadmin.model.Testimony;
import com.fahamutech.doctorappadmin.vholder.TestViewHolder;

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
    }

    @Override
    public int getItemCount() {
        return testimonies.size();
    }
}
