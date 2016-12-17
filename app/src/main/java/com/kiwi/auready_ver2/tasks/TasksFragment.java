package com.kiwi.auready_ver2.tasks;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.kiwi.auready_ver2.R;
import com.kiwi.auready_ver2.data.Friend;
import com.kiwi.auready_ver2.data.Task;
import com.kiwi.auready_ver2.data.TaskHead;
import com.kiwi.auready_ver2.taskheads.TaskHeadsActivity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksFragment extends Fragment implements TasksContract.View {

    public static final String TAG_TASKSFRAGMENT = "Tag_TasksFragment";

    private TextView mTitleView;
    private ExpandableListView mTasksView;
    private TasksAdapter mTasksAdapter;

    private String mTaskHeadId;
    private String mTaskHeadTitle;

    private TasksContract.Presenter mPresenter;

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTaskHeadId = getArguments().getString(TaskHeadsActivity.EXTRA_TASKHEAD_ID);
        }


//        groupList.add("member 1");
//        groupList.add("member 2");
//        groupList.add("member 3");
//
//        childContents.add("taskTextView 1");
//        childContents.add("taskTextView 2");
//        childContents.add("taskTextView 3");
//
//        childList.add(childContents);
//        childList.add(childContents);
//        childList.add(childContents);

        mTasksAdapter = new TasksAdapter(getContext(), new ArrayList<Friend>(), new ArrayList<ArrayList<Task>>(), mTaskItemListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull TasksContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set ListView
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        mTasksView = (ExpandableListView) root.findViewById(R.id.expand_listview);
        mTasksView.setAdapter(mTasksAdapter);

        return root;
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setTitle(String title) {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setMembers(List<Friend> members) {
        mTasksAdapter.replaceMemberList(members);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTasksAdapter.replaceTasksList(tasks);
    }

    @Override
    public void showNoTasks() {

    }

    TaskItemListener mTaskItemListener = new TaskItemListener() {
        @Override
        public void onTaskItemClick(String taskHeadId) {

        }

        @Override
        public void onDeleteClick(TaskHead clickedTaskHead) {

        }

        @Override
        public void onAddTaskClick() {

        }
    };

    public interface TaskItemListener {
        void onTaskItemClick(String taskHeadId);

        void onDeleteClick(TaskHead clickedTaskHead);

        void onAddTaskClick();
    }
}
