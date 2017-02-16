package com.rmuti.project.dogfeeder.connections;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rmuti.project.dogfeeder.utils.ShareData;


/**
 * Created by Sitthiphong on 1/13/2017 AD.
 */

public class Connections {
    private final String TAG = "Connections";
    private ConnectionsListener connectionsListener;
    private Context context;
    private ShareData shareData;

    public Connections(Context context) {
        this.context = context;
        shareData = new ShareData(context);
    }

    public void setAutoFood(ConnectionsListener connectionsListener) {
        this.connectionsListener = connectionsListener;
        String url = "http://" + shareData.getIPControlFood() + "/food?autofood=" + !shareData.getAutoFood();
        Log.e(TAG, "setAutoFood: url: " + url);
        new connectTask("Set auto food " + ((!shareData.getAutoFood()) ? "on" : "off")).execute(url);
    }

    public void giveFood(ConnectionsListener connectionsListener) {
        this.connectionsListener = connectionsListener;
        String url = "http://" + shareData.getIPControlFood() + "/food?givefood=true";
        Log.e(TAG, "giveFood: url: " + url);
        new connectTask("Give food to Dog").execute(url);
    }

    public void water(boolean value, ConnectionsListener connectionsListener) {
        this.connectionsListener = connectionsListener;
        String url = "http://" + shareData.getIPControlFood() + "/water?value=" + value;
        Log.e(TAG, "Water: url: " + url);
        new connectTask((value ? "Open Water" : "Close Water")).execute(url);
    }


    private class connectTask extends AsyncTask<String, Void, String> {

        private String messageLoading;

        public connectTask(String messageLoading) {
            this.messageLoading = messageLoading;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            return new OKHttpHelper().get(params[0]);
            //return new UrlConnectionHelper().get(params[0]);
        }

        protected void onPostExecute(String message) {
            if (connectionsListener != null) {
                connectionsListener.onServerResponse(message);
            }
        }
    }


    public interface ConnectionsListener {
        public void onServerResponse(String message);
    }
}
