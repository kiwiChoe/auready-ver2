package com.kiwi.auready_ver2.taskheads;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kiwi.auready_ver2.R;
import com.kiwi.auready_ver2.data.TaskHead;
import com.kiwi.auready_ver2.tasks.TasksActivity;
import com.kiwi.auready_ver2.util.ListViewAdapter;
import com.kiwi.auready_ver2.util.SwipeToDismissTouchListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskHeadFragment extends Fragment implements TaskHeadContract.View {

    public static final String TAG_TASKSFRAGMENT = "TAG_TasksFragment";

    private TaskHeadContract.Presenter mPresenter;

    // interface
    private TasksFragmentListener mListener;
    private TaskHeadListAdapter mTaskHeadListAdapter;

    private LinearLayout mTaskHeadsView;
    private LinearLayout mNoTaskHeadsView;

    public TaskHeadFragment() {
        // Required empty public constructor
    }

    public static TaskHeadFragment newInstance() {

        return new TaskHeadFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskHeadListAdapter = new TaskHeadListAdapter(new ArrayList<TaskHead>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /*
        * Listener for clicks on taskHeads in the ListView
        * */
    TaskHeadListAdapter.TaskHeadItemListener
            mItemListener = new TaskHeadListAdapter.TaskHeadItemListener() {
        @Override
        public void onClick(TaskHead clickedTaskHead) {
            mPresenter.openTaskHead(clickedTaskHead);
        }

        @Override
        public void onLongClick(TaskHead clickedTaskHead) {
            mPresenter.deleteTaskHead(clickedTaskHead.getId());
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TasksFragmentListener) {
            mListener = (TasksFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TasksFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void setPresenter(TaskHeadContract.Presenter tasksPresenter) {
        mPresenter = checkNotNull(tasksPresenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_taskheads, container, false);

        // Set Floating button
        FloatingActionButton fb =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTaskHead();
            }
        });

        // Set TaskHeadsView
        ListView taskHeadListView = (ListView) root.findViewById(R.id.taskhead_list);
        taskHeadListView.setAdapter(mTaskHeadListAdapter);
        initListView(taskHeadListView);

        mTaskHeadsView = (LinearLayout) root.findViewById(R.id.taskheads_view);
        mNoTaskHeadsView = (LinearLayout) root.findViewById(R.id.no_taskhead_view);

        return root;
    }

    private void initListView(ListView listView) {
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
//                                Log.d("MY_LOG", "canDismiss : " + position);
                                return true;
                            }

                            @Override
                            public void onPendingDismiss(ListViewAdapter view, int position) {
//                                Log.d("MY_LOG", "onPendingDismiss : " + position);
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
//                                Log.d("MY_LOG", "onDismiss : " + position);
                                mTaskHeadListAdapter.removeData(position);
                            }
                        });

        mTaskHeadListAdapter.setSwipeTouchListener(touchListener);
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mItemListener.onClick((TaskHead) mTaskHeadListAdapter.getItem(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mItemListener.onLongClick((TaskHead) mTaskHeadListAdapter.getItem(position));
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode, data);
    }

    @Override
    public void setLoginSuccessUI() {

        if (mListener != null) {
            mListener.onLoginSuccess();
        }
    }

    @Override
    public void openTasks(@NonNull TaskHead taskHead) {
        checkNotNull(taskHead);
        Intent intent = new Intent(getContext(), TasksActivity.class);
        intent.putExtra(TasksActivity.EXTRA_TASKHEAD_ID, taskHead.getId());
        intent.putExtra(TasksActivity.EXTRA_TASKHEAD_TITLE, taskHead.getTitle());
        startActivityForResult(intent, TasksActivity.REQ_ADD_TASK);
    }

    @Override
    public void showTaskHeadDeleted() {
        // Reload taskHeads after deleted
        mPresenter.loadTaskHeads();
    }

    @Override
    public void showTaskHeads(List<TaskHead> taskHeads) {
        mTaskHeadListAdapter.replaceData(taskHeads);

        mTaskHeadsView.setVisibility(View.VISIBLE);
        mNoTaskHeadsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoTaskHeads() {
    }

    @Override
    public void showEmptyTaskHeadError() {

        Snackbar.make(getView(), getString(R.string.taskhead_empty_err), Snackbar.LENGTH_LONG).show();
    }

    // Interface with TaskHeadActivity
    public interface TasksFragmentListener {
        void onLoginSuccess();
    }
}
