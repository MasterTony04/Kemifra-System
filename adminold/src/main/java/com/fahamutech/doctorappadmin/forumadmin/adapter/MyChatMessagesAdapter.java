package com.fahamu.tech.chat.forumadmin.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fahamu.tech.chat.forumadmin.ForumImageViwer;
import com.fahamu.tech.chat.forumadmin.GetTimeAgo;
import com.fahamu.tech.chat.forumadmin.R;
import com.fahamu.tech.chat.forumadmin.model.ChatEnum;
import com.fahamu.tech.chat.forumadmin.model.ChatMessages;
import com.fahamu.tech.chat.forumadmin.vholder.MyChatViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MyChatMessagesAdapter extends RecyclerView.Adapter<MyChatViewHolder> {

    private List<ChatMessages> chatMessagesList;
    private Context context;
    private String userId;

    public MyChatMessagesAdapter(List<ChatMessages> messages, Context context) {
        this.chatMessagesList = messages;
        this.context = context;
        this.userId = getUserId();
    }

    @NonNull
    @Override
    public MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate1 = LayoutInflater.from(context)
                .inflate(R.layout.forum_message_send_buble, parent, false);
        return new MyChatViewHolder(inflate1);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatViewHolder holder, int position) {

        ChatMessages chatMessages = chatMessagesList.get(position);
        if (chatMessages.getSenderId().equals(userId)) {
            showSender(holder, chatMessages);
        } else {
            showReceiver(holder, chatMessages);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessagesList.size();
    }

    public void addMessage(ChatMessages chatMessages) {
        chatMessagesList.add(chatMessages);
        notifyDataSetChanged();
    }

    private String getUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return "";
        }
    }

    private void showImage(String image) {
        Intent intent = new Intent(context, ForumImageViwer.class);
        intent.putExtra("_image_", image);
        context.startActivity(intent);
    }

    private void showSender(MyChatViewHolder holder, ChatMessages chatMessages) {
        holder.getCardViewSender().setVisibility(View.VISIBLE);
        holder.getCardViewReceiver().setVisibility(View.GONE);


        if (chatMessages.getType().equals(ChatEnum.text.name())) {
            holder.getMessageTextViewSender().setVisibility(View.VISIBLE);
            holder.getImageViewSender().setVisibility(View.GONE);

            //add text
            holder.getMessageTextViewSender().setText(chatMessages.getMessage());
            //add timestamp
            holder.getTimeTextViewSender()
                    .setText(GetTimeAgo.getTimeAgo(Long.parseLong(chatMessages.getTime()), context));

        } else if (chatMessages.getType().equals(ChatEnum.image.name())) {
            ImageView imageViewSender = holder.getImageViewSender();
            imageViewSender.setVisibility(View.VISIBLE);
            holder.getMessageTextViewSender().setVisibility(View.GONE);

            //imageViewSender.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ch))
                    .load(chatMessages.getMessage())
                    .into(imageViewSender);


            holder.getTimeTextViewSender()
                    .setText(GetTimeAgo.getTimeAgo(Long.parseLong(chatMessages.getTime()), context));
            holder.getCardViewSender().setOnClickListener(v -> {
                showImage(chatMessages.getMessage());
            });
        }
    }

    private void showReceiver(MyChatViewHolder holder, ChatMessages chatMessages) {
        holder.getCardViewReceiver().setVisibility(View.VISIBLE);
        holder.getCardViewSender().setVisibility(View.GONE);

        if (chatMessages.getType().equals(ChatEnum.text.name())) {
            holder.getMessageTextViewReceiver().setVisibility(View.VISIBLE);
            holder.getImageViewReceiver().setVisibility(View.GONE);

            //messageTextViewReceiver.setVisibility(View.VISIBLE);
            holder.getMessageTextViewReceiver().setText(chatMessages.getMessage());
            holder.getTimeTextViewReceiver()
                    .setText(GetTimeAgo.getTimeAgo(Long.parseLong(chatMessages.getTime()), context));
        } else if (chatMessages.getType().equals(ChatEnum.image.name())) {
            ImageView imageViewReceiver = holder.getImageViewReceiver();
            imageViewReceiver.setVisibility(View.VISIBLE);
            holder.getMessageTextViewReceiver().setVisibility(View.GONE);

            //imageViewReceiver.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ch))
                    .load(chatMessages.getMessage())
                    .into(imageViewReceiver);


            holder.getTimeTextViewReceiver()
                    .setText(GetTimeAgo.getTimeAgo(Long.parseLong(chatMessages.getTime()), context));
            holder.getCardViewReceiver().setOnClickListener(v -> {
                showImage(chatMessages.getMessage());
            });
        }
    }
}
