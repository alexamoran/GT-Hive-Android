package hive.mas.com.gthive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class BuildingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Activity mActivity;

    private Building mBuilding;

    private TextView mNameTextView;
    private TextView mPercentageOccupiedTextView;
    private CheckBox mFavoriteCheckbox;

    /**
     * Constructor that takes in the activity and the view
     *
     * @param activity
     * @param itemView
     */
    public BuildingHolder(Activity activity, View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mActivity = activity;
        mNameTextView = (TextView) itemView.findViewById(R.id.list_item_building_name_text_view);
        mPercentageOccupiedTextView = (TextView) itemView.findViewById(R.id.percentage_occupied_text_view);
        mFavoriteCheckbox = (CheckBox) itemView.findViewById(R.id.favorite_button);
    }

    /**
     * This will set up the CardView for each building
     *
     * @param building Building being set up for recycler view
     */
    public void bindBuilding(Building building) {
        mBuilding = building;
        mNameTextView.setText(mBuilding.getName());

        mFavoriteCheckbox.setChecked(mBuilding.getFavorite());

        mFavoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Favorites favorites = Favorites.get(mActivity);
                if (isChecked) {
                    favorites.addBuildingId(mBuilding.getBId());
                } else {
                    favorites.removeBuildingId(mBuilding.getBId(), Campus.get(mActivity));
                }
            }
        });

        setPercentageOccupiedTextView(building.getOccupancy());
    }

    @Override
    public void onClick(View v) {
        Intent intent = BuildingPagerActivity.newIntent(mActivity, mBuilding.getId());
        mActivity.startActivity(intent);
    }

    /**
     * This method will change the color of the occupancy textview for each building
     *
     * @param occupancyPercentage The occupancy of the building
     */
    public void setPercentageOccupiedTextView(int occupancyPercentage) {

        int color = R.color.OldGold;
        mPercentageOccupiedTextView.setText("" + occupancyPercentage + "");
        Drawable background = mPercentageOccupiedTextView.getBackground();

        // http://stackoverflow.com/questions/17823451/set-android-shape-color-programmatically
        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(ContextCompat.getColor(mActivity, color));
        } else if (background instanceof ShapeDrawable) {
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(mActivity, color));
        } else {
            Log.e("PercentOccupiedTextView", "not selected");
        }
    }
}