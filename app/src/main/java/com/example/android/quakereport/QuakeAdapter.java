package com.example.android.quakereport;

/*
* A custom ArrayAdapter class used to populate the list with the data gotten from ArrayList
* */


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuakeAdapter extends ArrayAdapter<EarthquakeInfo> {

    //A class variable used as delimiter while splitting location string
    private static final String LOCATION_SEPARATOR = " of ";


    //constructor...
    public QuakeAdapter(@NonNull Context context, List<EarthquakeInfo> earthquakeInfos) {
        super(context, 0, earthquakeInfos);
    }

    //implementing the visual appearance of the data by overriding the getView method of ArrayAdapter class
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //populate empty view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //get a reference to EarthquakeInfo class using getItem method
        EarthquakeInfo currentQuakeInfo = getItem(position);

        //formatting the returned magnitude value with helper method decimalFormat
        String quakeMagnitude = decimalFormat(currentQuakeInfo.getMagnitude());

        //displaying the magnitude on screen after formatting
        TextView magnitude_textview = convertView.findViewById(R.id.magnitude_textview);
        magnitude_textview.setText(quakeMagnitude);


        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude_textview.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentQuakeInfo.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        //get the original location of the earthquake in a variable for use
        String originalLocation = currentQuakeInfo.getLocation();

        //set variables to display the two location separately
        String primaryLocation;
        String locationOffset;


        //confirm if the original location contains the class variable declared above
        if(originalLocation.contains(LOCATION_SEPARATOR)){
            //split the string using the delimiter "of"
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }

        //if not assign something else
        else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        /*
        * Display the assigned value in the variable
        * locationOffset and primaryLocation in two different textviews
        * */

        TextView offset_location_textview = convertView.findViewById(R.id.offset_Location_textview);
        offset_location_textview.setText(locationOffset);

        TextView primary_location_textview = convertView.findViewById(R.id.primary_location_textview);
        primary_location_textview.setText(primaryLocation);

        //create date object to format the given
        // millisecond time value in the proper date format
        Date dateObject = new Date(currentQuakeInfo.getDate());

        //use the helper method (formatDate)
        // declared below to format the date object
        String formattedDate = formatDate(dateObject);

        /*
         * Display the assigned value in the variable in two different textViews
         * */
        TextView date_textview = convertView.findViewById(R.id.date_textview);
        date_textview.setText(formattedDate);

        TextView time_textview = convertView.findViewById(R.id.time_textview);
        String formattedTime = formatTime(dateObject);

        time_textview.setText(formattedTime);



        return convertView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorid;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor){

            case 0:
            case 1:
                magnitudeColorid = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorid = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorid = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorid = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorid = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorid = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorid = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorid = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorid = R.color.magnitude9;
                break;
            default:
                magnitudeColorid = R.color.magnitude10plus;
                break;

        }

        return ContextCompat.getColor(getContext(),magnitudeColorid);


    }


    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String decimalFormat(double quakeMagnitude){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(quakeMagnitude);
    }




}
