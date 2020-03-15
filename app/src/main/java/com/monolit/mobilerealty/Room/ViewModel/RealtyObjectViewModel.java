package com.monolit.mobilerealty.Room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.monolit.mobilerealty.RealtorObjects.RealtyObject;

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

    public RealtyObject getRealtyObjectById(String id){
        try {
            return new GetRealtyObjectTask().execute(id).get();
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

    public void insertRealtyObject(RealtyObject realtyObject){
        new InsertRealtyObjectTask().execute(realtyObject);
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

    private static class GetRealtyObjectTask extends AsyncTask<String, Void, RealtyObject>{
     @Override
        protected RealtyObject doInBackground(String... strings) {
            if (strings != null && strings.length > 0){
                return database.realtyObjectsDao().getRealtyObjectById(strings[0]);
            }

            return null;
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

    private static class InsertRealtyObjectTask extends AsyncTask<RealtyObject, Void, Void>{
        @Override
        protected Void doInBackground(RealtyObject... realtyObjects) {
            if (realtyObjects != null && realtyObjects.length > 0){
                database.realtyObjectsDao().insertRealtyObject(realtyObjects[0]);
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

    public List<RealtyObject> getRealtyObjects() {
        return realtyObjects;
    }

}
