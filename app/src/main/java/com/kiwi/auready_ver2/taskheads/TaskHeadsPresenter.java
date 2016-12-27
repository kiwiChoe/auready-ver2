package com.kiwi.auready_ver2.taskheads;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kiwi.auready_ver2.UseCase;
import com.kiwi.auready_ver2.UseCaseHandler;
import com.kiwi.auready_ver2.data.TaskHead;
import com.kiwi.auready_ver2.login.LoginFragment;
import com.kiwi.auready_ver2.taskheaddetail.TaskHeadDetailFragment;
import com.kiwi.auready_ver2.taskheads.domain.usecase.DeleteTaskHeads;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeads;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeadsCount;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by kiwi on 6/26/16.
 */
public class TaskHeadsPresenter implements TaskHeadsContract.Presenter {

    private final TaskHeadsContract.View mTaskHeadView;

    private UseCaseHandler mUseCaseHandler;
    private final GetTaskHeads mGetTaskHeads;
    private final DeleteTaskHeads mDeleteTaskHeads;
    private final GetTaskHeadsCount mGetTaskHeadCount;

    public TaskHeadsPresenter(UseCaseHandler useCaseHandler, @NonNull TaskHeadsContract.View tasksView,
                              @NonNull GetTaskHeads getTaskHeads,
                              @NonNull DeleteTaskHeads deleteTaskHeads,
                              @NonNull GetTaskHeadsCount getTaskHeadCount) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mTaskHeadView = checkNotNull(tasksView, "tasksView cannot be null!");
        mGetTaskHeads = checkNotNull(getTaskHeads, "getTaskHeads cannot be null");
        mDeleteTaskHeads = checkNotNull(deleteTaskHeads, "deleteTaskHeads cannot be null");
        mGetTaskHeadCount = checkNotNull(getTaskHeadCount);

        mTaskHeadView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTaskHeads();
    }

    @Override
    public void loadTaskHeads() {

        mUseCaseHandler.execute(mGetTaskHeads, new GetTaskHeads.RequestValues(),
                new UseCase.UseCaseCallback<GetTaskHeads.ResponseValue>() {

                    @Override
                    public void onSuccess(GetTaskHeads.ResponseValue response) {
                        List<TaskHead> taskHeads = response.getTaskHeads();
                        processTaskHeads(taskHeads);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void processTaskHeads(List<TaskHead> taskHeads) {
        if (taskHeads.isEmpty()) {
            mTaskHeadView.showNoTaskHeads();
        } else {
            mTaskHeadView.showTaskHeads(taskHeads);
        }
    }

    @Override
    public void addNewTaskHead() {
        // Get count of taskheads
        final int[] tmpArr = new int[1];
        mUseCaseHandler.execute(mGetTaskHeadCount, new GetTaskHeadsCount.RequestValues(),
                new UseCase.UseCaseCallback<GetTaskHeadsCount.ResponseValue>() {

                    @Override
                    public void onSuccess(GetTaskHeadsCount.ResponseValue response) {
                        tmpArr[0] = response.getTaskHeadsCount();
                    }

                    @Override
                    public void onError() {

                    }
                });

        int cntOfTaskHeads = tmpArr[0];
        mTaskHeadView.showTaskHeadDetail(cntOfTaskHeads);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (TaskHeadsActivity.REQ_ADD_TASKHEAD == requestCode && Activity.RESULT_OK == resultCode) {
            // Created TaskHead, Open TasksView of this taskhead
            if (data.hasExtra(TaskHeadDetailFragment.EXTRA_TASKHEAD_ID)) {
                String taskHeadId = data.getStringExtra(TaskHeadDetailFragment.EXTRA_TASKHEAD_ID);
                mTaskHeadView.showTasksView(taskHeadId);
            }
            // Canceled create taskhead, Open TaskHeadsView
        }
        if (TaskHeadsActivity.REQ_LOGINOUT == requestCode && Activity.RESULT_OK == resultCode) {

            boolean login = data.getBooleanExtra(LoginFragment.LOGIN_LOGOUT, false);
            boolean isSuccess = data.getBooleanExtra(LoginFragment.IS_SUCCESS, false);
            if (login && isSuccess) {
                mTaskHeadView.setLoginSuccessUI();
            } else if (!login && isSuccess) {
                mTaskHeadView.setLogoutSuccessUI();
            }
        }
    }

    @Override
    public void deleteTaskHeads(List<TaskHead> taskheads) {
        checkNotNull(taskheads);
        Log.d("TEST_TAG", "entered into TaskHeadsPresenter ---------------");
        Log.d("TEST_TAG", "taskheads size: " + taskheads.size());
        final List<String> taskHeadIds = new ArrayList<>();
        for (TaskHead taskHead : taskheads) {
            Log.d("TEST_TAG", taskHead.getTitle());
            taskHeadIds.add(taskHead.getId());
        }
        mUseCaseHandler.execute(mDeleteTaskHeads, new DeleteTaskHeads.RequestValues(taskHeadIds),
                new UseCase.UseCaseCallback<DeleteTaskHeads.ResponseValue>() {

                    @Override
                    public void onSuccess(DeleteTaskHeads.ResponseValue response) {
                        loadTaskHeads();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
