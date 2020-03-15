package com.monolit.mobilerealty.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class DataBaseConstants {
    public static final class DataBaseEntry implements BaseColumns {
        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INT = "INTEGER";
        public static final String TYPE_DOUBLE = "DOUBLE";

        // Realty objects
        public static final String REALTY_OBJECTS_TABLE_NAME = "realtyObjects";

        public static final String REALTY_OBJECTS_COLUMN_NAME = "name";
        public static final String REALTY_OBJECTS_COLUMN_ID_1C = "id1C";
        public static final String REALTY_OBJECTS_COLUMN_STATUS = "status";
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
                REALTY_OBJECTS_COLUMN_PRICE + " " + TYPE_DOUBLE + ", " +
                REALTY_OBJECTS_COLUMN_AMOUNT + " " + TYPE_DOUBLE + ", " +
                REALTY_OBJECTS_COLUMN_SQUARE + " " + TYPE_DOUBLE + ", " +
                REALTY_OBJECTS_COLUMN_SECTION + " " + TYPE_INT + ", " +
                REALTY_OBJECTS_COLUMN_FLOOR + " " + TYPE_INT + ", " +
                REALTY_OBJECTS_COLUMN_ROOMS + " " + TYPE_INT + ", " +
                REALTY_OBJECTS_COLUMN_APARTMENT + " " + TYPE_INT + ")";
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
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
