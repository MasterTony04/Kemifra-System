package com.fahamu.tech.chat.forumadmin.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fahamu.tech.chat.forumadmin.GetTimeAgo;
import com.fahamu.tech.chat.forumadmin.MyChatActivity;
import com.fahamu.tech.chat.forumadmin.R;
import com.fahamu.tech.chat.forumadmin.model.ChatTopic;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chami o n  7/10/18.
 */

public class AllChatFragmentAdapter extends RecyclerView.Adapter<AllChatFragmentAdapter.ViewHolder> {

    public static String TOPIC = "_chat_topic_";

    private List<ChatTopic> listItem;
    private Context context;

    public AllChatFragmentAdapter(List<ChatTopic> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_post_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatTopic chatTopicModal = listItem.get(position);

        if (chatTopicModal.isDoctorSeen()) {
            holder.view.setVisibility(View.GONE);
        } else holder.view.setVisibility(View.VISIBLE);

        holder.postTitle.setText(chatTopicModal.getTitle());
        holder.postDescription.setText(chatTopicModal.getDescription());

        //holder.docId.setText(chatTopicModal.getDocId());
        holder.time.setText(GetTimeAgo.getTimeAgo(Long.parseLong(chatTopicModal.getTime()), context));
        Glide.with(context)
                .load(chatTopicModal.getUserPhoto())
                .into(holder.circleImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MyChatActivity.class);
            intent.putExtra(TOPIC, chatTopicModal);
            intent.putExtra("_doctor_", true);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    /**
     * view holder class of the adapter
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView postTitle;
        TextView postDescription;
        TextView time;
        View view;
        //TextView docId;
        CircleImageView circleImageView;
        View itemView;


        ViewHolder(View itemView) {
            super(itemView);
            this.postTitle = itemView.findViewById(R.id.post_title);
            this.postDescription = itemView.findViewById(R.id.post_description);
            this.time = itemView.findViewById(R.id.forum_time);
            this.view = itemView.findViewById(R.id.forum_topic_new);
            //this.docId = itemView.findViewById(R.id.post_id);
            this.circleImageView = itemView.findViewById(R.id.profile_image);
            this.itemView = itemView;
        }
    }
}
