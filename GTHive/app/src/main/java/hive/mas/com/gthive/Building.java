package hive.mas.com.gthive;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Building extends Location {

    private List<Floor> mFloors;
    private boolean mFavorite;

    /**
     * Constructor for building
     */
    public Building(String id) {
        super(id);
        mFloors = new ArrayList<>();
        mFavorite = false;
    }

    /**
     * Constructor for building
     */
    public Building(String id, String name) {
        super(id, name);
        mFloors = new ArrayList<>();
        mFavorite = false;
    }

    /**
     * Check to see if a building is in the favorites list
     * @param context The context of the favorites fragment
     * @return Either true or false
     */
    public boolean isFavorite(Context context) {

        Favorites favorites = Favorites.get(context);

        return favorites.getBuildingIds().contains(this.getBId());
    }

    /**
     * Add a new floor to a building
     *
     * @param floor
     */
    public void addFloor(Floor floor) {
        mFloors.add(floor);
    }

    /**
     * Sort the floors so that they display in numerical order
     */
    public void sortFloors() {
        Collections.sort(mFloors, new Comparator<Floor>() {

            @Override
            public int compare(Floor f1, Floor f2) {
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
        });
    }


    /* Accessors and Modifiers */

    public List<Floor> getFloors() {
        return mFloors;
    }

    public Floor getFloor(char floor) {
        for (Floor f : getFloors()) {
            if (f.getFloorNumber() == floor) {
                return f;
            }
        }
        return null;
    }

    public boolean getFavorite() {
        return mFavorite;
    }

    public void setFavorite(Boolean bool) {
        mFavorite = bool;
    }
}
