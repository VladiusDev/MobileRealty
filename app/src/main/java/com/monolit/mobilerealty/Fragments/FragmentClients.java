package com.monolit.mobilerealty.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.monolit.mobilerealty.Adapters.ClientsListAdapter;
import com.monolit.mobilerealty.DataBase.RealtyDataBase;
import com.monolit.mobilerealty.Dialog.DialogAlerts;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.JsonUtils.JsonParser1C;
import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.WebServiceUtils.WebService1C;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class FragmentClients extends Fragment{

    private ArrayList<Client> clients = new ArrayList<>();
    private RecyclerView mRecyclerViewClients;
    private SwipeRefreshLayout mSwipeRefresh;
    private ClientsListAdapter mClientsListAdapter;
    private ProgressDialog progressDialog;
    private RealtyDataBase.DataBaseHelper databaseHelper;
    private SQLiteDatabase database;
    private SharedPreferences preferences;
    private TextView txt_clientsList_update_data;
    private ConstraintLayout clientslist_constraintLayout;
    private ImageView clientslist_img_empty;
    private TextView clientslist_txt_empty;
    private Button clientslist_btn_update;
    private AlertDialog alertDialog;
    private View viewCard;
    private TextView client_name;
    private TextView client_email;
    private TextView client_phone;
    private TextView client_address;
    private TextView client_manager;
    private Button client_btn_close;

    public FragmentClients() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_clients, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        txt_clientsList_update_data = view.findViewById(R.id.txt_clientsList_update_data);
        clientslist_constraintLayout = view.findViewById(R.id.clientslist_constraintLayout);
        clientslist_img_empty = view.findViewById(R.id.clientslist_img_empty);
        clientslist_txt_empty = view.findViewById(R.id.clientslist_txt_empty);
        clientslist_btn_update = view.findViewById(R.id.clientslist_btn_update);
        clientslist_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getContext().getString(R.string.clientslist_download));
                progressDialog.show();

                downloadDataToDB();
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        mRecyclerViewClients = view.findViewById(R.id.rw_clientList);
        mSwipeRefresh = view.findViewById(R.id.swipe_container_client_list);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadDataToDB();
            }
        });

        mClientsListAdapter  = new ClientsListAdapter(clients);
        mClientsListAdapter.setOnClickClientListener(new ClientsListAdapter.OnClickClientListener() {
            @Override
            public void OnClickClient(int position) {
                Client client = clients.get(position);

                if (client != null){
                    viewCard = LayoutInflater.from(getContext()).inflate(R.layout.activity_client_card, null);

                    client_name = viewCard.findViewById(R.id.client_name);
                    client_email = viewCard.findViewById(R.id.client_email);
                    client_phone = viewCard.findViewById(R.id.client_phone);
                    client_address = viewCard.findViewById(R.id.client_address);
                    client_manager = viewCard.findViewById(R.id.client_manager);
                    client_btn_close = viewCard.findViewById(R.id.client_btn_close);
                    client_btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    client_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String phoneNumber = client_phone.getText().toString();

                            if (!phoneNumber.isEmpty()) {
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                                        DialogAlerts.showDialogAccessToGallery(getActivity());
                                    } else {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 202);
                                    }
                                }else{
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                                    startActivity(intent);
                                }
                            }
                        }
                    });

                    client_email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                            emailIntent.setData(Uri.parse(client_email.getText().toString()));

                            startActivity(Intent.createChooser(emailIntent, "Send Email"));
                        }
                    });

                    String name = client.getName();
                    String email = client.getEmail();
                    String phone = client.getPhone();
                    String address = client.getAddress();
                    String manager = client.getManager();

                    String emptyValue = getString(R.string.card_empty_value);

                    if (email.isEmpty()) {
                        email = emptyValue;
                    }

                    if (phone.isEmpty()) {
                        phone = emptyValue;
                    }
                    if (address.isEmpty()) {
                        address = emptyValue;
                    }
                    if (manager.isEmpty()) {
                        manager = emptyValue;
                    }

                    client_name.setText(name);
                    client_email.setText(email);
                    client_phone.setText(phone);
                    client_address.setText(address);
                    client_manager.setText(manager);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(viewCard);

                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        mRecyclerViewClients.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerViewClients.setAdapter(mClientsListAdapter);

        fillRecyclerView();

        return view;
    }

    class GetClientsTak extends AsyncTask<Void, Void, String> implements Constants {

        @Override
        protected String doInBackground(Void... voids) {
            return WebService1C.sendRequest(SERVER_SOAP_METHOD_GET_CLIENTS, getContext());
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (!result.isEmpty()) {
                    JSONObject jsonObject = null;
                    Boolean executeResult = false;

                    try {
                        jsonObject = new JSONObject(result);
                    } catch (JSONException e) {
                        DialogAlerts.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                    }finally {
                        progressDialog.dismiss();
                    }

                    if (jsonObject != null) {
                        try {
                            executeResult = jsonObject.getBoolean("result");
                        } catch (JSONException e) {
                            DialogAlerts.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                        }finally {
                            progressDialog.dismiss();
                        }

                        if (executeResult == true) {
                            try {
                                String stringData = jsonObject.getString("data");
                                JSONObject jsonData = new JSONObject(stringData);

                                // Get data from 1C
                                ArrayList<ContentValues> arrayListClients = JsonParser1C.getClients(jsonData.toString());

                                // Init database helper
                                databaseHelper = new RealtyDataBase.DataBaseHelper(getContext());
                                database = databaseHelper.getWritableDatabase();

                                // Fill database
                                try {
                                    // Fill table
                                    for (ContentValues mValue : arrayListClients) {
                                        String id1C = mValue.get("id1C").toString();
                                        String[] args = new String[] {id1C};

                                        database.delete(RealtyDataBase.DataBaseEntry.CLIENTS_TABLE_NAME, RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_ID1C + " = ?", new String[] { id1C });
                                        database.insert(RealtyDataBase.DataBaseEntry.CLIENTS_TABLE_NAME, null, mValue);
                                    }

                                    // Set last update data
                                    Date currentDate = new Date();
                                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

                                    preferences.edit().putString("clientsList_update_data", dateFormat.format(currentDate)).apply();

                                    txt_clientsList_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + dateFormat.format(currentDate));
                                } finally {
                                    // Close DB
                                    database.close();

                                    // Fill RW
                                    fillRecyclerView();

                                    // Dismiss progress bar
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                DialogAlerts.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                            }finally {
                                progressDialog.dismiss();
                            }
                        } else {
                            try {
                                String executeMessage = jsonObject.getString("message");

                                DialogAlerts.showAlertDialog(getContext(), executeMessage, getContext().getString(R.string.error));
                            } catch (JSONException e) {
                                DialogAlerts.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                            }finally {
                                progressDialog.dismiss();
                            }
                        }
                    } else {
                        DialogAlerts.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));

                        progressDialog.dismiss();
                    }
                }else{
                    DialogAlerts.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));

                    progressDialog.dismiss();
                }
            }else{
                DialogAlerts.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));

                progressDialog.dismiss();
            }
        }

    }

    private void downloadDataToDB(){
        GetClientsTak getClientsTak = new GetClientsTak();
        getClientsTak.execute();
    }

    private void fillRecyclerView(){
        fillRecyclerViewData(null);
    }

    private void fillRecyclerView(String filter){
        fillRecyclerViewData(filter);
    }

    private void fillRecyclerViewData(String filter){
        Cursor cursor = null;
        String[] params = null;
        Boolean searchUsing = false;

        if (filter != null){
            searchUsing = true;
        }

        // Clear array list
        clients.clear();

        // Fill array list data from DB
        if (databaseHelper == null){
            databaseHelper = new RealtyDataBase.DataBaseHelper(getContext());
        }

        database = databaseHelper.getReadableDatabase();

        // First selection by name
        cursor = database.rawQuery(RealtyDataBase.DataBaseEntry.CLIENTS_QUERY, null);

        if (filter != null) {
            if (!filter.isEmpty()) {
                params = new String[2];
                params[0] = "%" + filter + "%";
                params[1] = "%" + filter + "%";

                cursor = database.rawQuery(RealtyDataBase.DataBaseEntry.CLIENTS_QUERY_FILTER, params);
            }
        }

        while (cursor.moveToNext()){
            addClientToArrayList(cursor);
        }

        cursor.close();
        database.close();

        // Fill recycler view
        mClientsListAdapter.clients = clients;
        mClientsListAdapter.notifyDataSetChanged();

        mSwipeRefresh.setRefreshing(false);
        progressDialog.dismiss();

        // Show last data update
        if (preferences.contains("clientsList_update_data")){
            txt_clientsList_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("clientsList_update_data", ""));
        }

        checkEmptyState(searchUsing);
    }

    private void addClientToArrayList(Cursor cursor){
        String name = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_NAME));
        String phone = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_PHONE));
        String email = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_EMAIL));
        String address = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_ADDRESS));
        String manager = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_MANAGER));
        String id = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry._ID));
        String id1C = cursor.getString(cursor.getColumnIndex(RealtyDataBase.DataBaseEntry.CLIENTS_COLUMN_ID1C));

        clients.add(new Client(name, phone, email, address, manager, id, id1C));
    }

    private void checkEmptyState(Boolean searchUsing) {
        if (clients.size() == 0) {
            clientslist_constraintLayout.setBackgroundColor(getContext().getColor(R.color.colorWhite));

            clientslist_txt_empty.setText(R.string.clientlist_empty);
            clientslist_img_empty.setVisibility(View.VISIBLE);
            clientslist_txt_empty.setVisibility(View.VISIBLE);
            clientslist_btn_update.setVisibility(View.VISIBLE);
            txt_clientsList_update_data.setVisibility(View.INVISIBLE);

            mRecyclerViewClients.setVisibility(View.INVISIBLE);

            if (searchUsing) {
                clientslist_txt_empty.setText(R.string.empty_search);
                clientslist_btn_update.setVisibility(View.INVISIBLE);
            }
        }else {
            clientslist_constraintLayout.setBackgroundColor(getContext().getColor(R.color.background));

            clientslist_img_empty.setVisibility(View.INVISIBLE);
            clientslist_txt_empty.setVisibility(View.INVISIBLE);
            clientslist_btn_update.setVisibility(View.INVISIBLE);
            txt_clientsList_update_data.setVisibility(View.VISIBLE);

            mRecyclerViewClients.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu_list, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.clients_list_search));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                checkEmptyState(false);

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    fillRecyclerView(newText);
                }else{
                    fillRecyclerView();
                }

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
