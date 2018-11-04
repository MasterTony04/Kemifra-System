package com.fahamu.tech.chat.forumadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fahamu.tech.chat.forumadmin.GetTimeAgo;
import com.fahamu.tech.chat.forumadmin.MyChatActivity;
import com.fahamu.tech.chat.forumadmin.R;
import com.fahamu.tech.chat.forumadmin.database.PostDataSource;
import com.fahamu.tech.chat.forumadmin.database.PostNoSqlDataBase;
import com.fahamu.tech.chat.forumadmin.model.ChatTopic;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chami o n 7/9/18.
 */

public class MyChatFragmentAdapter extends RecyclerView.Adapter<MyChatFragmentAdapter.ViewHolder> {

    private List<ChatTopic> listItem;
    private Context context;
    private PostDataSource noSqlDatabase;

    public MyChatFragmentAdapter(List<ChatTopic> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        this.noSqlDatabase = new PostNoSqlDataBase(context);
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

        if (chatTopicModal.isUserSeen()) {
            holder.view.setVisibility(View.GONE);
        } else holder.view.setVisibility(View.VISIBLE);

        holder.postTitle.setText(chatTopicModal.getTitle());
        holder.postDescription.setText(chatTopicModal.getDescription());
        long time = Long.parseLong(chatTopicModal.getTime());

        //holder.docId.setText(chatTopicModal.getDocId());
        holder.time.setText(GetTimeAgo.getTimeAgo(time, context));

        Glide.with(context).load(chatTopicModal.getUserPhoto())
                .into(holder.circleImageView);

        holder.mainView.setOnClickListener(v1 -> {
            Intent intent = new Intent(context, MyChatActivity.class);
            intent.putExtra("_chat_topic_", chatTopicModal);
            intent.putExtra("_doctor_", false);
            context.startActivity(intent);
        });

        holder.mainView.setOnLongClickListener(v -> {
            //show delete doalog
            deleteDialog(holder.mainView, chatTopicModal.getDocId());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect
                        .createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(100);
            }
        }
    }

    private void deleteDialog(View itemView, String docId) {
        vibrate();
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
                    noSqlDatabase.deletePost(docId);
                    //Log.e("TAG******","Umefua");
                    dialog.dismiss();
                    Snackbar.make(itemView, "Chat deleted...", Toast.LENGTH_SHORT).show();
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                    Snackbar.make(itemView, "Canceled...", Toast.LENGTH_SHORT).show();
                }).show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView postTitle;
        TextView postDescription;
        TextView time;
        CircleImageView circleImageView;
        View mainView;

        ViewHolder(final View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.post_title);
            postDescription = itemView.findViewById(R.id.post_description);
            this.time = itemView.findViewById(R.id.forum_time);
            this.circleImageView = itemView.findViewById(R.id.profile_image);
            this.view = itemView.findViewById(R.id.forum_topic_new);
            this.mainView = itemView;
        }
    }

}
