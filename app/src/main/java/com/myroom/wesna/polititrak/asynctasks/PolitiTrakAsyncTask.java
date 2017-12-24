package com.myroom.wesna.polititrak.asynctasks;

import android.os.AsyncTask;

import com.myroom.wesna.polititrak.AsyncResponse;
import com.myroom.wesna.polititrak.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class PolitiTrakAsyncTask extends AsyncTask<URL, Void, String> {

    AsyncResponse delegate = null;

    public PolitiTrakAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        String result = null;

        try {
            result = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String recentBillsResults) {
        delegate.asyncResponseHandler(recentBillsResults);
    }
}
