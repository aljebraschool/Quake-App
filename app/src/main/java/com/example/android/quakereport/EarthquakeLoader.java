package com.example.android.quakereport;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/*
* Loader class to implement the AsynctaskLoader as a separate activity in the background
* */
public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeInfo>> {
    /** Query URL */
    private final String mUrl;
    private final String LOG_TAG = EarthquakeActivity.class.getSimpleName();


    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<EarthquakeInfo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<EarthquakeInfo> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        Log.i(LOG_TAG, "TEST: loadInBackground - started loading data in the background" );
        return earthquakes;
    }

    //method to start the background thread
    @Override
    protected void onStartLoading() {
        forceLoad();

    }
}
