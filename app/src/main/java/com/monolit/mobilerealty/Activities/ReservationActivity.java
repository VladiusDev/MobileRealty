package com.monolit.mobilerealty.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.monolit.mobilerealty.DataBase.RealtyDataBase;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.RealtorObjects.Utils.RealtorObjectsUtils;

import org.json.JSONException;

public class ReservationActivity extends AppCompatActivity {

    private SearchView searchView;
    private Button btn_reservation;
    private Button btn_close;
    private String client_name;
    private String client_id1c;
    private String objectId;
    private String name;
    private SimpleCursorAdapter searchAdapter;
    private RealtyDataBase.DataBaseHelper dataBaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private TextView reservation_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        dataBaseHelper = new RealtyDataBase.DataBaseHelper(getApplicationContext());
        database = dataBaseHelper.getReadableDatabase();

        searchAdapter = new SimpleCursorAdapter(this, R.layout.search_item, null, new String[] { "name" }, new int[] { R.id.item }, 0);

        reservation_client = findViewById(R.id.reservation_client);

        searchView = findViewById(R.id.reservation_search);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String[] params = new String[] {"%" + newText + "%"};

                cursor = database.rawQuery(RealtyDataBase.DataBaseEntry.CLIENTS_QUERY_FILTER_SEARCH, params);

                searchAdapter.changeCursor(cursor);

                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                client_name = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_NAME));
                client_id1c = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_ID1C));

                reservation_client.setText(client_name);

                return false;
            }
        });

        searchView.setSuggestionsAdapter(searchAdapter);

        btn_reservation = findViewById(R.id.reservation_btn_reservation);
        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RealtorObjectsUtils.objectReservation(objectId, client_id1c, btn_close.getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_close = findViewById(R.id.reservation_btn_cancel);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        name = intent.getStringExtra("object");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.reservation_activity) + name);
    }

}
