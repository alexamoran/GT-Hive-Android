package hive.mas.com.gthive;

import android.support.v4.app.Fragment;

public class BuildingListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BuildingListFragment();
    }
}
