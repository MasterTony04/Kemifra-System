package com.fahamu.tech.chat.forumadmin.vholder;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahamu.tech.chat.forumadmin.R;

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
