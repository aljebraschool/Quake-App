/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

/*
 * An EarthquakeActivity class that models how an earthquake
 * data pulled from the internet is processed and displayed
 * */

//import declaration of needed libraries
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager.LoaderCallbacks;
import android.os.Bundle;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;





public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<EarthquakeInfo>> {

    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private QuakeAdapter adapter;
    private final String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private TextView empty_text_view;
    private ProgressBar list_view_progress;


    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} with empty list of earthquakes
        adapter = new QuakeAdapter(this, new ArrayList<EarthquakeInfo>());

        //find a reference to the empty textView
        empty_text_view = (TextView) findViewById(R.id.empty_text_view);

        //use the @setEmptyView method to set the empty textView on the earthquakelistView
        earthquakeListView.setEmptyView(empty_text_view);

        //find the reference to the listView progress bar
        list_view_progress = (ProgressBar) findViewById(R.id.list_view_progress);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //initialize the loader with the given ID
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else{
            list_view_progress.setVisibility(View.GONE);
            empty_text_view.setText(R.string.no_internet_connection);
        }






        //Create an onItemClickListener event on every list item in the listview
        // to implement an explicit intent to navigate to the USGS website
        // associated with every earthquake returned from custom class EarthquakeInfo
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the current earthquake that was clicked on by storing the position of
                // the current item in the list using the getItem ArrayAdapter
                // method then store this in earthquakeInfo variable
                EarthquakeInfo currentEarthquake = adapter.getItem(position);

                //implement explicit intent to get to every website associated with each earthquake
                Uri webpage = Uri.parse(currentEarthquake.getWebpage());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * Implementing the callback methods below to respond to loaderManager events
    * */

    @Override
    public Loader<List<EarthquakeInfo>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        // Create a new loader for the given URL
        return new EarthquakeLoader(this, uriBuilder.toString());




    }


    @Override
    public void onLoadFinished(Loader<List<EarthquakeInfo>> loader, List<EarthquakeInfo> data) {
        //hide the progress bar when the app completes loading its data
        list_view_progress.setVisibility(View.GONE);

        //only set a set to the empty textView when the loader has finished loading the data
        empty_text_view.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeInfo>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();

    }



}
