package com.kiwi.auready_ver2.tasks.domain.usecase;

import android.support.annotation.NonNull;

import com.kiwi.auready_ver2.UseCase;
import com.kiwi.auready_ver2.data.Task;
import com.kiwi.auready_ver2.data.source.TaskRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by kiwi on 9/17/16.
 */
public class SortTasks extends UseCase<SortTasks.RequestValues, SortTasks.ResponseValue> {

    private final TaskRepository mTaskRepository;

    public SortTasks(@NonNull TaskRepository taskRepository) {
        mTaskRepository = checkNotNull(taskRepository, "taskRepository cannot be null");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<Task> tasks = requestValues.getTasks();
        mTaskRepository.sortTasks(tasks);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final List<Task> mTasks;

        public RequestValues(@NonNull List<Task> tasks) {
            mTasks = checkNotNull(tasks, "tasks cannot be null");
        }

        public List<Task> getTasks() {
            return mTasks;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
