package com.fahamutech.doctorapp.forum.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fahamutech.doctorapp.R;
import com.fahamutech.doctorapp.forum.model.Receipt;
import com.fahamutech.doctorapp.forum.vholder.ReceiptsViewHolder;

import java.util.Date;
import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsViewHolder> {

    private Context context;
    private List<Receipt> receipts;

    public ReceiptsAdapter(Context context, List<Receipt> receipt) {
        this.context = context;
        this.receipts = receipt;
    }

    @NonNull
    @Override
    public ReceiptsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.forum_receipt, parent, false);
        return new ReceiptsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsViewHolder holder, int position) {
        long l = Long.parseLong(receipts.get(0).getStart());
        holder.getDate().setText(new Date(l).toString());
        holder.getAmount().setText(receipts.get(0).getAmount());
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }
}
