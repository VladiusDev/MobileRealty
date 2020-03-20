package com.monolit.mobilerealty.RealtorObjects.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.monolit.mobilerealty.Activities.PlanActivity;
import com.monolit.mobilerealty.AlertDialog.DialogFactory;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.RealtorObjects.RealtyObject;
import com.monolit.mobilerealty.WebServiceUtils.WebService1C;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RealtorObjectsUtils {

    public static void openObjectPlan(RealtyObject realtyObject, Context context){
        OpenObjectPlanTask getObjectPlanTask = new OpenObjectPlanTask(realtyObject, context);
        getObjectPlanTask.execute();
    }

    public static void objectReservation(String objectId, String clientId, Context context) throws JSONException {
        ObjectReservationTask objectReservationTask = new ObjectReservationTask(objectId, clientId, context);
        objectReservationTask.execute();

    }

    static class OpenObjectPlanTask extends AsyncTask<Void, Void, String> implements Constants {

        private ProgressDialog progressDialog;
        private Context context;
        private RealtyObject realtyObject;

        public OpenObjectPlanTask(RealtyObject realtyObject, Context context) {
            this.context = context;
            this.realtyObject = realtyObject;

            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(context.getString(R.string.object_card_plan_request_send));
        }


        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> args = new HashMap<>();
            args.put("uid", realtyObject.getId1c());

            return WebService1C.sendRequest(Constants.SERVER_SOAP_METHOD_GET_OBJECT_PLAN, context, args);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            if (result != null) {
                if (!result.isEmpty()) {
                    JSONObject jsonObject = null;
                    Boolean executeResult = false;

                    try {
                        jsonObject = new JSONObject(result);
                    } catch (JSONException e) {
                        DialogFactory.showAlertDialog(context, e.toString(), context.getString(R.string.error));
                    }

                    if (jsonObject != null) {
                        try {
                            executeResult = jsonObject.getBoolean("result");
                        } catch (JSONException e) {
                            DialogFactory.showAlertDialog(context, e.toString(), context.getString(R.string.error));
                        }

                        if (executeResult == true) {
                            try {
                                String stringData = jsonObject.getString("data");
                                JSONObject jsonData = new JSONObject(stringData);
                                JSONArray jsonRealtyData = jsonData.getJSONArray("RealtyData");

                                String[] urlArray = new String[jsonRealtyData.length()];

                                for (int i = 0, fileIndex = 1; i < jsonRealtyData.length(); i++, fileIndex++){
                                    JSONObject fileObject = jsonRealtyData.getJSONObject(i);
                                    String fileAddress = fileObject.getString("file"+fileIndex);

                                    urlArray[i] = fileAddress;
                                }

                                Intent intent = new Intent(context, PlanActivity.class);
                                intent.putExtra("urlArray", urlArray);
                                intent.putExtra("objectName", realtyObject.getName());

                                context.startActivity(intent);
                            } catch (JSONException e) {
                                DialogFactory.showAlertDialog(context, e.toString(), context.getString(R.string.error));
                            }

                        } else {
                            try {
                                String executeMessage = jsonObject.getString("message");

                                DialogFactory.showAlertDialog(context, executeMessage, context.getString(R.string.error));
                            } catch (JSONException e) {
                                DialogFactory.showAlertDialog(context, e.toString(), context.getString(R.string.error));
                            }
                        }
                    } else {
                        DialogFactory.showAlertDialog(context, context.getString(R.string.error_work_server), context.getString(R.string.error));
                    }
                }else{
                    DialogFactory.showAlertDialog(context, context.getString(R.string.error_work_server), context.getString(R.string.error));
                }
            }else{
                DialogFactory.showAlertDialog(context, context.getString(R.string.error_work_server), context.getString(R.string.error));
            }

        }
    }

    static class ObjectReservationTask extends AsyncTask<Void, Void, String> implements Constants {
        private ProgressDialog progressDialog;
        private Context context;
        private String objectId;
        private String clientId;

        public ObjectReservationTask(String objectId, String clientId, Context context) {
            this.context = context;
            this.objectId = objectId;
            this.clientId = clientId;

            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(context.getString(R.string.object_card_sending_reservation));
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> args = new HashMap<>();
            args.put("objectId", objectId);
            args.put("clientId", clientId);

            return WebService1C.sendRequest(Constants.SERVER_SOAP_METHOD_OBJECT_RESERVATION, context, args);
        }

        @Override
        protected void onPreExecute() {
           progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
           progressDialog.dismiss();

            if (result != null) {
                if (!result.isEmpty()) {
                    JSONObject jsonObject = null;
                    Boolean executeResult = false;

                    try {
                        jsonObject = new JSONObject(result);
                    } catch (JSONException e) {
                        DialogFactory.showAlertDialog(context, e.toString(), context.getString(R.string.error));
                    }

                    if (jsonObject != null) {
                        try {
                            executeResult = jsonObject.getBoolean("result");
                        } catch (JSONException e) {
                            DialogFactory.showAlertDialog(context, e.toString(), context.getString(R.string.error));
                        }

                        if (executeResult == true) {
                            DialogFactory.showAlertDialog(context, context.getString(R.string.object_card_reserved), context.getString(R.string.reserved));
                        } else {
                            try {
                                String executeMessage = jsonObject.getString("message");

                                DialogFactory.showAlertDialog(context, executeMessage, context.getString(R.string.error));
                            } catch (JSONException e) {
                                DialogFactory.showAlertDialog(context, e.toString(), context.getString(R.string.error));
                            }
                        }
                    } else {
                        DialogFactory.showAlertDialog(context, context.getString(R.string.error_work_server), context.getString(R.string.error));
                    }
                }else{
                    DialogFactory.showAlertDialog(context, context.getString(R.string.error_work_server), context.getString(R.string.error));
                }
            }else{
                DialogFactory.showAlertDialog(context, context.getString(R.string.error_work_server), context.getString(R.string.error));
            }

        }
    }

}
