package org.chat21.android.ui.archived_conversations.activities;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.chat21.android.R;
import org.chat21.android.ui.archived_conversations.fragments.ArchivedConversationListFragment;

/**
 * Created by stefano on 02/08/2018.
 */
public class ArchivedConversationListActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_archived_conversation_list);

        // #### BEGIN TOOLBAR ####
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar!=null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // #### END  TOOLBAR ####

        // #### BEGIN CONTAINER ####
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ArchivedConversationListFragment())
                .commit();
        // #### BEGIN CONTAINER ####


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //startActivity(new Intent(this, YourActivity.class));
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}