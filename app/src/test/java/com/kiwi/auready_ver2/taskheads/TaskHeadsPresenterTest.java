package com.kiwi.auready_ver2.taskheads;

import com.kiwi.auready_ver2.TestUseCaseScheduler;
import com.kiwi.auready_ver2.UseCaseHandler;
import com.kiwi.auready_ver2.data.source.TaskHeadDataSource.LoadTaskHeadsCallback;
import com.kiwi.auready_ver2.data.source.TaskHeadRepository;
import com.kiwi.auready_ver2.taskheads.domain.usecase.DeleteTaskHeads;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeads;
import com.kiwi.auready_ver2.taskheads.domain.usecase.GetTaskHeadsCount;
import com.kiwi.auready_ver2.taskheads.domain.usecase.UpdateTaskHeadsOrder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.kiwi.auready_ver2.StubbedData.TaskStub.TASKHEADS;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by kiwi on 8/23/16.
 */
public class TaskHeadsPresenterTest {

    private TaskHeadsPresenter mTaskHeadsPresenter;

    @Mock
    private TaskHeadRepository mTaskHeadRepository;
    @Mock
    private TaskHeadsContract.View mTaskHeadView;

    @Captor
    private ArgumentCaptor<LoadTaskHeadsCallback> mLoadTaskHeadsCallbackCaptor;
//    @Captor
//    private ArgumentCaptor<TaskDataSource.DeleteTasksCallback> mDeleteTasksCallbackCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mTaskHeadsPresenter = givenTaskHeadsPresenter();
    }

    private TaskHeadsPresenter givenTaskHeadsPresenter() {

        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetTaskHeads getTaskHeads = new GetTaskHeads(mTaskHeadRepository);
        DeleteTaskHeads deleteTaskHeads = new DeleteTaskHeads(mTaskHeadRepository);
        GetTaskHeadsCount getTaskHeadsCount = new GetTaskHeadsCount(mTaskHeadRepository);
        UpdateTaskHeadsOrder updateTaskHeadsOrder = new UpdateTaskHeadsOrder(mTaskHeadRepository);

        return new TaskHeadsPresenter(useCaseHandler, mTaskHeadView,
                getTaskHeads, deleteTaskHeads, getTaskHeadsCount, updateTaskHeadsOrder);
    }

    @Test
    public void loadAllTaskHeadsFromRepository_andLoadIntoView() {
        mTaskHeadsPresenter.loadTaskHeads();

        verify(mTaskHeadRepository).getTaskHeads(mLoadTaskHeadsCallbackCaptor.capture());
        mLoadTaskHeadsCallbackCaptor.getValue().onTaskHeadsLoaded(TASKHEADS);

        ArgumentCaptor<List> showTaskHeadsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mTaskHeadView).showTaskHeads(showTaskHeadsArgumentCaptor.capture());
        assertTrue(showTaskHeadsArgumentCaptor.getValue().size() == TASKHEADS.size());
    }

//    @Test
//    public void deleteTaskHeads_andDeleteTasks() {
//        List<String> taskHeadIds = new ArrayList<>();
//        taskHeadIds.add(TASKHEADS.get(0).getTaskHeadId());
//        taskHeadIds.add(TASKHEADS.get(1).getTaskHeadId());
//        taskHeadIds.add(TASKHEADS.get(2).getTaskHeadId());
//
//        mTaskHeadsPresenter.deleteTaskHeads(TASKHEADS);
//
//        verify(mTaskHeadRepository).deleteTaskHeads(taskHeadIds);
//    }
//
//    @Test
//    public void getTaskHeadsCountFromRepo_andShowsAddTaskHeadUi_whenCall_addNewTask() {
//        mTaskHeadsPresenter.addNewTaskHead();
//
//        verify(mTaskHeadRepository).getTaskHeadsCount();
//        verify(mTaskHeadView).showTaskHeadDetail(anyInt());
//    }
}