package com.fahamutech.doctorapp.forum.vholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahamutech.doctorapp.R;


public class ChatReceiverViewHolder extends RecyclerView.ViewHolder {
    private CardView cardViewReceiver;
    private ImageView imageViewReceiver;
    private TextView messageTextViewReceiver;
    private TextView timeTextViewReceiver;

    public ChatReceiverViewHolder(View itemView) {
        super(itemView);
        cardViewReceiver = itemView.findViewById(R.id.chat_buble_card_receive);
        imageViewReceiver = itemView.findViewById(R.id.chat_buble_image_receive);
        messageTextViewReceiver = itemView.findViewById(R.id.chat_buble_mesage_receive);
        timeTextViewReceiver = itemView.findViewById(R.id.chat_buble_time_receive);
    }

    public CardView getCardViewReceiver() {
        return cardViewReceiver;
    }

    public ImageView getImageViewReceiver() {
        return imageViewReceiver;
    }

    public TextView getMessageTextViewReceiver() {
        return messageTextViewReceiver;
    }

    public TextView getTimeTextViewReceiver() {
        return timeTextViewReceiver;
    }
}
