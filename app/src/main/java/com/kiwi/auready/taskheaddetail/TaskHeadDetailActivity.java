package com.kiwi.auready.taskheaddetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kiwi.auready.Injection;
import com.kiwi.auready.R;
import com.kiwi.auready.util.ActivityUtils;

public class TaskHeadDetailActivity extends AppCompatActivity {


    public static final String ARG_TASKHEAD_ID = "arg_taskhead_id";
    public static final String ARG_CNT_OF_TASKHEADS = "arg_countOfTaskHeads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_head_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TaskHeadDetailFragment taskHeadDetailFragment =
                (TaskHeadDetailFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        String taskHeadId = null;
        if (taskHeadDetailFragment == null) {
            taskHeadDetailFragment = TaskHeadDetailFragment.newInstance();

            Bundle bundle = new Bundle();
            // Edit
            if (getIntent().hasExtra(ARG_TASKHEAD_ID)) {
                taskHeadId = getIntent().getStringExtra(ARG_TASKHEAD_ID);
                bundle.putString(ARG_TASKHEAD_ID, taskHeadId);
            }
            // New
            else {
                if (getIntent().hasExtra(ARG_CNT_OF_TASKHEADS)) {
                    int cntOfTaskheads = getIntent().getIntExtra(ARG_CNT_OF_TASKHEADS, 0);
                    bundle.putInt(ARG_CNT_OF_TASKHEADS, cntOfTaskheads);
                }
            }
            taskHeadDetailFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskHeadDetailFragment, R.id.content_frame, TaskHeadDetailFragment.TAG);
        }

        // Create the presenter
        TaskHeadDetailPresenter presenter = new TaskHeadDetailPresenter(
                Injection.provideUseCaseHandler(),
                taskHeadId,
                taskHeadDetailFragment,
                Injection.provideSaveTaskHeadDetail(getApplicationContext()),
                Injection.provideGetTaskHeadDetail(getApplicationContext()),
                Injection.provideEditTaskHeadDetail(getApplicationContext()));

    }
}
