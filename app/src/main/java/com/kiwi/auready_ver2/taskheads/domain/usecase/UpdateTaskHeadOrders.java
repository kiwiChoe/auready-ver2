package com.kiwi.auready_ver2.taskheads.domain.usecase;

import android.support.annotation.NonNull;

import com.kiwi.auready_ver2.UseCase;
import com.kiwi.auready_ver2.data.TaskHead;
import com.kiwi.auready_ver2.data.source.TaskRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by kiwi on 8/26/16.
 */
public class UpdateTaskHeadOrders extends UseCase<UpdateTaskHeadOrders.RequestValues, UpdateTaskHeadOrders.ResponseValue> {

    private final TaskRepository mRepository;

    public UpdateTaskHeadOrders(@NonNull TaskRepository taskRepository) {
        mRepository = checkNotNull(taskRepository, "taskRepository cannot be null");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<TaskHead> taskHeads = requestValues.getTaskHeads();
        mRepository.updateTaskHeadOrders(taskHeads);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {
        private final List<TaskHead> mTaskHeads;

        public RequestValues(@NonNull List<TaskHead> taskHeads) {
            mTaskHeads = checkNotNull(taskHeads, "taskHeads cannot be null");
        }

        public List<TaskHead> getTaskHeads() {
            return mTaskHeads;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {    }

}