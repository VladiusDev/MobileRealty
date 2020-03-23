package com.monolit.mobilerealty.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.monolit.mobilerealty.Activities.ReservationActivity;
import com.monolit.mobilerealty.Adapters.ObjectListAdapter;
import com.monolit.mobilerealty.FormattingUtils.NumberFormat;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.JsonUtils.JsonParser1C;
import com.monolit.mobilerealty.Room.ViewModel.RealtyObjectViewModel;
import com.monolit.mobilerealty.RealtorObjects.RealtyObject;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.RealtorObjects.Utils.RealtorObjectsUtils;
import com.monolit.mobilerealty.WebServiceUtils.WebService1C;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentObjects extends Fragment {
    private RecyclerView mRecyclerViewObjects;
    private ObjectListAdapter mRecyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshObjects;
    private SharedPreferences preferences;
    private TextView txt_objectList_update_data;
    private View view;
    private View viewCard;
    private AlertDialog alertDialog;
    private TextView tv_status;
    private ProgressDialog progressDialog;
    private TextView tv_title;
    private TextView tv_square;
    private TextView tv_rooms;
    private TextView tv_section;
    private TextView tv_floor;
    private TextView tv_price;
    private TextView tv_amount;
    private TextView tv_reservation;
    private ConstraintLayout objectslist_constraintlayout;
    private ImageView objectlist_img_empty;
    private TextView objectlist_txt_empty;
    private Button objectlist_btn_update;
    private TextView tv_get_plan;
    private RealtyObjectViewModel realtyObjectViewModel;
    private List<RealtyObject> objects;

    public FragmentObjects() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_objects, container, false);

        realtyObjectViewModel = ViewModelProviders.of(this).get(RealtyObjectViewModel.class);
        preferences   = PreferenceManager.getDefaultSharedPreferences(getContext());

        objects = realtyObjectViewModel.getRealtyObjects();

        objectlist_img_empty = view.findViewById(R.id.objectlist_img_empty);
        objectlist_txt_empty = view.findViewById(R.id.objectlist_txt_empty);
        objectlist_btn_update = view.findViewById(R.id.objectlist_btn_update);
        txt_objectList_update_data = view.findViewById(R.id.txt_objectList_update_data);
        objectslist_constraintlayout = view.findViewById(R.id.objectslist_constraintlayout);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        swipeRefreshObjects = view.findViewById(R.id.swipe_container_object_list);
        swipeRefreshObjects.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshObjects.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadDataToDB();
            }
        });

        objectlist_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getContext().getString(R.string.object_list_download));
                progressDialog.show();

                downloadDataToDB();
            }
        });

        mRecyclerViewObjects = view.findViewById(R.id.rw_realty_objects);
        mRecyclerViewObjects.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerViewAdapter = new ObjectListAdapter();
        mRecyclerViewAdapter.setOnClickObjectListener(new ObjectListAdapter.OnClickObjectListener() {
            @Override
            public void onClickObject(int position) {
                List<RealtyObject> objects = mRecyclerViewAdapter.getObjectList();

                final RealtyObject realtyObject = objects.get(position);

                if (realtyObject != null) {
                    viewCard = LayoutInflater.from(getContext()).inflate(R.layout.activity_object_card, null);

                    tv_title = viewCard.findViewById(R.id.object_card_txt_title);
                    tv_status = viewCard.findViewById(R.id.object_card_txt_status);
                    tv_square = viewCard.findViewById(R.id.object_card_txt_square);
                    tv_rooms = viewCard.findViewById(R.id.object_card_txt_rooms);
                    tv_section = viewCard.findViewById(R.id.object_card_txt_section);
                    tv_floor = viewCard.findViewById(R.id.object_card_txt_floor);
                    tv_price = viewCard.findViewById(R.id.object_card_txt_price);
                    tv_amount = viewCard.findViewById(R.id.object_card_txt_amount);
                    tv_reservation = viewCard.findViewById(R.id.object_card_txt_reservation);
                    tv_get_plan = viewCard.findViewById(R.id.object_card_txt_get_plan);

                    Button btn_close = viewCard.findViewById(R.id.object_card_btn_close);
                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    tv_reservation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), ReservationActivity.class);
                            intent.putExtra("objectId", realtyObject.getId1c());
                            intent.putExtra("object", realtyObject.getName());

                            startActivity(intent);
                        }
                    });

                    tv_get_plan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RealtorObjectsUtils.openObjectPlan(realtyObject, getContext());
                        }
                    });

                    String priceString = NumberFormat.convertToSumFormat(realtyObject.getPrice()) + " " + getContext().getString(R.string.square_meter);
                    String amountString = NumberFormat.convertToSumFormat(realtyObject.getAmount());

                    tv_title.setText(realtyObject.getName());
                    tv_status.setText(realtyObject.getStatus());
                    tv_square.setText(realtyObject.getSquare().toString() + " " + getContext().getString(R.string.square_meter));
                    tv_rooms.setText(Integer.toString(realtyObject.getRooms()));
                    tv_section.setText(Integer.toString(realtyObject.getSection()));
                    tv_floor.setText(Integer.toString(realtyObject.getFloor()));
                    tv_price.setText(priceString);
                    tv_amount.setText(amountString);

                    updateStatusColor(realtyObject.getStatusId());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(viewCard);

                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        mRecyclerViewObjects.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setObjectList(objects);

        adapterNotifyDataSetChanged();

        checkEmptyState();

        if (preferences.contains("objectList_update_data")){
            txt_objectList_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("objectList_update_data", ""));
        }

        return view;
    }

    class GetObjectsTask extends AsyncTask<Void, Void, String> implements Constants {

        @Override
        protected String doInBackground(Void... voids) {
            return WebService1C.sendRequest(SERVER_SOAP_METHOD_GET_OBJECTS, getContext());
        }

        @Override
        protected void onPostExecute(String result) {
            // Get data from 1C
            ArrayList<RealtyObject> newObjects = JsonParser1C.getFreeObjects(result);

            // Fill database
            try {
                // Insert objects
                realtyObjectViewModel.deleteAll();
                realtyObjectViewModel.insertAllRealtyObjects(newObjects);

                objects.clear();
                objects.addAll(newObjects);
            } finally {
                swipeRefreshObjects.setRefreshing(false);
                progressDialog.dismiss();

                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

                preferences.edit().putString("objectList_update_data", dateFormat.format(currentDate)).apply();

                if (preferences.contains("objectList_update_data")){
                    txt_objectList_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("objectList_update_data", ""));
                }

                mRecyclerViewAdapter.setObjectList(objects);
                adapterNotifyDataSetChanged();

                checkEmptyState();
            }
        }
    }

    private void downloadDataToDB(){
        GetObjectsTask getObjectsTask = new GetObjectsTask();
        getObjectsTask.execute();
    }

    private void updateStatusColor(int statusId){
        switch (statusId){
            case 1:
                tv_status.setBackground(getContext().getDrawable(R.drawable.object_status_reservation));
                break;
            case 2:
                tv_status.setBackground(getContext().getDrawable(R.drawable.object_status_pledged));
                break;
            case 3:
                tv_status.setBackground(getContext().getDrawable(R.drawable.object_status_refusing));
                break;
        }
    }

    private void checkEmptyState(){
        if (objects.size() == 0) {
            objectslist_constraintlayout.setBackgroundColor(getContext().getColor(R.color.colorWhite));

            objectlist_txt_empty.setText(R.string.objectlist_empty);
            objectlist_img_empty.setVisibility(View.VISIBLE);
            objectlist_txt_empty.setVisibility(View.VISIBLE);
            objectlist_btn_update.setVisibility(View.VISIBLE);
            txt_objectList_update_data.setVisibility(View.INVISIBLE);

            mRecyclerViewObjects.setVisibility(View.INVISIBLE);
        }else{
            objectslist_constraintlayout.setBackgroundColor(getContext().getColor(R.color.background));

            objectlist_img_empty.setVisibility(View.INVISIBLE);
            objectlist_txt_empty.setVisibility(View.INVISIBLE);
            objectlist_btn_update.setVisibility(View.INVISIBLE);
            txt_objectList_update_data.setVisibility(View.VISIBLE);

            mRecyclerViewObjects.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu_list, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.object_list_search));
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
                    mRecyclerViewAdapter.setObjectList(realtyObjectViewModel.getRealtyObjectsByFilter(newText));

                    adapterNotifyDataSetChanged();
                }else{
                    mRecyclerViewAdapter.setObjectList(realtyObjectViewModel.getRealtyObjects());

                    adapterNotifyDataSetChanged();
                }

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    void adapterNotifyDataSetChanged(){
        mRecyclerViewObjects.post(new Runnable()
        {
            @Override
            public void run() {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

}
