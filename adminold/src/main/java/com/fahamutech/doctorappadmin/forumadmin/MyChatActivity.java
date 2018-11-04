package com.fahamu.tech.chat.forumadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.fahamu.tech.chat.forumadmin.adapter.MyChatMessagesAdapter;
import com.fahamu.tech.chat.forumadmin.database.ChatDataSource;
import com.fahamu.tech.chat.forumadmin.database.ChatNoSqlDataBase;
import com.fahamu.tech.chat.forumadmin.database.DataBaseCallback;
import com.fahamu.tech.chat.forumadmin.model.ChatEnum;
import com.fahamu.tech.chat.forumadmin.model.ChatMessages;
import com.fahamu.tech.chat.forumadmin.model.ChatTopic;
import com.fahamu.tech.chat.forumadmin.model.OnlineStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fahamu.tech.chat.forumadmin.adapter.AllChatFragmentAdapter.TOPIC;

public class MyChatActivity extends AppCompatActivity {

    private List<ChatMessages> chatMessagesArrayList = new ArrayList<>();


    private Toolbar toolbar;
    private FloatingActionButton addImageFab;
    private FloatingActionButton sendMessageFab;
    private EditText messageInput;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatTopic chatTopic;
    private ChatDataSource chatDataSource;
    private ActionBar actionBar;
    private ListenerRegistration receiveListener;
    private ListenerRegistration onlineListener;
    private MyChatMessagesAdapter myChatMessagesAdapter;
    private boolean seenFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity_my_chat);
        bindView();

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("");
            actionBar.setTitle("");
        }

        //get the chat topic object
        chatTopic = (ChatTopic) getIntent().getSerializableExtra("_chat_topic_");
        if (chatTopic != null) {
            actionBar.setTitle(chatTopic.getTitle());
            //fab
            fabActions();
            //testing
            //fakeData();
            //addTypeListener();

            //data
            initFirebase();
            //ad type listener
            //addTypeListener();
            seenFlag = getIntent().getBooleanExtra("_doctor_", false);
            //updateSeenFlags(seenFlag);
        }
    }

    private void updateSeenFlags(boolean seenFlag) {
        if (seenFlag) {
            chatDataSource.updateDoctorSeen(
                    chatTopic.getDocId(),
                    true,
                    data -> {
                        //if ok
                    },
                    data -> {
                        //if error
                    }
            );
        } else {
            chatDataSource.updateUserSeen(
                    chatTopic.getDocId(),
                    true,
                    data -> {
                        //if ok
                    },
                    data -> {
                        //if error
                    }
            );
        }
    }

    private void initFirebase() {

        chatDataSource = new ChatNoSqlDataBase(this);
        //set online listener
        onlineListener = (ListenerRegistration)
                chatDataSource.onlineStatus(chatTopic.getUserId(), (DataBaseCallback) data -> {
                    OnlineStatus onlineStatus = (OnlineStatus) data;
                    actionBar.setSubtitle(onlineStatus.getOnline());
                });

        //receive message
        receiveListener = (ListenerRegistration)
                chatDataSource.receiveMessage(
                        chatTopic.getDocId(),
                        data -> {
                            //for changed document
                            DocumentChange documentChanges = (DocumentChange) data;
                            if (documentChanges != null) {
                                ChatMessages chatMessages =
                                        documentChanges.getDocument().toObject(ChatMessages.class);
                                if (!chatMessages.getSenderId().equals(userId())) {
                                    updateChatUI(chatMessages);
                                }
                            }
                        }
                );

        //get all message
        getAllMessage();
        swipeRefreshLayout.setOnRefreshListener(this::getAllMessage);

        //update seen flag
    }

    private void getAllMessage() {
        disableFab();
        //how loading
        swipeRefreshLayout.setRefreshing(true);
        //get all messages
        chatDataSource.getAllMessage(chatTopic.getDocId(),
                data -> {
                    swipeRefreshLayout.setRefreshing(false);
                    enableFab();
                    //success
                    chatMessagesArrayList = (List<ChatMessages>) data;
                    //initUi
                    initUI();
                },
                data -> {
                    swipeRefreshLayout.setRefreshing(false);
                    enableFab();
                    //failure
                    Exception e = (Exception) data;
                    Log.e("Chat activity", e.getLocalizedMessage());
                });
    }

    private void disableFab() {
        sendMessageFab.setVisibility(View.INVISIBLE);
        addImageFab.setVisibility(View.INVISIBLE);
    }

    private void enableFab() {
        sendMessageFab.setVisibility(View.VISIBLE);
        addImageFab.setVisibility(View.VISIBLE);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        addImageFab = findViewById(R.id.chat_add_image);
        sendMessageFab = findViewById(R.id.chat_send_message);
        messageInput = findViewById(R.id.chat_input_message);
        recyclerView = findViewById(R.id.chat_recycleview);
        swipeRefreshLayout = findViewById(R.id.chat_swipe);
    }

    private void fabActions() {

        sendMessageFab.setOnClickListener(view -> {
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //.setAction("Action", null).show();
            saveMessageToFirebase(messageInput.getText().toString(), ChatEnum.text.name());
        });

        addImageFab.setOnClickListener(view -> {
            //send image
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //.setAction("Action", null).show();
            ImagePicker.create(this)
                    .single()
                    .returnMode(ReturnMode.ALL)
                    .includeVideo(false)
                    .theme(R.style.ForumAppTheme)
                    .start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            //List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            //send image chat
            //Log.e("TAG*****", image.getPath());
            uploadImage(image.getPath(), image.getName());

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadImage(String fileLocation, String fileName) {

        //progress dialog
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Upload Image")
                .content("Please wait...")
                .canceledOnTouchOutside(false)
                .progress(true, 1)
                .positiveText("Close")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                }).build();
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        //save to firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference reference = storage.getReference();
        StorageReference child = storage.getReference()
                .child(chatTopic.getUserId() + "/" + year + "/" + month + "/" + fileName);
        child.putFile(Uri.fromFile(new File(fileLocation)))
                .addOnProgressListener(taskSnapshot -> {
                    //dialog.setProgress((int) (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                    Log.e("BYTE TRANS : ", String.valueOf(taskSnapshot.getBytesTransferred()));
                })
                .continueWithTask(task -> {
                    if (!task.isComplete()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return child.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String downloadUri = Objects.requireNonNull(task.getResult()).toString();
                        if (downloadUri == null) downloadUri = "";
                        dialog.dismiss();

                        //fill the url to the firestore
                        saveMessageToFirebase(downloadUri, ChatEnum.image.name());

                    } else {
                        // Handle failures
                        Log.e("Image failed upload : ",
                                Objects.requireNonNull(task.getException()).getMessage());
                        dialog.dismiss();
                    }
                });

    }

    private void saveMessageToFirebase(String message, String type) {
        //String message = messageInput.getText().toString();
        if (message.isEmpty()) {
            messageInput.requestFocus();
            Snackbar.make(messageInput,
                    "Message or Image can not be empty", Snackbar.LENGTH_SHORT).show();
        } else {
            String time = String.valueOf(new Date().getTime());
            ChatMessages chatMessages = new ChatMessages(userId(), message, time, type);
            //update ui
            updateChatUI(chatMessages);

            //send message to database
            chatDataSource.sendMessage(
                    chatTopic.getDocId(),
                    chatMessages,
                    data -> {
                        //called when data is successful updated
                        String s = (String) data;
                        Log.e("write to chart*** : ", s);
                    },
                    data -> {
                        //called when data is not written
                        String s = (String) data;
                        Log.e("write to chart*** : ", s);
                    }
            );

            //update document timestamp
            chatDataSource.updateThreadTimestamp(
                    chatTopic.getDocId(),
                    time,
                    data -> {
                        //when successful
                        Log.e("Done update doc : ", (String) data);
                    },
                    data -> {
                        //when failed
                        Log.e("Error update doc : ", (String) data);
                    }
            );

            //update seen flag
            updateSeenFlags(seenFlag);
        }
    }


    private void hideKeyboard() {
        //            messageInput.clearFocus();
//            View view = this.getCurrentFocus();
//            if (view != null) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }
    }

    private String userId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
            return "";
        }
    }

    private void updateChatUI(ChatMessages chatMessages) {
        try {
            myChatMessagesAdapter.addMessage(chatMessages);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            //clear input field
            messageInput.getText().clear();
        } catch (NullPointerException ignore) {
        }
    }

    private void initUI() {
        myChatMessagesAdapter = new MyChatMessagesAdapter(chatMessagesArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myChatMessagesAdapter);
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

    private void addTypeListener() {
//        messageInput.setOnKeyListener((v, keyCode, event) -> {
//            Log.e("Key Clicked : ", String.valueOf(keyCode));
//            return true;
//        });
    }

    @Override
    protected void onDestroy() {
        receiveListener.remove();
        onlineListener.remove();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.forum_chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.chat_menu_topic) {
            Snackbar.make(recyclerView, "Topic will be displayed here", Snackbar.LENGTH_SHORT)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
