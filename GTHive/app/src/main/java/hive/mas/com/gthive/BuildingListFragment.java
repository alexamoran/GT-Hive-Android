package hive.mas.com.gthive;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * This fragment controls and displays the recycler view for all of the buildings
 * On the Georgia Tech Campus. (The home page of the application.)
 *
 */
public class BuildingListFragment extends Fragment {

    private RecyclerView mBuildingRecyclerView;
    private BuildingAdapter mAdapter;

    public static final String ARG_PAGE = "ARG_PAGE";

    /**
     * This function gives our app the ability to use tabs
     *
     * @param page Integer value of the page the tab is on
     * @return The fragment the tab is on
     */
    public static BuildingListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        BuildingListFragment fragment = new BuildingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new campus when the app is opened
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        new FetchBuildingsTask(Campus.get(getActivity())).execute();
    }

    /**
     * Set up the recycler view
     *
     * @param inflater Inflator for the view
     * @param container Container for the view
     * @param savedInstance Saved Instance if we need it
     * @return The newly set up view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mBuildingRecyclerView = (RecyclerView) view.findViewById(R.id.building_recycler_view);
        mBuildingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    /**
     * Update the UI whenever the tab is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Check if the UI has changed and update if we need to
     */
    private void updateUI() {

        Campus campus = Campus.get(getActivity());
        List<Building> buildings = campus.getBuildings();

        if (mAdapter == null) {
            mAdapter = new BuildingAdapter(getActivity(), buildings);
            mBuildingRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This is a private class that will be used to set up a new camus by calling the API fetcher
     */
    private class FetchBuildingsTask extends AsyncTask<Void, Void, Campus> {

        Campus campus;

        protected FetchBuildingsTask(Campus campus) {
            this.campus = campus;
        }

        @Override
        protected Campus doInBackground(Void... params) {
            if (this.campus.getBuildings().size() == 0) {
                campus = new APIFetcher().getOccupancyOfAllBuildings(this.campus);
                Favorites fav = Favorites.get(getActivity());
                List<Building> buildings = fav.getFavoriteBuildings(campus);

                return campus;
            } else {
                return this.campus;
            }
        }

        @Override
        protected void onPostExecute(Campus campus) {
            updateUI();
        }
    }
}