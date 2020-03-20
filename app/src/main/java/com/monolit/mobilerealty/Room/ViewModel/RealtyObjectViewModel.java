package com.monolit.mobilerealty.Room.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.monolit.mobilerealty.RealtorObjects.RealtyObject;
import com.monolit.mobilerealty.Room.RealtyDB;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RealtyObjectViewModel extends AndroidViewModel {

    private static RealtyDB database;
    private List<RealtyObject> realtyObjects;

    public RealtyObjectViewModel(@NonNull Application application) {
        super(application);

        database = RealtyDB.getInstance(getApplication());

        realtyObjects = getAllRealtyObjects();
    }

    public List<RealtyObject> getAllRealtyObjects(){
        try {
            return new GetAllRealtyObjects().execute().get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public List<RealtyObject> getRealtyObjectsByFilter(String filter){
        try {
            return new GetRealtyObjectsByFilter().execute(filter).get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public void deleteAll(){
        new DeleteAllTask().execute();
    }

    public void insertAllRealtyObjects(List<RealtyObject> realtyObjects){
        new InsertAllRealtyObjectsTask().execute(realtyObjects);
    }

    private static class GetAllRealtyObjects extends AsyncTask<Void, Void, List<RealtyObject>>{
        @Override
        protected List<RealtyObject> doInBackground(Void... voids) {
            return database.realtyObjectsDao().getAllObjects();
        }
    }

    private static class GetRealtyObjectsByFilter extends AsyncTask<String, Void, List<RealtyObject>>{
        @Override
        protected List<RealtyObject> doInBackground(String... strings) {
            if (strings != null && strings.length > 0){
                return database.realtyObjectsDao().getRealtyObjectsByFilter(strings[0]);
            }

            return null;
        }
    }

    private static class InsertAllRealtyObjectsTask extends AsyncTask<List<RealtyObject>, Void, Void>{
        @Override
        protected Void doInBackground(List<RealtyObject>... lists) {
            if (lists != null && lists.length > 0){
                database.realtyObjectsDao().insertAllRealtyObjects(lists[0]);
            }

            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            database.realtyObjectsDao().deleteAll();

            return null;
        }
    }

    public List<RealtyObject> getRealtyObjects() {
        return realtyObjects;
    }

}
