package com.monolit.mobilerealty.Fragments;


import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.monolit.mobilerealty.Adapters.TasksListAdapter;
import com.monolit.mobilerealty.AlertDialog.DialogFactory;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.JsonUtils.JsonParser1C;
import com.monolit.mobilerealty.RealtorObjects.Task;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.Room.ViewModel.TasksViewModel;
import com.monolit.mobilerealty.WebServiceUtils.WebService1C;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FragmentTasks extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TasksListAdapter mTasksListAdapter;
    private SharedPreferences preferences;
    private TextView txt_tasks_update_data;
    private AlertDialog alertDialog;
    private TextView txt_task_card_deadline;
    private TextView txt_task_card_date;
    private TextView txt_task_card_author;
    private TextView txt_task_card_title;
    private TextView txt_task_card_description;
    private ImageView img_task_card_deadline;
    private Button btn_task_card_cancel;
    private Button btn_task_card_confirm;
    private EditText edt_task_card_result;
    private View viewCard;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private ConstraintLayout tasklist_constraintlayout;
    private ImageView taskslist_img_empty;
    private TextView taskslist_txt_empty;
    private Button taskslist_btn_update;
    private TasksViewModel tasksViewModel;
    private List<Task> tasks;

    public FragmentTasks() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        taskslist_img_empty = view.findViewById(R.id.taskslist_img_empty);
        taskslist_txt_empty = view.findViewById(R.id.taskslist_txt_empty);
        taskslist_btn_update = view.findViewById(R.id.taskslist_btn_update);
        txt_tasks_update_data = view.findViewById(R.id.txt_tasks_update_data);
        tasklist_constraintlayout = view.findViewById(R.id.tasklist_constraintlayout);

        tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        tasks = tasksViewModel.getTasks();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.task_card_execute_task));
        progressDialog.setCancelable(false);

        taskslist_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               progressDialog.setMessage(getContext().getString(R.string.tasks_list_download));
               progressDialog.show();

               downloadDataToDB();
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container_tasks);
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

        mTasksListAdapter = new TasksListAdapter();
        mTasksListAdapter.setOnNoteClickListener(new TasksListAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                final Task task = tasks.get(position);
                String deadlineDateString = task.getDeadline();

                // Init alert dialog and view
                builder = new AlertDialog.Builder(getContext());
                viewCard = LayoutInflater.from(getContext()).inflate(R.layout.activity_task_card, null);

                // Init views
                txt_task_card_deadline = viewCard.findViewById(R.id.task_card_txt_deadline);
                txt_task_card_date = viewCard.findViewById(R.id.task_card_txt_date);
                txt_task_card_author = viewCard.findViewById(R.id.task_card_txt_author);
                txt_task_card_title = viewCard.findViewById(R.id.task_card_txt_title);
                txt_task_card_description = viewCard.findViewById(R.id.task_card_txt_description);

                img_task_card_deadline = viewCard.findViewById(R.id.task_card_img_deadline);

                btn_task_card_cancel = viewCard.findViewById(R.id.task_card_btn_cancel);
                btn_task_card_confirm = viewCard.findViewById(R.id.task_card_btn_confirm);

                edt_task_card_result = viewCard.findViewById(R.id.task_card_edt_result);

                // Set listeners
                btn_task_card_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btn_task_card_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        executeUserTask(task.getId1c(), edt_task_card_result.getText().toString());
                    }
                });

                // Set task values
                txt_task_card_deadline.setText(deadlineDateString);
                txt_task_card_date.setText(task.getDate());
                txt_task_card_author.setText(task.getAuthor());
                txt_task_card_title.setText(task.getTitle());
                txt_task_card_description.setText(task.getDescription());

                // Deadline
                if (!deadlineDateString.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    try {
                        Date deadlineDate = dateFormat.parse(deadlineDateString);
                        long timedeff = System.currentTimeMillis() - deadlineDate.getTime();
                        if (timedeff > 0) {
                            txt_task_card_deadline.setTextColor(viewCard.getContext().getColor(R.color.colorWhite));
                            txt_task_card_deadline.setBackground(viewCard.getContext().getDrawable(R.drawable.deadline_shape_text));
                        } else if (timedeff == 0) {

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    img_task_card_deadline.setVisibility(View.INVISIBLE);
                }

                // Show dialog
                builder.setView(viewCard);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        mRecyclerView = view.findViewById(R.id.rw_tasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mTasksListAdapter);

        mTasksListAdapter.setTaskArrayList(tasks);

        if (preferences.contains("taskList_update_data")){
            txt_tasks_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("taskList_update_data", ""));
        }

        checkEmptyState();

        return view;
    }

    class GetTasksTask extends AsyncTask<Void, Void, String> implements Constants {

        @Override
        protected String doInBackground(Void... voids) {
            return WebService1C.sendRequest(SERVER_SOAP_METHOD_GET_TASKS, getContext());
        }

        @Override
        protected void onPostExecute(String result) {
            new InsertTasksTask().execute(result);
        }

    }

    class InsertTasksTask extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            mSwipeRefreshLayout.setRefreshing(false);
            progressDialog.dismiss();

            if (preferences.contains("taskList_update_data")){
                txt_tasks_update_data.setText(getString(R.string.objectList_lastUpdate) + " " + preferences.getString("taskList_update_data", ""));
            }

            checkEmptyState();
        }

        @Override
        protected Void doInBackground(String... strings) {
            // Get data from 1C
            List<Task> newTasks = JsonParser1C.getTasks(strings[0]);

            // Fill database
            tasksViewModel.deleteAll();
            tasksViewModel.insertTasks(newTasks);

            tasks = newTasks;

            // Set last update data
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("YYYY:MM:dd hh:mm:ss", Locale.getDefault());

            preferences.edit().putString("taskList_update_data", dateFormat.format(currentDate)).apply();

            mTasksListAdapter.setTaskArrayList(tasks);
            adapterNotifyDataSetChanged();

            return null;
        }
    }

    class ExecuteTask extends AsyncTask<HashMap<String, String>, Void, String> implements Constants {

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            return WebService1C.sendRequest(SERVER_SOAP_METHOD_EXECUTE_TASK, getContext(), params);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

                if (!result.isEmpty()) {
                    JSONObject jsonObject = null;
                    Boolean executeResult = false;

                    try {
                        jsonObject = new JSONObject(result);
                    } catch (JSONException e) {
                        DialogFactory.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                    }

                    if (jsonObject != null) {
                        try {
                            executeResult = jsonObject.getBoolean("result");
                        } catch (JSONException e) {
                            DialogFactory.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                        }

                        if (executeResult == true) {
                            alertDialog.dismiss();
                            downloadDataToDB();
                        } else {
                            try {
                                String executeMessage = jsonObject.getString("message");

                                DialogFactory.showAlertDialog(getContext(), executeMessage, getContext().getString(R.string.error));
                            } catch (JSONException e) {
                                DialogFactory.showAlertDialog(getContext(), e.toString(), getContext().getString(R.string.error));
                            }
                        }
                    } else {
                        DialogFactory.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));
                    }
                }else{
                    DialogFactory.showAlertDialog(getContext(), getContext().getString(R.string.error_work_server), getContext().getString(R.string.error));
                }
        }
    }

    private void downloadDataToDB(){
        GetTasksTask getTasksTask = new GetTasksTask();
        getTasksTask.execute();
    }

    private void executeUserTask(String uid, String result){
        HashMap<String, String> args = new HashMap<>();
        args.put("uid", uid);
        args.put("result", result);

        ExecuteTask executeTask = new ExecuteTask();
        executeTask.execute(args);
    }

    private void checkEmptyState() {
        if (tasks.size() == 0) {
            tasklist_constraintlayout.setBackgroundColor(getContext().getColor(R.color.colorWhite));

            taskslist_txt_empty.setText(R.string.tasklist_empty);
            taskslist_img_empty.setVisibility(View.VISIBLE);
            taskslist_txt_empty.setVisibility(View.VISIBLE);
            taskslist_btn_update.setVisibility(View.VISIBLE);
            txt_tasks_update_data.setVisibility(View.INVISIBLE);

            mRecyclerView.setVisibility(View.INVISIBLE);
        }else {
            tasklist_constraintlayout.setBackgroundColor(getContext().getColor(R.color.background));

            taskslist_img_empty.setVisibility(View.INVISIBLE);
            taskslist_txt_empty.setVisibility(View.INVISIBLE);
            taskslist_btn_update.setVisibility(View.INVISIBLE);
            txt_tasks_update_data.setVisibility(View.VISIBLE);

            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu_list, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString((R.string.tasks_list_search)));
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
                    mTasksListAdapter.setTaskArrayList(tasksViewModel.getTasksByFilter(newText));

                    adapterNotifyDataSetChanged();

                    checkEmptyState();
                }else{
                    mTasksListAdapter.setTaskArrayList(tasksViewModel.getAllTasks());

                    adapterNotifyDataSetChanged();

                    checkEmptyState();
                }

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    void adapterNotifyDataSetChanged(){
        mRecyclerView.post(new Runnable()
        {
            @Override
            public void run() {
                mTasksListAdapter.notifyDataSetChanged();
            }
        });
    }
}
