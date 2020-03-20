package com.monolit.mobilerealty.Room.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.monolit.mobilerealty.RealtorObjects.Task;
import com.monolit.mobilerealty.Room.RealtyDB;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TasksViewModel extends AndroidViewModel {

    private static RealtyDB database;
    private List<Task> tasks;

    public TasksViewModel(@NonNull Application application) {
        super(application);

        database = RealtyDB.getInstance(getApplication());

        tasks = getAllTasks();
    }

    public void deleteAll(){
        new DeleteAllTask().execute();
    }

    public List<Task> getAllTasks(){
        try {
            return new GetAllTasksTask().execute().get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public List<Task> getTasksByFilter(String title){
        try {
            return new GetTasksByFilter().execute(title).get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            database.tasksDao().deleteAll();

            return null;
        }
    }

    public void insertTasks(List<Task> tasks){
        new InsertTasks().execute(tasks);
    }

    private static class GetTasksByFilter extends AsyncTask<String, Void, List<Task>>{
        @Override
        protected List<Task> doInBackground(String... strings) {
            return database.tasksDao().getTasksByFilter(strings[0]);
        }
    }

    private static class InsertTasks extends AsyncTask<List<Task>, Void, Void>{
        @Override
        protected Void doInBackground(List<Task>... lists) {
            if (lists != null && lists.length > 0){
                database.tasksDao().insertTasks(lists[0]);
            }

            return null;
        }
    }

    private static class GetAllTasksTask extends AsyncTask<Void, Void, List<Task>> {
        @Override
        protected List<Task> doInBackground(Void... voids) {
            return database.tasksDao().getAllTasks();
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
