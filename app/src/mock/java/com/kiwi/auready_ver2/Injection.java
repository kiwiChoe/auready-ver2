package com.kiwi.auready_ver2;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kiwi.auready_ver2.data.source.FakeFriendRemoteDataSource;
import com.kiwi.auready_ver2.data.source.FriendDataSource;
import com.kiwi.auready_ver2.data.source.FriendRepository;
import com.kiwi.auready_ver2.data.source.TaskRepository;
import com.kiwi.auready_ver2.data.source.local.FriendLocalDataSource;
import com.kiwi.auready_ver2.data.source.local.TaskLocalDataSource;
import com.kiwi.auready_ver2.friend.domain.usecase.AddFriend;
import com.kiwi.auready_ver2.friend.domain.usecase.DeleteFriend;
import com.kiwi.auready_ver2.friend.domain.usecase.FindPeople;
import com.kiwi.auready_ver2.friend.domain.usecase.GetFriends;
import com.kiwi.auready_ver2.friend.domain.usecase.SaveFriend;
import com.kiwi.auready_ver2.taskheaddetail.domain.usecase.EditTaskHeadDetail;
import com.kiwi.auready_ver2.taskheaddetail.domain.usecase.GetTaskHeadDetail;
import com.kiwi.auready_ver2.taskheaddetail.domain.usecase.SaveTaskHeadDetail;
import com.kiwi.auready_ver2.taskheads.domain.usecase.DeleteTaskHeads;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeads;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeadsCount;
import com.kiwi.auready_ver2.taskheads.domain.usecase.UpdateTaskHeadOrders;
import com.kiwi.auready_ver2.tasks.domain.usecase.DeleteTasks;
import com.kiwi.auready_ver2.tasks.domain.usecase.EditTasks;
import com.kiwi.auready_ver2.tasks.domain.usecase.GetMembers;
import com.kiwi.auready_ver2.tasks.domain.usecase.GetTasks;
import com.kiwi.auready_ver2.tasks.domain.usecase.SaveTask;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for {@link FriendDataSource} at compile time.
 * This is useful for testing, since it allows us to use a fake instance of the class
 * to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    /*
    * Friend
    * */
    public static FriendRepository provideFriendRepository(@NonNull Context context) {
        checkNotNull(context);
        return FriendRepository.getInstance(FriendLocalDataSource.getInstance(context),
                FakeFriendRemoteDataSource.getInstance());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static GetFriends provideGetFriends(@NonNull Context context) {
        return new GetFriends(Injection.provideFriendRepository(context));
    }

    public static SaveFriend provideSaveFriend(@NonNull Context context) {
        return new SaveFriend(Injection.provideFriendRepository(context));
    }

    public static DeleteFriend provideDeleteFriend(@NonNull Context context) {
        return new DeleteFriend(Injection.provideFriendRepository(context));
    }


    /*
    * Task
    * */
    public static TaskRepository provideTaskRepository(@NonNull Context context) {
        checkNotNull(context);
        return TaskRepository.getInstance(TaskLocalDataSource.getInstance(context));
    }

    public static GetTaskHeads provideGetTaskHeads(@NonNull Context context) {
        return new GetTaskHeads(Injection.provideTaskRepository(context));
    }

    public static GetTaskHeadsCount provideGetTaskHeadsCount(@NonNull Context context) {
        return new GetTaskHeadsCount(Injection.provideTaskRepository(context));
    }

    public static UpdateTaskHeadOrders provideUpdateTaskHeadsOrder(@NonNull Context context) {
        return new UpdateTaskHeadOrders(Injection.provideTaskRepository(context));
    }

    public static SaveTaskHeadDetail provideSaveTaskHeadDetail(@NonNull Context context) {
        return new SaveTaskHeadDetail(Injection.provideTaskRepository(context));
    }

    public static DeleteTaskHeads provideDeleteTaskHeads(@NonNull Context context) {
        return new DeleteTaskHeads(Injection.provideTaskRepository(context));
    }

    public static GetTaskHeadDetail provideGetTaskHeadDetail(@NonNull Context context) {
        return new GetTaskHeadDetail(Injection.provideTaskRepository(context));
    }

    public static EditTaskHeadDetail provideEditTaskHeadDetail(@NonNull Context context) {
        return new EditTaskHeadDetail(Injection.provideTaskRepository(context));
    }

    public static GetMembers provideGetMembers(@NonNull Context context) {
        return new GetMembers(Injection.provideTaskRepository(context));
    }

    public static GetTasks provideGetTasks(@NonNull Context context) {
        return new GetTasks(Injection.provideTaskRepository(context));
    }

    public static SaveTask provideSaveTask(@NonNull Context context) {
        return new SaveTask(Injection.provideTaskRepository(context));
    }

    public static DeleteTasks provideDeleteTasks(@NonNull Context context) {
        return new DeleteTasks(Injection.provideTaskRepository(context));
    }

    public static EditTasks provideEditTasks(@NonNull Context context) {
        return new EditTasks(Injection.provideTaskRepository(context));
    }

    public static FindPeople provideFindPeople(@NonNull Context context) {
        return new FindPeople(Injection.provideFriendRepository(context));
    }

    public static AddFriend provideAddFriend(@NonNull Context context) {
        return new AddFriend(Injection.provideFriendRepository(context));
    }
}
