package com.monolit.mobilerealty.JsonUtils;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.monolit.mobilerealty.DataBase.RealtyDataBase;
import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.RealtorObjects.RealtyObject;
import com.monolit.mobilerealty.RealtorObjects.Reservation;
import com.monolit.mobilerealty.RealtorObjects.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class JsonParser1C {

    public static HashMap<String, String> getUserInfo(String jsonText){
        HashMap<String, String> hashMap = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonText);

            String fullName    = jsonObject.getString("fullName");
            String department  = jsonObject.getString("department");
            String phoneNumber = jsonObject.getString("phoneNumber");

            hashMap.put("fullName",    fullName);
            hashMap.put("department",  department);
            hashMap.put("phoneNumber", phoneNumber);
        } catch (JSONException e) {
            return hashMap;
        }

        return hashMap;
    }

    public static ArrayList<RealtyObject> getFreeObjects(String jsonText){
        try {
            return new GetFreeObjectsTask().execute(jsonText).get();
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public static ArrayList<Task> getTasks(String jsonText){
        ArrayList<Task> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject  = new JSONObject(jsonText);
            JSONArray jsonArray    = jsonObject.getJSONArray("RealtyData");

            for (int i = 0; i < jsonArray.length(); i++){
                int index = i;
                ContentValues values = new ContentValues();
                JSONObject task = jsonArray.getJSONObject(index);

                String author = task.getString("author");
                String date = task.getString("date");
                String id = task.getString("id");
                String title = task.getString("title");
                String description = task.getString("description");
                String deadline = task.getString("deadline");

                arrayList.add(new Task(title, description, date, deadline, author, id));
            }

            return arrayList;
        } catch (JSONException e) {
            return arrayList;
        }
    }

    public static ArrayList<Client> getClients(String jsonText){
        ArrayList<Client> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject  = new JSONObject(jsonText);
            JSONArray jsonArray    = jsonObject.getJSONArray("RealtyData");

            for (int i = 0; i < jsonArray.length(); i++){
                int index = i;
                JSONObject clients = jsonArray.getJSONObject(index);

                String name = clients.getString(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_NAME);
                String phone = clients.getString(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_PHONE);
                String address = clients.getString(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_ADDRESS);
                String email = clients.getString(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_EMAIL);
                String manager = clients.getString(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_MANAGER);
                String id1c = clients.getString(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_ID1C);

                arrayList.add(new Client(name, phone, email, address, manager, id1c));
            }

            return arrayList;
        } catch (JSONException e) {
            return arrayList;
        }
    }

    public static ArrayList<Reservation> getReservations(String jsonText){
        ArrayList<Reservation> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject  = new JSONObject(jsonText);
            JSONArray jsonArray    = jsonObject.getJSONArray("RealtyData");

            for (int i = 0; i < jsonArray.length(); i++){
                int index = i;
                ContentValues values = new ContentValues();
                JSONObject clients = jsonArray.getJSONObject(index);

                String realtyObject = clients.getString(RealtyDataBase.DataBaseEntry.RESERVATIONS_COLUMN_REALTY_OBJECT);
                String reservation = clients.getString(RealtyDataBase.DataBaseEntry.RESERVATIONS_COLUMN_RESERVATION);
                String client = clients.getString(RealtyDataBase.DataBaseEntry.RESERVATIONS_COLUMN_CLIENT);
                String date = clients.getString(RealtyDataBase.DataBaseEntry.RESERVATIONS_COLUMN_DATE);
                int status = clients.getInt(RealtyDataBase.DataBaseEntry.RESERVATIONS_COLUMN_STATUS);
                String id1c = clients.getString(RealtyDataBase.DataBaseEntry.RESERVATIONS_COLUMN_ID1C);
                int isCRM = clients.getInt(RealtyDataBase.DataBaseEntry.RESERVATIONS_COLUMN_IS_CRM);

                arrayList.add(new Reservation(realtyObject, client, date, status, isCRM, reservation, id1c));
            }

            return arrayList;
        } catch (JSONException e) {
            return arrayList;
        }
    }

    public static Boolean getJsonResult(String jsonResult){
        if (!jsonResult.isEmpty()) {
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(jsonResult);
            } catch (JSONException e) {
                return false;
            }

            if (jsonObject != null) {
                try {
                    Boolean result = jsonObject.getBoolean("result");

                    return result;
                } catch (JSONException e) {
                    return false;
                }
            }
        }

        return false;
    }

    static class GetFreeObjectsTask extends AsyncTask<String, Void, ArrayList<RealtyObject>> {
        @Override
        protected ArrayList<RealtyObject> doInBackground(String... strings) {
            ArrayList<RealtyObject> objects = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(strings[0]);
                JSONArray jsonArray = jsonObject.getJSONArray("RealtyData");

                for (int i = 0; i < jsonArray.length(); i++) {
                    int index = i;
                    JSONObject realtyObjectJson = jsonArray.getJSONObject(index);

                    String name = realtyObjectJson.getString(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_NAME);
                    String status = realtyObjectJson.getString(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_STATUS);
                    String id_1c = realtyObjectJson.getString(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_ID_1C);
                    Double square = realtyObjectJson.getDouble(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_SQUARE);
                    Double price = realtyObjectJson.getDouble(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_PRICE);
                    Double amount = realtyObjectJson.getDouble(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_AMOUNT);
                    int section = realtyObjectJson.getInt(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_SECTION);
                    int floor = realtyObjectJson.getInt(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_FLOOR);
                    int rooms = realtyObjectJson.getInt(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_ROOMS);
                    int apartment = realtyObjectJson.getInt(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_APARTMENT);
                    int status_id = realtyObjectJson.getInt(RealtyDataBase.DataBaseEntry.REALTY_OBJECTS_COLUMN_STATUS_ID);

                    objects.add(new RealtyObject(name, status, status_id, price, amount, square, section, floor, rooms, apartment, id_1c));
                }

                return objects;
            } catch (JSONException e) {
                return objects;
            }
        }
    }
}
