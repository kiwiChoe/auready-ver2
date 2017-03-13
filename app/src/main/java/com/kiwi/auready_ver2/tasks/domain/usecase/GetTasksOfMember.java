package com.kiwi.auready_ver2.tasks.domain.usecase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kiwi.auready_ver2.UseCase;
import com.kiwi.auready_ver2.data.Task;
import com.kiwi.auready_ver2.data.source.TaskDataSource;
import com.kiwi.auready_ver2.data.source.TaskRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fetches the list of tasks by memberId.
 */
public class GetTasksOfMember extends UseCase<GetTasksOfMember.RequestValues, GetTasksOfMember.ResponseValue> {


    private final TaskRepository mTaskRepository;

    public GetTasksOfMember(@NonNull TaskRepository taskRepository) {
        mTaskRepository = checkNotNull(taskRepository, "taskRepository cannot be null");
    }

    public GetTasksOfMember(@NonNull GetTasksOfMember getTasksOfMember){
        mTaskRepository = getTasksOfMember.mTaskRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mTaskRepository.getTasksOfMember(values.getMemberId(), new TaskDataSource.LoadTasksCallback() {

            @Override
            public void onTasksLoaded(List<Task> tasks) {
                getUseCaseCallback().onSuccess(new ResponseValue(tasks));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private final String mMemberId;

        public RequestValues(@NonNull String memberId) {
            mMemberId = checkNotNull(memberId);
        }

        public String getMemberId() {
            return mMemberId;
        }
    }

    public class ResponseValue implements UseCase.ResponseValue {

        private final List<Task> mTasks;


        public ResponseValue(List<Task> tasks) {
            mTasks = tasks;
        }

        public List<Task> getTasks() {
            return mTasks;
        }
    }
}
