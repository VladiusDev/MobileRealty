package com.monolit.mobilerealty.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.RealtorObjects.RealtyObject;
import com.monolit.mobilerealty.RealtorObjects.Reservation;
import com.monolit.mobilerealty.RealtorObjects.Task;
import com.monolit.mobilerealty.Room.Dao.ClientsDao;
import com.monolit.mobilerealty.Room.Dao.RealtyObjectsDao;
import com.monolit.mobilerealty.Room.Dao.ReservationsDao;
import com.monolit.mobilerealty.Room.Dao.TasksDao;

@Database(entities = {RealtyObject.class, Client.class, Task.class, Reservation.class}, version = 1, exportSchema = false)
public abstract class RealtyDB extends RoomDatabase {

    private static final String DB_NAME = "realty.db";
    private static RealtyDB database;
    private static final Object LOCK = new Object();

    public static RealtyDB getInstance(Context context){
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, RealtyDB.class, DB_NAME).build();
            }
        }

        return database;
    }

    public abstract RealtyObjectsDao realtyObjectsDao();

    public abstract ClientsDao clientsDao();

    public abstract TasksDao tasksDao();

    public abstract ReservationsDao reservationsDao();

}
