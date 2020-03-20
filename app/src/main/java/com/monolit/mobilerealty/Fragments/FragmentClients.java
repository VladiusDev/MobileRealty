package com.monolit.mobilerealty.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.lifecycle.ViewModelProviders;
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
import com.monolit.mobilerealty.Room.ViewModel.ClientsViewModel;
import com.monolit.mobilerealty.AlertDialog.DialogFactory;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.JsonUtils.JsonParser1C;
import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.WebServiceUtils.WebService1C;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FragmentClients extends Fragment{

    private static final int LIMIT_COUNT = 50;

    private RecyclerView mRecyclerViewClients;
    private SwipeRefreshLayout mSwipeRefresh;
    private ClientsListAdapter mClientsListAdapter;
    private ProgressDialog progressDialog;
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
    private int rowCount;
    private ClientsViewModel clientsViewModel;
    private List<Client> clients;

    public FragmentClients() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        setRetainInstance(false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getContext().getString(R.string.clientslist_download));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_clients, container, false);

        clientsViewModel = ViewModelProviders.of(this).get(ClientsViewModel.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        rowCount = 0;

        txt_clientsList_update_data = view.findViewById(R.id.txt_clientsList_update_data);
        clientslist_constraintLayout = view.findViewById(R.id.clientslist_constraintLayout);
        clientslist_img_empty = view.findViewById(R.id.clientslist_img_empty);
        clientslist_txt_empty = view.findViewById(R.id.clientslist_txt_empty);
        clientslist_btn_update = view.findViewById(R.id.clientslist_btn_update);
        clientslist_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadDataToDB(false);
            }
        });

        mRecyclerViewClients = view.findViewById(R.id.rw_clientList);
        mSwipeRefresh = view.findViewById(R.id.swipe_container_client_list);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadDataToDB(true);
            }
        });

        mClientsListAdapter  = new ClientsListAdapter();
        mClientsListAdapter.setOnReachEndListener(new ClientsListAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                rowCount = rowCount + 100;

                List<Client> oldClients = mClientsListAdapter.getClients();
                List<Client> newClients = clientsViewModel.getAllClients(rowCount);

                oldClients.addAll(newClients);

                mClientsListAdapter.setClients(oldClients);

                adapterNotifyDataSetChanged();
            }
        });
        mClientsListAdapter.setOnClickClientListener(new ClientsListAdapter.OnClickClientListener() {
            @Override
            public void OnClickClient(int position) {
                Client client = mClientsListAdapter.getClients().get(position);

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
                                        DialogFactory.showDialogAccessToGallery(getActivity());
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

        clients = clientsViewModel.getClients();

        mClientsListAdapter.setClients(clients);

        adapterNotifyDataSetChanged();

        checkEmptyState();

        if (preferences.contains("clientsList_update_data")){
            txt_clientsList_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("clientsList_update_data", ""));
        }

        return view;
    }

    class InsertDataToDB extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            mSwipeRefresh.setRefreshing(false);
            progressDialog.dismiss();

            checkEmptyState();

            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

            preferences.edit().putString("clientsList_update_data", dateFormat.format(currentDate)).apply();

            if (preferences.contains("clientsList_update_data")){
                txt_clientsList_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("clientsList_update_data", ""));
            }

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (strings[0] != null) {
                if (!strings[0].isEmpty()) {
                    JSONObject jsonObject = null;
                    Boolean executeResult = false;

                    try {
                        jsonObject = new JSONObject(strings[0] );
                    } catch (JSONException e) {
                    }

                    if (jsonObject != null) {
                        try {
                            executeResult = jsonObject.getBoolean("result");
                        } catch (JSONException e) {
                        }

                        if (executeResult == true) {
                            String stringData = null;

                            try {
                                stringData = jsonObject.getString("data");
                            } catch (JSONException e) {
                                return null;
                            }

                            JSONObject jsonData = null;
                            try {
                                jsonData = new JSONObject(stringData);
                            } catch (JSONException e) {
                                return null;
                            }

                            // Get data from 1C
                            List<Client> newClients = JsonParser1C.getClients(jsonData.toString());

                            // Fill database
                            clientsViewModel.insertAllClients(newClients);

                            clients.addAll(newClients);

                            mClientsListAdapter.setClients(clients);
                            adapterNotifyDataSetChanged();
                        }
                    }
                }
            }

            return null;
        }
    }

    class GetClientsTak extends AsyncTask<Void, Void, String> implements Constants {

        String fullDownload;
        Boolean itsSwipe;

        public GetClientsTak(String fullDownload, Boolean itsSwipe) {
            this.fullDownload = fullDownload;
            this.itsSwipe = itsSwipe;
        }

        @Override
        protected void onPreExecute() {
            if (itsSwipe == false) {
                progressDialog.show();
            }

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> args = new HashMap<>();
            args.put("fullDownload", fullDownload);

            return WebService1C.sendRequest(SERVER_SOAP_METHOD_GET_CLIENTS, getContext(), args);
        }

        @Override
        protected void onPostExecute(String result) {
            InsertDataToDB insertDataToDB = new InsertDataToDB();
            insertDataToDB.execute(result);
        }
    }

    private void downloadDataToDB(Boolean itsSwipe){
        String fullDownload = "0";
        if (mClientsListAdapter.getItemCount() == 0){
            fullDownload = "1";
        }

        GetClientsTak getClientsTak = new GetClientsTak(fullDownload, itsSwipe);
        getClientsTak.execute();
    }

    private void checkEmptyState() {
        if (clients.size() == 0) {
            clientslist_constraintLayout.setBackgroundColor(getContext().getColor(R.color.colorWhite));

            clientslist_txt_empty.setText(R.string.clientlist_empty);
            clientslist_img_empty.setVisibility(View.VISIBLE);
            clientslist_txt_empty.setVisibility(View.VISIBLE);
            clientslist_btn_update.setVisibility(View.VISIBLE);
            txt_clientsList_update_data.setVisibility(View.INVISIBLE);

            mRecyclerViewClients.setVisibility(View.INVISIBLE);
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
                checkEmptyState();

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
                    mClientsListAdapter.setClients(clientsViewModel.getClientsByFilter(newText));

                    adapterNotifyDataSetChanged();
                }else{
                    mClientsListAdapter.setClients(clientsViewModel.getAllClients(0));

                    adapterNotifyDataSetChanged();

                    rowCount = 0;
                }

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    void adapterNotifyDataSetChanged(){
        mRecyclerViewClients.post(new Runnable()
        {
            @Override
            public void run() {
                mClientsListAdapter.notifyDataSetChanged();
            }
        });
    }

}
