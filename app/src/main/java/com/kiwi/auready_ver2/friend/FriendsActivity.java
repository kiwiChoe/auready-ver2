package com.kiwi.auready_ver2.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kiwi.auready_ver2.Injection;
import com.kiwi.auready_ver2.R;
import com.kiwi.auready_ver2.util.ActivityUtils;

public class FriendsActivity extends AppCompatActivity {

    public static final int REQ_FRIENDS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        FriendsFragment friendsFragment =
                (FriendsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (friendsFragment == null) {
            friendsFragment = FriendsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), friendsFragment, R.id.content_frame, FriendsFragment.TAG_FRIENDFRAG);
        }

        // Create Presenter
        FriendPresenter presenter = new FriendPresenter(
                Injection.provideUseCaseHandler(),
                friendsFragment,
                Injection.provideGetFriends(getApplicationContext())
        );

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_open_findview) {
            openFindView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFindView() {
        Intent intent =
                new Intent(FriendsActivity.this, FindActivity.class);
        startActivity(intent);
    }
}