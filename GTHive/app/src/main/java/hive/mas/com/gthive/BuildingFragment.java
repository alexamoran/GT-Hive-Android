package hive.mas.com.gthive;

import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

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
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_building, container, false);

        mOccupancyTextView = (TextView) v.findViewById(R.id.occupancy);
        mOccupancyTextView.setText("" + mBuilding.getName()+ "\n" + "Total Occupancy: "+ mBuilding.getOccupancy());


        /* Draw Line Graph */

        int averageCrowdValues = (int)(100 * (Math.random() * 10));
        // List<Integer> todaysCrowdValues = mBuilding.getTodaysCrowdValues();
        // List<Integer> rodCrowdValues = mBuilding.getRodCrowdValues();
        // temporarily using random numbers
        List<Integer> todaysCrowdValues = new ArrayList<>();
        List<Integer> rodCrowdValues = new ArrayList<>();
        //List<Integer> rodCrowdValues = FetchAverages(rodCrowdValues);
        for (int i = 0; i < 12; i++) todaysCrowdValues.add( (int)(100 * (Math.random() * 10)));
        for (int i = 0; i < 12; i++) rodCrowdValues.add( (int)(100 * (Math.random() * 10)));

        drawLineChart(v, averageCrowdValues, todaysCrowdValues, rodCrowdValues);

        // Set up and display the best time to go to the building
        mFirstBestTimeTextView = (TextView) v.findViewById(R.id.best_times_id);
        mFirstBestTimeTextView.setText("Estimated Best Times To Go:");

        mFirstBestTimeTextView = (TextView) v.findViewById(R.id.first_best_time_text_view);
        int minMorning = 1000000;
        int index = 0;
        for (int i = 5; i < 11; i++) {
            int temp = rodCrowdValues.get(i);
            if (temp < minMorning) {
                minMorning = temp;
                index = i;
            }
        }
        String a = "am";
        if (index == 12) {
            a = "pm";
        }
        mFirstBestTimeTextView.setText(index+a);

        mSecondBestTimeTextView = (TextView) v.findViewById(R.id.second_best_time_text_view);
        int minAfternoon = 1000000;
        index = 0;
        for (int i = 11; i < 17; i++) {
            int temp = rodCrowdValues.get(i);
            if (temp < minAfternoon) {
                minAfternoon = temp;
                index = i;
            }
        }
        mSecondBestTimeTextView.setText(index+"pm");

        mThirdBestTimeTextView = (TextView) v.findViewById(R.id.third_best_time_text_view);
        int minNight = 1000000;
        index = 0;
        for (int i = 17; i < 23; i++) {
            int temp = rodCrowdValues.get(i);
            if (temp < minNight) {
                minNight = temp;
                index = i;
            }
        }
        a = "pm";
        if (index == 12) {
            a = "am";
        }
        mThirdBestTimeTextView.setText(index + a);


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
     * @param averageCrowdValue The average crowd values
     * @param todaysCrowdValues Today's crowd values
     */
    public void drawLineChart(View v, int averageCrowdValue, List<Integer> todaysCrowdValues, List<Integer> rodCrowdValues) {

        /* Create data entries1 and labels for todaysCrowd Values*/
        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int hour = 0; hour < todaysCrowdValues.size(); hour++) {
            entries1.add(new Entry(todaysCrowdValues.get(hour), hour));
            labels.add("" + hour);
        }

        /* Create data entries2 and labels for rodCrowdValues Values*/
        ArrayList<Entry> entries2 = new ArrayList<>();
        for (int hour = 0; hour < rodCrowdValues.size(); hour++) {
            entries2.add(new Entry(rodCrowdValues.get(hour), hour + todaysCrowdValues.size()));
            labels.add("" + (hour  + todaysCrowdValues.size()));
        }

        /* Create data entries for averageCrowdValue */
        ArrayList<Entry> entries3 = new ArrayList<>();
        entries3.add(new Entry(averageCrowdValue, 0)); entries3.add(new Entry(averageCrowdValue, 23));

        // Create data set from data entries1
        LineDataSet dataset1 = new LineDataSet(entries1, "Today's Crowd");
        LineDataSet dataset2 = new LineDataSet(entries2, "Predicted Crowd");
        //LineDataSet dataset3 = new LineDataSet(entries3, "Average Crowd");

        // Set the color for this data set
        dataset1.setColor(Color.rgb(0, 37, 76)); // GT Navy
        dataset2.setColor(Color.rgb(238, 178, 17)); // Buzz Gold
        //dataset3.setColor(Color.rgb(197, 147, 83)); // GT Gold

        // Aggregate all data sets
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(dataset1);
        dataSets.add(dataset2);
        //dataSets.add(dataset3);

        /* Create the chart */
        LineChart chart = (LineChart) v.findViewById(R.id.chart);

        // Hide Labels and grid lines from x axis
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(true);

        // Hide labels and grid lines from y axis
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);

        // Don't label each node on graph
        chart.setDrawMarkerViews(false);
        LineData data = new LineData(labels, dataSets);
        chart.setData(data);

        chart.setDescription("Today's Crowd");

        // animations
        chart.animateY(1000);
    }

    private List<Integer> FetchAverages(List<Integer> averages, String bid) {
        return new APIFetcher().getAverages(averages, bid);
    }
}
