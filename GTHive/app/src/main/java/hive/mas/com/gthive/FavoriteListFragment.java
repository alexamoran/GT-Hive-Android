package hive.mas.com.gthive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FavoriteListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private RecyclerView mBuildingRecyclerView;
    private BuildingAdapter mAdapter;

    /**
     * Establish a static instance of the favorite list fragment
     *
     * @param page The page number of the fragment for the tabs
     * @return The fragment
     */
    public static FavoriteListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FavoriteListFragment fragment = new FavoriteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mBuildingRecyclerView = (RecyclerView) view.findViewById(R.id.building_recycler_view);
        mBuildingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    /**
     * Update the UI every time the activity is resumed
     */
    @Override
    public void onResume() {
        updateUI();
        super.onResume();
    }

    /**
     * Private method that updates the recycler view for the favorites list
     *
     */
    private void updateUI() {

        Campus campus = Campus.get(getActivity());
        Favorites favorites = Favorites.get(getActivity());
        List<Building> buildings = favorites.getFavoriteBuildings(campus);

        if (mAdapter == null) {
            mAdapter = new BuildingAdapter(getActivity(), buildings);
            mBuildingRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
}