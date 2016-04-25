package hive.mas.com.gthive;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Controls views on tabs and which view gets inflated with each tab
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "All Buildings", "Favorites" };
    private Context context;

    public TabFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * Get the fragment at the current tab position
     *
     * @param position The tab position
     * @return The fragment at that position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // all buildings fragment activity
                return BuildingListFragment.newInstance(position + 1);
            case 1:
                // favorites list fragment activity
                return FavoriteListFragment.newInstance(position + 1);
        }

        return null;
    }

    /**
     * Generate title based on item position
     *
     * @param position The current tab position
     * @return The page title at the position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
