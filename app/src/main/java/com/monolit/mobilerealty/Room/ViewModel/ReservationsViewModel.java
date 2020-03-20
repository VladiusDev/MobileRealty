package com.monolit.mobilerealty.Room.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.monolit.mobilerealty.RealtorObjects.Reservation;
import com.monolit.mobilerealty.Room.RealtyDB;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReservationsViewModel extends AndroidViewModel {
    private static RealtyDB database;
    private List<Reservation> reservations;

    public ReservationsViewModel(@NonNull Application application) {
        super(application);

        database = RealtyDB.getInstance(getApplication());

        reservations = getAllReservations();
    }

    public List<Reservation> getAllReservations(){
        try {
            return new GetAllReservationsTask().execute().get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public void deleteAll(){
        new DeleteAllTask().execute();
    }

    public List<Reservation> getReservationsByFilter(String name){
        try {
            return new GetReservationsByFilterTask().execute(name).get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public void insertReservations(List<Reservation> reservations){
        new InsertReservations().execute(reservations);
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            database.reservationsDao().deleteAll();

            return null;
        }
    }
    private static class InsertReservations extends AsyncTask<List<Reservation>, Void, Void>{
        @Override
        protected Void doInBackground(List<Reservation>... lists) {
            if (lists != null && lists.length > 0){
                database.reservationsDao().insertReservations(lists[0]);
            }

            return null;
        }
    }

    private static class GetReservationsByFilterTask extends AsyncTask<String, Void, List<Reservation>> {
        @Override
        protected List<Reservation> doInBackground(String... strings) {
            return database.reservationsDao().getReservationsByFilter(strings[0]);
        }
    }

    private static class GetAllReservationsTask extends AsyncTask<Void, Void, List<Reservation>> {
        @Override
        protected List<Reservation> doInBackground(Void... voids) {
            return database.reservationsDao().getAllReservations();
        }
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
