package hive.mas.com.gthive;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Building extends Location {

    private List<Floor> mFloors;
    private boolean mFavorite;

    public Building(String id) {
        super(id);
        mFloors = new ArrayList<>();
        mFavorite = false;
    }

    public Building(String id, String name) {
        super(id, name);
        mFloors = new ArrayList<>();
        mFavorite = false;
    }

    public boolean isFavorite(Context context) {

        Favorites favorites = Favorites.get(context);

        return favorites.getBuildingIds().contains(this.getBId());
    }

    // Add new Floor to the Building
    public void addFloor(Floor floor) {
        mFloors.add(floor);
    }

    // Returns a String of Floor Numbers
    public String getFloorNumbers() {
        String floors = "";
        for (Floor f : getFloors()) {
            floors += f.getFloorNumber() + " ";
        }
        return floors;
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

    public void setFloors(List<Floor> floors) {
        mFloors = floors;
    }

    public boolean getFavorite() {
        return mFavorite;
    }

    public void flipFavorite() {
        if (mFavorite == false) {
            mFavorite = true;
        } else {
            mFavorite = false;
        }
    }
}
