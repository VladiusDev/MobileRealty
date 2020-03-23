package com.monolit.mobilerealty.Fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import com.monolit.mobilerealty.Adapters.ReservationListAdapter;
import com.monolit.mobilerealty.DataBase.RealtyDataBase;
import com.monolit.mobilerealty.AlertDialog.DialogFactory;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.JsonUtils.JsonParser1C;
import com.monolit.mobilerealty.RealtorObjects.Reservation;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.Room.ViewModel.ReservationsViewModel;
import com.monolit.mobilerealty.WebServiceUtils.WebService1C;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentReservation extends Fragment {

    private RecyclerView mRecyclerViewReservations;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ReservationListAdapter mReservationListAdapter;
    private List<Reservation> reservations;
    private ConstraintLayout reservationlist_constraintLayout;
    private ImageView reservationlist_img_empty;
    private TextView reservationlist_txt_empty;
    private Button reservationlist_btn_update;
    private TextView txt_reservationlist_update_data;
    private RealtyDataBase.DataBaseHelper databaseHelper;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private ReservationsViewModel reservationsViewModel;

    public FragmentReservation() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        reservationsViewModel = ViewModelProviders.of(this).get(ReservationsViewModel.class);

        reservations = reservationsViewModel.getReservations();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        reservationlist_constraintLayout = view.findViewById(R.id.reservationlist_constraintLayout);
        reservationlist_img_empty = view.findViewById(R.id.reservationlist_img_empty);
        reservationlist_txt_empty = view.findViewById(R.id.reservationlist_txt_empty);
        reservationlist_btn_update = view.findViewById(R.id.reservationlist_btn_update);
        txt_reservationlist_update_data = view.findViewById(R.id.txt_reservationList_update_data);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container_reservation_list);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadDataToDB();
            }
        });

        reservationlist_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getContext().getString(R.string.reservationslist_download));
                progressDialog.show();

                downloadDataToDB();
            }
        });

        mReservationListAdapter = new ReservationListAdapter();
        mReservationListAdapter.setReservations(reservations);

        mRecyclerViewReservations = view.findViewById(R.id.rw_reservationList);
        mRecyclerViewReservations.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewReservations.setAdapter(mReservationListAdapter);

        checkEmptyState();

        return view;
    }

    class GetReservationsTask extends AsyncTask<Void, Void, String> implements Constants {

        @Override
        protected String doInBackground(Void... voids) {
            return WebService1C.sendRequest(SERVER_SOAP_METHOD_GET_RESERVATIONS, getContext());
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
                        DialogFactory.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                    }finally {
                        progressDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    if (jsonObject != null) {
                        try {
                            executeResult = jsonObject.getBoolean("result");
                        } catch (JSONException e) {
                            DialogFactory.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                        }finally {
                            progressDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        if (executeResult == true) {
                            new InsertDataToDB().execute(jsonObject);
                        } else {
                            try {
                                String executeMessage = jsonObject.getString("message");

                                DialogFactory.showAlertDialog(getContext(), executeMessage, getContext().getString(R.string.error));
                            } catch (JSONException e) {
                                DialogFactory.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                            }finally {
                                progressDialog.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    } else {
                        DialogFactory.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));

                        progressDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }else{
                    DialogFactory.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));

                    progressDialog.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }else{
                DialogFactory.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));

                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }

    class InsertDataToDB extends AsyncTask<JSONObject, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);

            if (preferences.contains("reservationsList_update_data")){
                txt_reservationlist_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("reservationsList_update_data", ""));
            }

            checkEmptyState();
        }

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            String stringData;
            JSONObject jsonData;

            try {
                stringData = jsonObjects[0].getString("data");
            } catch (JSONException e) {
                return null;
            }

            try {
                jsonData = new JSONObject(stringData);
            } catch (JSONException e) {
                return null;
            }

            reservations = JsonParser1C.getReservations(jsonData.toString());

            reservationsViewModel.deleteAll();
            reservationsViewModel.insertReservations(reservations);

            mReservationListAdapter.setReservations(reservations);

            adapterNotifyDataSetChanged();

            // Set last update data
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

            preferences.edit().putString("reservationsList_update_data", dateFormat.format(currentDate)).apply();

            return null;
        }
    }

    private void downloadDataToDB(){
        GetReservationsTask getReservationsTask = new GetReservationsTask();
        getReservationsTask.execute();
    }

    private void checkEmptyState() {
        if (reservations.size() == 0) {
            reservationlist_constraintLayout.setBackgroundColor(getContext().getColor(R.color.colorWhite));

            reservationlist_txt_empty.setText(R.string.reservationlist_empty);
            reservationlist_img_empty.setVisibility(View.VISIBLE);
            reservationlist_txt_empty.setVisibility(View.VISIBLE);
            reservationlist_btn_update.setVisibility(View.VISIBLE);
            txt_reservationlist_update_data.setVisibility(View.INVISIBLE);

            mRecyclerViewReservations.setVisibility(View.INVISIBLE);
        }else {
            reservationlist_constraintLayout.setBackgroundColor(getContext().getColor(R.color.background));

            reservationlist_img_empty.setVisibility(View.INVISIBLE);
            reservationlist_txt_empty.setVisibility(View.INVISIBLE);
            reservationlist_btn_update.setVisibility(View.INVISIBLE);
            txt_reservationlist_update_data.setVisibility(View.VISIBLE);

            mRecyclerViewReservations.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu_list, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.reservations_list_search));
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
                    mReservationListAdapter.setReservations(reservationsViewModel.getReservationsByFilter(newText));

                    adapterNotifyDataSetChanged();

                    checkEmptyState();
                }else{
                    mReservationListAdapter.setReservations(reservationsViewModel.getAllReservations());

                    adapterNotifyDataSetChanged();

                    checkEmptyState();
                }

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    void adapterNotifyDataSetChanged(){
        mRecyclerViewReservations.post(new Runnable()
        {
            @Override
            public void run() {
                mReservationListAdapter.notifyDataSetChanged();
            }
        });
    }
}
