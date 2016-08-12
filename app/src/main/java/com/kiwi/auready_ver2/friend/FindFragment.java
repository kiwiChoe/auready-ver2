package com.kiwi.auready_ver2.friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kiwi.auready_ver2.R;
import com.kiwi.auready_ver2.data.Friend;

import static com.google.common.base.Preconditions.checkNotNull;

/*
* Find users to add friend into my friendList
* */
public class FindFragment extends Fragment implements
        FindContract.View {

    public static final String TAG_FINDFRAG = "Tag_FindFragment";
    private FindContract.Presenter mPresenter;

    public FindFragment() {
        // Required empty public constructor
    }

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_find, container, false);

        Button btSaveFriend = (Button)root.findViewById(R.id.bt_test_save_friend);
        btSaveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stub a Friend
                Friend friend = new Friend("aa@aa.com", "nameOfaa");
                mPresenter.saveFriend(friend);
            }
        });
        return root;
    }

    @Override
    public void setPresenter(@NonNull FindContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter, "presenter cannot be null");
    }

    @Override
    public void showSuccessMsgOfAddFriend(Friend newFriend) {
        String successMsg = newFriend.getName() + " " + getString(R.string.add_friend_success_msg);
        Snackbar.make(getView(), successMsg, Snackbar.LENGTH_SHORT);
    }
}
