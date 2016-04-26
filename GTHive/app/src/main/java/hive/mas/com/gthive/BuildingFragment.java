package hive.mas.com.gthive;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * This class is used to set up the more information pages for each building
 * whenever a building is clicked from the recycler view
 *
 */
public class BuildingFragment extends android.support.v4.app.Fragment {

    private static final String ARG_BUILDING_ID = "building_id";

    private Building mBuilding;
    private TextView mOccupancyTextView;
    private TextView mFirstBestTimeTextView;
    private TextView mSecondBestTimeTextView;
    private TextView mThirdBestTimeTextView;
    private LinearLayout mFloorsLinearLayout;

    /**
     * Set up a static instance for the individual building pages
     *
     * @param buildingId The building whose page is being viewed
     * @return Anew building fragment
     */
    public static BuildingFragment newInstance(String buildingId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BUILDING_ID, buildingId);

        BuildingFragment fragment = new BuildingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create the new building fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String buildingId = (String) getArguments().getSerializable(ARG_BUILDING_ID);
        mBuilding = Campus.get(getActivity()).getBuilding(buildingId);
    }

    /**
     * Create the new view for the building fragment
     *
     * @param inflater The inflator for the view
     * @param container The container for the view
     * @param savedInstanceState
     * @return The view for the building
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_building, container, false);

        mOccupancyTextView = (TextView) v.findViewById(R.id.occupancy);
        mOccupancyTextView.setText("" + mBuilding.getName() + "\n" + "Total Occupancy: " + mBuilding.getOccupancy());


        /* Set Up Graph */

        List<Integer> todaysCrowd = new ArrayList<>();
        List<Integer> averageCrowd = new ArrayList<>();
        for (int i = 0; i < 24; i++) {todaysCrowd.add(0); averageCrowd.add(0);}

        // Query our API for the average value points for the building
        FetchValuesTask task1 = new FetchValuesTask(averageCrowd, mBuilding.getBId(),"avg");

        List<List<Integer>> myList1 = new ArrayList<>();
        try {
            myList1.add(task1.execute().get());
        } catch (InterruptedException ie) {

        } catch (ExecutionException ee) {

        }

        averageCrowd = myList1.get(0);

        // Query our API for today's value points for the building
        FetchValuesTask task2 = new FetchValuesTask(averageCrowd, mBuilding.getBId(),"today");

        List<List<Integer>> myList2 = new ArrayList<>();
        try {
            myList2.add(task2.execute().get());
        } catch (InterruptedException ie) {

        } catch (ExecutionException ee) {

        }

        todaysCrowd = myList2.get(0);


        // Get the current hour of the day for todays List
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String hour = currentDateandTime.substring(9, 11);
        Scanner s = new Scanner(hour);
        int todayHour = s.nextInt();

        // Adjust today's crowd
        List<Integer> adjustedTodaysCrowd = new ArrayList<>();
        for (int i = 0; i <= todayHour; i++) {
            adjustedTodaysCrowd.add(todaysCrowd.get(i));
        }

        // Adjust the average crowd
        List<Integer> adjustedAverageCrowd = new ArrayList<>();
        for (int i = adjustedTodaysCrowd.size() + 1; i < 24; i++) {
            adjustedAverageCrowd.add(averageCrowd.get(i));
        }

        drawLineChart(v, adjustedTodaysCrowd, adjustedAverageCrowd);

        // Set up and display the best time to go to the building
        List<Integer> currentData = new ArrayList<>();
        for (int i = 0; i < adjustedTodaysCrowd.size(); i++) {currentData.add(adjustedTodaysCrowd.get(i));}
        for (int i = 0; i < adjustedAverageCrowd.size(); i++) {currentData.add(adjustedAverageCrowd.get(i));}
        mFirstBestTimeTextView = (TextView) v.findViewById(R.id.best_times_id);
        mFirstBestTimeTextView.setText("Estimated Best Times To Go:");

        mFirstBestTimeTextView = (TextView) v.findViewById(R.id.first_best_time_text_view);
        int minMorning = 1000000;
        int index = 0;
        for (int i = 7; i <= 12; i++) {
            int temp = averageCrowd.get(i);
            if (temp < minMorning) {
                minMorning = temp;
                index = i;
            }
        }
        if (index == 12) {
            mFirstBestTimeTextView.setText("12pm");
        } else {
            mFirstBestTimeTextView.setText(index + "am");
        }

        mSecondBestTimeTextView = (TextView) v.findViewById(R.id.second_best_time_text_view);
        int minAfternoon = 1000000;
        index = 0;
        for (int i = 13; i <= 17; i++) {
            int temp = averageCrowd.get(i);
            if (temp < minAfternoon) {
                minAfternoon = temp;
                index = i;
            }
        }
        mSecondBestTimeTextView.setText((index - 11) + "pm");

        mThirdBestTimeTextView = (TextView) v.findViewById(R.id.third_best_time_text_view);
        int minNight = 1000000;
        index = 0;
        for (int i = 18; i < 24; i++) {
            int temp = averageCrowd.get(i);
            if (temp < minNight) {
                minNight = temp;
                index = i;
            }
        }
        if (averageCrowd.get(0) < minNight) {
            index = 0;
        }

        if (index == 0) {
            mThirdBestTimeTextView.setText("12am");
        } else {
            mThirdBestTimeTextView.setText((index - 11) + "pm");
        }


        // Set up Floors linear layout
        mFloorsLinearLayout = (LinearLayout) v.findViewById(R.id.floors_linear_layout);

        if (mBuilding.getFloors() != null) {
            if (mBuilding.getFloors().size() >= 2) {
                mBuilding.sortFloors();
            }
        }

        for (Floor f : mBuilding.getFloors()) {
            if (f.getOccupancy() != 0) {
                View listItemFloor = inflater.inflate(R.layout.list_item_floor, null, false);

                TextView floorNumberTextView = (TextView) listItemFloor.findViewById(R.id.floor_number_text_view);
                floorNumberTextView.setText("Floor " + f.getFloorNumber());

                TextView floorOccupancyTextView = (TextView) listItemFloor.findViewById(R.id.floor_occupancy_text_view);
                floorOccupancyTextView.setText("" + f.getOccupancy());

                mFloorsLinearLayout.addView(listItemFloor);
            }
        }

        return v;
    }

    /**
     * Actually draw the line graph on the Building Page
     *
     * @param v The view being drawn into
     * @param todaysCrowdValues Today's crowd values
     * @param avgCrowdValues The average Crowd for this building over the day
     */
    public void drawLineChart(View v, List<Integer> todaysCrowdValues, List<Integer> avgCrowdValues) {

        /* Create data entries1 and labels for todaysCrowd Values */
        ArrayList<Entry> entries1 = new ArrayList<>();
        for (int hour = 0; hour < todaysCrowdValues.size(); hour++) {
            entries1.add(new Entry(todaysCrowdValues.get(hour), hour));
        }

        /* Create data entries2 and labels for rodCrowdValues Values */
        ArrayList<Entry> entries2 = new ArrayList<>();
        for (int hour = 0; hour < avgCrowdValues.size(); hour++) {
            entries2.add(new Entry(avgCrowdValues.get(hour), hour + todaysCrowdValues.size()));
        }

        // Create data set from data entries1
        LineDataSet dataset1 = new LineDataSet(entries1, "Today's Crowd");
        LineDataSet dataset2 = new LineDataSet(entries2, "Predicted Crowd");

        // Set the color for the data sets
        dataset1.setColor(Color.rgb(0, 37, 76)); // GT Navy
        dataset2.setColor(Color.rgb(238, 178, 17)); // Buzz Gold

        // Aggregate the data sets
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(dataset1);
        dataSets.add(dataset2);

        /* Create the chart */
        LineChart chart = (LineChart) v.findViewById(R.id.chart);

        // Hide grid lines from x axis
        chart.getXAxis().setDrawGridLines(false);

        // Hide labels and grid lines from y axis
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);

        // Don't label each node on graph
        chart.setDrawMarkerViews(false);
        chart.setDescription(" ");
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false);

        List<String> myList = new ArrayList(Arrays.asList("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"));
        chart.getXAxis().setLabelsToSkip(1);
        chart.getXAxis().setTextSize(6);
        LineData data = new LineData(myList, dataSets);

        chart.setData(data);
        chart.animateY(1000);
    }

    /**
     * This is a private class that will be used to get the values
     * For the graph for each building
     *
     */
    private class FetchValuesTask extends AsyncTask<Void, Void, List<Integer>> {

        List<Integer> values;
        String bid;
        String str;

        protected FetchValuesTask(List<Integer> values, String bid, String str) {
            this.values = values;
            this.bid = bid;
            this.str = str;
        }

        @Override
        protected List<Integer> doInBackground(Void... params) {
            this.values = new APIFetcher().getValues(bid, str);
            return this.values;
        }
    }
}
