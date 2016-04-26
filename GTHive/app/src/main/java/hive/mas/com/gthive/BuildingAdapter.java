package hive.mas.com.gthive;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Takes buildings from the Campus and binds them to CardViews
 * in the Recycler View
 *
 */
public class BuildingAdapter extends RecyclerView.Adapter<BuildingHolder> {

    private Activity mActivity;
    private List<Building> mBuildings;

    public BuildingAdapter(Activity activity, List<Building> buildings) {
        mActivity = activity;
        mBuildings = buildings;
    }

    /**
     * Set up the adapter for the recycler view
     *
     * @param parent The parent view
     * @param viewType The type of view
     * @return Return a new building holder
     */
    @Override
    public BuildingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = layoutInflater.inflate(R.layout.list_item_building, parent, false);
        return new BuildingHolder(mActivity, view);
    }

    /**
     *  Binds the new building with the view
     *
     * @param holder The building holder
     * @param position Where the holder should be palced in the recycler view
     */
    @Override
    public void onBindViewHolder(BuildingHolder holder, int position) {
        Building building = mBuildings.get(position);
        holder.bindBuilding(building);
    }

    /**
     * Gives the number of buildings
     * @return
     */
    @Override
    public int getItemCount() {
        return mBuildings.size();
    }
}