package com.monolit.mobilerealty.Room.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.Room.RealtyDB;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientsViewModel extends AndroidViewModel {

    private static RealtyDB database;
    private List<Client> clients;

    public ClientsViewModel(@NonNull Application application) {
        super(application);

        database = RealtyDB.getInstance(getApplication());

        clients = getAllClients(0);
    }

    public void insertAllClients(List<Client> clients){
        new InsertAllClientsTask().execute(clients);
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Client> getAllClients(int offset){
        try {
            return new GetAllClientsTask().execute(offset).get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public List<Client> getClientsByFilter(String name){
        try {
            return new GetClientsByFilter().execute(name).get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    private class GetClientsByFilter extends AsyncTask<String, Void, List<Client>>{

        @Override
        protected List<Client> doInBackground(String... strings) {
            return database.clientsDao().getClientsByFilter(strings[0]);
        }
    }

    private static class GetAllClientsTask extends AsyncTask<Integer, Void, List<Client>>{
        @Override
        protected List<Client> doInBackground(Integer... integers) {
            return database.clientsDao().getAllClients(integers[0]);
        }
    }

    private static class InsertAllClientsTask extends AsyncTask<List<Client>, Void, Void>{
        @Override
        protected Void doInBackground(List<Client>... lists) {
            if (lists != null && lists.length > 0){
                database.clientsDao().insertClients(lists[0]);
            }

            return null;
        }
    }

}
