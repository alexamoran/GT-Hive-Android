package hive.mas.com.gthive;

import android.support.v4.app.Fragment;

/**
 * Class that sets up the activity for the building list
 */
public class BuildingListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BuildingListFragment();
    }
}
