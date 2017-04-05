package com.kiwi.auready_ver2.friend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.kiwi.auready_ver2.R;
import com.kiwi.auready_ver2.data.Friend;
import com.kiwi.auready_ver2.data.SearchedUser;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/*
* Find users to add friend into my friendList
* */
public class FindFragment extends Fragment implements
        FindContract.View {

    public static final String TAG_FINDFRAG = "Tag_FindFragment";
    private FindContract.Presenter mPresenter;

    // testing
    private ArrayList<Friend> TEST_FRIENDS;
    private int TEST_FRIENDS_CNT;

    private View mRoot;
    private LinearLayout mSearchedUsersView;
    private EditText mSearchPeopleEd;
    private ImageButton mSearchPeopleBt;
    private ListView mSearchedList;
    private SearchedUsersAdapter mListAdapter;

    private LinearLayout mNoUsersView;

    public FindFragment() {
        // Required empty public constructor
    }

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // testing
        TEST_FRIENDS = Lists.newArrayList(
                new Friend("email1", "name1"),
                new Friend("email2", "name2"),
                new Friend("email3", "name3"),
                new Friend("email4", "name4"),
                new Friend("email5", "name5"),
                new Friend("email6", "name6"),
                new Friend("email7", "name7"),
                new Friend("email8", "name8"),
                new Friend("email9", "name9"),
                new Friend("email10", "name10"));
        TEST_FRIENDS_CNT = -1;

        mListAdapter = new SearchedUsersAdapter(new ArrayList<SearchedUser>(0), mItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_find, container, false);

        Button btSaveFriend = (Button)mRoot.findViewById(R.id.bt_test_save_friend);
        btSaveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // testing
                // Stub a Friend
                TEST_FRIENDS_CNT = (TEST_FRIENDS_CNT+1) % 10;
                mPresenter.saveFriend(TEST_FRIENDS.get(TEST_FRIENDS_CNT));
            }
        });

        // Searched users view
        mSearchedUsersView = (LinearLayout) mRoot.findViewById(R.id.searched_list_layout);
        mSearchPeopleEd = (EditText) mRoot.findViewById(R.id.ed_search_people);
        mSearchPeopleBt = (ImageButton) mRoot.findViewById(R.id.bt_search_people);

        mSearchedList = (ListView) mRoot.findViewById(R.id.searched_list);
        mSearchedList.setAdapter(mListAdapter);

        // No users view
        mNoUsersView = (LinearLayout) mRoot.findViewById(R.id.no_searched_email_layout);
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSearchPeopleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.findPeople(mSearchPeopleEd.getText().toString());
            }
        });
    }

    @Override
    public void setPresenter(@NonNull FindContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter, "presenter cannot be null");
    }

    @Override
    public void setAddFriendSucceedUI(@NonNull String name) {
        // Show message
        String successMsg = name + " " + getString(R.string.add_friend_success_msg);
        Log.d(TAG_FINDFRAG, successMsg);
        Snackbar.make(mRoot, successMsg, Snackbar.LENGTH_SHORT).show();

        // replace adapter data
        mListAdapter.updateStatusOf(name);
    }

    @Override
    public void showSearchedPeople(List<SearchedUser> searchedPeople) {
        mListAdapter.replaceData(searchedPeople);

        mNoUsersView.setVisibility(View.GONE);
        mSearchedUsersView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoSearchedPeople() {
        mSearchedUsersView.setVisibility(View.GONE);
        mNoUsersView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAddFriendFailMessage(int stringResource) {
        Log.d(TAG_FINDFRAG, getString(stringResource));
        Snackbar.make(mRoot, getString(stringResource), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailOrNameTextError() {
        mSearchPeopleEd.requestFocus();
        mSearchPeopleEd.setError(getString(R.string.find_email_empty_error));
    }

    public interface SearchedUserItemListener {
        void onClickUserPendingStatus(String userName);
        void onClickUser(SearchedUser searchedUser);
    }

    /*
    * Listener for clicks on searched users in the Listview
    * */
    SearchedUserItemListener mItemListener = new SearchedUserItemListener() {
        @Override
        public void onClickUserPendingStatus(String userName) {
            Toast.makeText(getActivity(), userName + " 님의 수락을 기다리는 중입니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickUser(SearchedUser searchedUser) {
            mPresenter.addFriend(searchedUser);
        }
    };
}
