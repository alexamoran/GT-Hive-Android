package hive.mas.com.gthive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * This class is used to help page between buildings when they are
 * pressed on in the recycler view
 *
 */
public class BuildingPagerActivity extends AppCompatActivity {

    private static final String EXTRA_BUILDING_ID = "hive.mas.com.gthive.building_id";

    private ViewPager mViewPager;
    private List<Building> mBuildings;

    /**
     * Static function to set up intents
     *
     * @param packageContext The package we want the intent for
     * @param buildingId The building being added to the intent
     * @return The new intent
     */
    public static Intent newIntent(Context packageContext, String buildingId) {
        Intent intent = new Intent(packageContext, BuildingPagerActivity.class);
        intent.putExtra(EXTRA_BUILDING_ID, buildingId);
        return intent;
    }

    /**
     * Creates the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_pager);

        String buildingId = (String) getIntent().getSerializableExtra(EXTRA_BUILDING_ID);

        // Find ViewPager
        mViewPager = (ViewPager) findViewById(R.id.activity_building_pager_view_pager);

        mBuildings = Campus.get(this).getBuildings(); // Get data set
        FragmentManager fragmentManager = getSupportFragmentManager(); // Get activity's instance of FragmentManager

        // FragmentStatePagerAdapter is the agent managing conversation with ViewPager
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Building building = mBuildings.get(position);
                return BuildingFragment.newInstance(building.getId());
            }

            @Override
            public int getCount() {
                return mBuildings.size();
            }
        });

        // Get index of building clicked, pass it to viewpager so it opens correct building
        for (int i = 0; i < mBuildings.size(); i++) {
            if (mBuildings.get(i).getId().equals(buildingId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
