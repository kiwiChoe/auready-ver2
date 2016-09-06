package com.kiwi.auready_ver2.tasks;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kiwi.auready_ver2.UseCase;
import com.kiwi.auready_ver2.UseCaseHandler;
import com.kiwi.auready_ver2.data.Task;
import com.kiwi.auready_ver2.tasks.domain.filter.FilterFactory;
import com.kiwi.auready_ver2.tasks.domain.usecase.GetTasks;
import com.kiwi.auready_ver2.tasks.domain.usecase.SaveTask;
import com.kiwi.auready_ver2.tasks.domain.usecase.SaveTasks;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by kiwi on 8/26/16.
 */
public class TasksPresenter implements TasksContract.Presenter {

    private final UseCaseHandler mUseCaseHandler;
    private final TasksContract.View mTasksView;
    private final GetTasks mGetTasks;
    private final SaveTasks mSaveTasks;
    private final SaveTask mSaveTask;
    private final FilterFactory mFilterFactory;

    private String mTaskHeadId;

    public TasksPresenter(@NonNull UseCaseHandler useCaseHandler,
                          String taskHeadId,
                          @NonNull TasksContract.View tasksView,
                          @NonNull GetTasks getTasks,
                          @NonNull SaveTasks saveTasks, @NonNull SaveTask saveTask) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandler cannot be null");
        mTaskHeadId = taskHeadId;
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");

        mGetTasks = checkNotNull(getTasks, "getTasks cannot be null!");
        mSaveTasks = checkNotNull(saveTasks, "saveTasks cannot be null!");
        mSaveTask = checkNotNull(saveTask, "saveTask cannot be null!");

        mFilterFactory = new FilterFactory();

        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks();
    }

    @Override
    public void loadTasks() {

        if(mTaskHeadId == null || mTaskHeadId.isEmpty()) {
            Log.d("test", "entered mTaskHeadId is null? or empty");
            mTasksView.showEmptyTaskHeadError();
            return;
        }

        mUseCaseHandler.execute(mGetTasks, new GetTasks.RequestValues(mTaskHeadId),
                new UseCase.UseCaseCallback<GetTasks.ResponseValue>() {
                    @Override
                    public void onSuccess(GetTasks.ResponseValue response) {
                        Log.d("test", "entered GetTask onSuccess()");
                        List<Task> tasks = response.getTasks();
                        mTasksView.showTasks(tasks);
                    }

                    @Override
                    public void onError() {

                        Log.d("test", "entered GetTask onError()");
                        mTasksView.showEmptyTaskHeadError();
                    }
                });

    }

    @Override
    public void validateEmptyTaskHead(String taskHeadTitle, int numOfTasks) {

        if (taskHeadTitle.isEmpty() &&
                (numOfTasks == 0)) {
            mTasksView.showEmptyTaskHeadError();
        }

    }

    @Override
    public void saveTask(@NonNull final Task task) {
        checkNotNull(task);

        mUseCaseHandler.execute(mSaveTask, new SaveTask.RequestValues(task),
                new UseCase.UseCaseCallback<SaveTask.ResponseValue>() {
                    @Override
                    public void onSuccess(SaveTask.ResponseValue response) {
                        loadTasks();
                    }

                    @Override
                    public void onError() {
                        mTasksView.showLoadingErrorTasksError();
                    }
                });
    }
}
