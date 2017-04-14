package com.kiwi.auready_ver2.taskheads;

import com.kiwi.auready_ver2.TestUseCaseScheduler;
import com.kiwi.auready_ver2.UseCaseHandler;
import com.kiwi.auready_ver2.data.TaskHead;
import com.kiwi.auready_ver2.data.source.TaskDataSource;
import com.kiwi.auready_ver2.data.source.TaskRepository;
import com.kiwi.auready_ver2.taskheads.domain.usecase.DeleteTaskHeads;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeadDetails;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeadsCount;
import com.kiwi.auready_ver2.taskheads.domain.usecase.InitializeLocalData;
import com.kiwi.auready_ver2.taskheads.domain.usecase.UpdateTaskHeadOrders;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.kiwi.auready_ver2.StubbedData.TaskStub.TASKHEADS;
import static com.kiwi.auready_ver2.StubbedData.TaskStub.TASKHEAD_DETAILS;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * TaskHeadsPresenter test
 */
public class TaskHeadsPresenterTest {

    private TaskHeadsPresenter mTaskHeadsPresenter;

    @Mock
    private TaskRepository mRepository;
    @Mock
    private TaskHeadsContract.View mTaskHeadView;

    @Captor
    private ArgumentCaptor<TaskDataSource.LoadTaskHeadDetailsCallback> mLoadTaskHeadsCallbackCaptor;

    @Captor
    private ArgumentCaptor<TaskDataSource.InitLocalDataCallback> mInitLocalDataCallbackCaptor;

    @Captor
    private ArgumentCaptor<TaskDataSource.DeleteTaskHeadsCallback> mDeleteTaskHeadsCallbackCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mTaskHeadsPresenter = givenTaskHeadsPresenter();
    }

    private TaskHeadsPresenter givenTaskHeadsPresenter() {

        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetTaskHeadDetails getTaskHeadDetails = new GetTaskHeadDetails(mRepository);
        DeleteTaskHeads deleteTaskHeads = new DeleteTaskHeads(mRepository);
        GetTaskHeadsCount getTaskHeadsCount = new GetTaskHeadsCount(mRepository);
        UpdateTaskHeadOrders updateTaskHeadOrders = new UpdateTaskHeadOrders(mRepository);
        InitializeLocalData initializeLocalData = new InitializeLocalData(mRepository);

        return new TaskHeadsPresenter(useCaseHandler, mTaskHeadView,
                getTaskHeadDetails, deleteTaskHeads, getTaskHeadsCount, updateTaskHeadOrders,
                initializeLocalData);
    }

    @Test
    public void loadAllTaskHeadsFromRepository_andLoadIntoView() {
        mTaskHeadsPresenter.loadTaskHeads();

        verify(mRepository).getTaskHeadDetails(mLoadTaskHeadsCallbackCaptor.capture());
        mLoadTaskHeadsCallbackCaptor.getValue().onTaskHeadDetailsLoaded(TASKHEAD_DETAILS);

        ArgumentCaptor<List> showTaskHeadsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mTaskHeadView).showTaskHeads(showTaskHeadsArgumentCaptor.capture());
        assertTrue(showTaskHeadsArgumentCaptor.getValue().size() == TASKHEAD_DETAILS.size());
    }

    @Test
    public void deleteTaskHeads_andDeleteTasks() {
        List<String> taskHeadIds = new ArrayList<>();
        taskHeadIds.add(TASKHEADS.get(0).getId());
        taskHeadIds.add(TASKHEADS.get(1).getId());
        taskHeadIds.add(TASKHEADS.get(2).getId());

        mTaskHeadsPresenter.deleteTaskHeads(TASKHEADS);

        verify(mRepository).deleteTaskHeads(eq(taskHeadIds), mDeleteTaskHeadsCallbackCaptor.capture());
    }

    @Test
    public void getTaskHeadsCountFromRepo_andShowsAddTaskHeadUi_whenCall_addNewTask() {
        mTaskHeadsPresenter.addNewTaskHead();

        verify(mRepository).getTaskHeadsCount();
        verify(mTaskHeadView).showTaskHeadDetail(anyInt());
    }

    @Test
    public void updateTaskHeadsOrder_toRepo() {
        mTaskHeadsPresenter.updateOrders(TASKHEADS);

        verify(mRepository).updateTaskHeadOrders((List<TaskHead>) anyCollectionOf(TaskHead.class));
    }

    @Test
    public void logoutIsSucceed_updateViews_andDeleteAllInRepo() {
        mTaskHeadsPresenter.onLogoutSuccess();

        verify(mTaskHeadView).setLogoutSuccessUI();

        verify(mRepository).initializeLocalData(mInitLocalDataCallbackCaptor.capture());
        mInitLocalDataCallbackCaptor.getValue().onInitSuccess();
    }
}