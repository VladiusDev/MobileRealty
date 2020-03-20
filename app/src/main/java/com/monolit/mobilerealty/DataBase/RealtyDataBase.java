package com.monolit.mobilerealty.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class RealtyDataBase {
    public static final class DataBaseEntry implements BaseColumns {
        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INT = "INTEGER";
        public static final String TYPE_DOUBLE = "DOUBLE";

        // Realty objects
        public static final String REALTY_OBJECTS_TABLE_NAME = "realtyObjects";

        public static final String REALTY_OBJECTS_COLUMN_NAME = "name";
        public static final String REALTY_OBJECTS_COLUMN_ID_1C = "id";
        public static final String REALTY_OBJECTS_COLUMN_STATUS = "status";
        public static final String REALTY_OBJECTS_COLUMN_STATUS_ID = "statusId";
        public static final String REALTY_OBJECTS_COLUMN_PRICE = "price";
        public static final String REALTY_OBJECTS_COLUMN_AMOUNT = "amount";
        public static final String REALTY_OBJECTS_COLUMN_SQUARE = "square";
        public static final String REALTY_OBJECTS_COLUMN_SECTION = "section";
        public static final String REALTY_OBJECTS_COLUMN_FLOOR = "floor";
        public static final String REALTY_OBJECTS_COLUMN_ROOMS = "rooms";
        public static final String REALTY_OBJECTS_COLUMN_APARTMENT = "apartment";

        public static final String REALTY_OBJECTS_CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + REALTY_OBJECTS_TABLE_NAME +
                "(" + _ID + " " + TYPE_INT + " PRIMARY KEY AUTOINCREMENT, " +
                REALTY_OBJECTS_COLUMN_NAME + " " + TYPE_TEXT + ", "  +
                REALTY_OBJECTS_COLUMN_ID_1C + " " + TYPE_TEXT + ", " +
                REALTY_OBJECTS_COLUMN_STATUS + " " + TYPE_TEXT + ", " +
                REALTY_OBJECTS_COLUMN_STATUS_ID + " " + TYPE_TEXT + ", " +
                REALTY_OBJECTS_COLUMN_PRICE + " " + TYPE_DOUBLE + ", " +
                REALTY_OBJECTS_COLUMN_AMOUNT + " " + TYPE_DOUBLE + ", " +
                REALTY_OBJECTS_COLUMN_SQUARE + " " + TYPE_DOUBLE + ", " +
                REALTY_OBJECTS_COLUMN_SECTION + " " + TYPE_INT + ", " +
                REALTY_OBJECTS_COLUMN_FLOOR + " " + TYPE_INT + ", " +
                REALTY_OBJECTS_COLUMN_ROOMS + " " + TYPE_INT + ", " +
                REALTY_OBJECTS_COLUMN_APARTMENT + " " + TYPE_INT + ")";

        public static final String REALTY_OBJECTS_QUERY = String.format("SELECT * FROM %s ORDER BY %s", REALTY_OBJECTS_TABLE_NAME, REALTY_OBJECTS_COLUMN_NAME);
        public static final String REALTY_OBJECTS_QUERY_FILTER = String.format("SELECT * FROM %s WHERE %s LIKE ? ORDER BY %s", REALTY_OBJECTS_TABLE_NAME, REALTY_OBJECTS_COLUMN_NAME, REALTY_OBJECTS_COLUMN_NAME);

        // Tasks
        public static final String TASK_TABLE_NAME = "tasks";

        public static final String TASK_COLUMN_TITLE = "title";
        public static final String TASK_COLUMN_DATE = "date";
        public static final String TASK_COLUMN_DESCRIPTION = "description";
        public static final String TASK_COLUMN_AUTHOR = "author";
        public static final String TASK_COLUMN_ID_1C = "id1C";
        public static final String TASK_COLUMN_DEADLINE = "deadline";

        public static final String TASK_CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TASK_TABLE_NAME +
                "(" + _ID + " " + TYPE_INT + " PRIMARY KEY AUTOINCREMENT, " +
                TASK_COLUMN_TITLE + " " + TYPE_TEXT + ", "  +
                TASK_COLUMN_DESCRIPTION + " " + TYPE_TEXT + ", "  +
                TASK_COLUMN_DATE + " " + TYPE_TEXT + ", " +
                TASK_COLUMN_AUTHOR + " " + TYPE_TEXT + ", " +
                TASK_COLUMN_DEADLINE + " " + TYPE_TEXT + ", " +
                TASK_COLUMN_ID_1C + " " + TYPE_TEXT + ")";

        public static final String TASK_QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC", TASK_TABLE_NAME, TASK_COLUMN_DATE);
        public static final String TASK_QUERY_FILTER = String.format("SELECT * FROM %s WHERE %s LIKE ? OR %s LIKE ? ORDER BY %s DESC" , TASK_TABLE_NAME, TASK_COLUMN_TITLE, TASK_COLUMN_AUTHOR, TASK_COLUMN_DATE);

        // Clients
        public static final String CLIENTS_TABLE_NAME = "clients";

        public static final String CLIENTS_COLUMN_NAME = "name";
        public static final String CLIENTS_COLUMN_ADDRESS = "address";
        public static final String CLIENTS_COLUMN_PHONE = "phone";
        public static final String CLIENTS_COLUMN_EMAIL = "email";
        public static final String CLIENTS_COLUMN_MANAGER = "manager";
        public static final String CLIENTS_COLUMN_ID1C = "id1C";

        public static final String CLIENTS_CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + CLIENTS_TABLE_NAME +
                "(" + _ID + " " + TYPE_INT + " PRIMARY KEY AUTOINCREMENT, " +
                CLIENTS_COLUMN_NAME + " " + TYPE_TEXT + ", " +
                CLIENTS_COLUMN_ADDRESS + " " + TYPE_TEXT + ", " +
                CLIENTS_COLUMN_PHONE + " " + TYPE_TEXT + ", " +
                CLIENTS_COLUMN_MANAGER + " " + TYPE_TEXT + ", " +
                CLIENTS_COLUMN_EMAIL + " " + TYPE_TEXT + ", " +
                CLIENTS_COLUMN_ID1C + " " + TYPE_TEXT + ")";

        public static final String CLIENTS_QUERY = String.format("SELECT * FROM %s ORDER BY %s LIMIT ? OFFSET ?", CLIENTS_TABLE_NAME, CLIENTS_COLUMN_NAME);
        public static final String CLIENTS_QUERY_FILTER = String.format("SELECT * FROM %s WHERE %s LIKE ? OR %s LIKE ? ORDER BY %s LIMIT ? OFFSET ?",
                CLIENTS_TABLE_NAME, CLIENTS_COLUMN_NAME, CLIENTS_COLUMN_PHONE, CLIENTS_COLUMN_NAME);
        public static final String CLIENTS_QUERY_FILTER_SEARCH = String.format("SELECT * FROM %s WHERE %s LIKE ? LIMIT 5",
                CLIENTS_TABLE_NAME, CLIENTS_COLUMN_NAME);
        public static final String CLIENTS_ROW_DELETE = String.format("DELETE FROM %s WHERE %s = ?" , CLIENTS_TABLE_NAME, CLIENTS_COLUMN_ID1C);

        // Reservations
        public static final String RESERVATIONS_TABLE_NAME = "reservations";

        public static final String RESERVATIONS_COLUMN_REALTY_OBJECT = "realtyObject";
        public static final String RESERVATIONS_COLUMN_RESERVATION = "reservation";
        public static final String RESERVATIONS_COLUMN_CLIENT = "client";
        public static final String RESERVATIONS_COLUMN_DATE = "date";
        public static final String RESERVATIONS_COLUMN_STATUS = "status";
        public static final String RESERVATIONS_COLUMN_ID1C = "id1C";
        public static final String RESERVATIONS_COLUMN_IS_CRM = "isCRM";

        public static final String RESERVATIONS_CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + RESERVATIONS_TABLE_NAME +
                "(" + _ID + " " + TYPE_INT + " PRIMARY KEY AUTOINCREMENT, " +
                RESERVATIONS_COLUMN_REALTY_OBJECT + " " + TYPE_TEXT + ", " +
                RESERVATIONS_COLUMN_RESERVATION + " " + TYPE_TEXT + ", " +
                RESERVATIONS_COLUMN_CLIENT + " " + TYPE_TEXT + ", " +
                RESERVATIONS_COLUMN_DATE + " " + TYPE_TEXT + ", " +
                RESERVATIONS_COLUMN_STATUS + " " + TYPE_TEXT + ", " +
                RESERVATIONS_COLUMN_ID1C + " " + TYPE_TEXT + ", " +
                RESERVATIONS_COLUMN_IS_CRM + " " + TYPE_INT + ")";

        public static final String RESERVATIONS_QUERY = String.format("SELECT * FROM %s ORDER BY %s", RESERVATIONS_TABLE_NAME, RESERVATIONS_COLUMN_CLIENT);
        public static final String RESERVATIONS_QUERY_FILTER = String.format("SELECT * FROM %s WHERE %s LIKE ? OR %s LIKE ? OR %s LIKE ? ORDER BY %s",
                RESERVATIONS_TABLE_NAME, RESERVATIONS_COLUMN_CLIENT, RESERVATIONS_COLUMN_REALTY_OBJECT, RESERVATIONS_COLUMN_RESERVATION, RESERVATIONS_COLUMN_CLIENT);
    }

    public static class DataBaseHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "realtyDataBase.db";
        public static final int DB_VERSION = 1;

        public DataBaseHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DataBaseEntry.REALTY_OBJECTS_CREATE_COMMAND);
            database.execSQL(DataBaseEntry.TASK_CREATE_COMMAND);
            database.execSQL(DataBaseEntry.CLIENTS_CREATE_COMMAND);
            database.execSQL(DataBaseEntry.RESERVATIONS_CREATE_COMMAND);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        public void clearDB(){
            SQLiteDatabase database = this.getWritableDatabase();

            database.delete(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_TABLE_NAME, null, null);
            database.delete(RealtyDataBase.DataBaseEntry.CLIENTS_TABLE_NAME, null, null);
            database.delete(RealtyDataBase.DataBaseEntry.TASK_TABLE_NAME, null, null);
            database.delete(RealtyDataBase.DataBaseEntry.RESERVATIONS_TABLE_NAME, null, null);

            database.close();
        }

    }
}
