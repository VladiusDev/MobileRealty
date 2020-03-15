package com.monolit.mobilerealty;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.RealtorObjects.RealtyObject;

@Database(entities = {RealtyObject.class, Client.class}, version = 1, exportSchema = false)
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

}
