package hive.mas.com.gthive;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Campus {
    private final String TAG = "CAMPUS";
    private static Campus sCampus;
    private List<Building> mBuildings;
    private Favorites mFavorites;

    public static Campus get(Context context) {
        if (sCampus == null) {
            sCampus = new Campus(context);
        }
        return sCampus;
    }

    private Campus(Context context) {
        mBuildings = new ArrayList<>();

        // Sort mBuildings Alphabetically
        sortBuildings();

        mFavorites = Favorites.get(context);
    }

    public List<Building> getBuildings() {
        return mBuildings;
    }

    public Building getBuilding(String id) {
        for (Building building : mBuildings) {
            if (building.getId().equals(id)) {
                return building;
            }
        }
        return null;
    }

    public void addBuilding(Building building) {
        mBuildings.add(building);
    }

    public List<Building> getFavoriteBuildings() {

        List<Building> buildings = new ArrayList<>();
        for (String bid : mFavorites.getBuildingIds()) {

            Building favoritedBuilding = getBuilding(bid);
            buildings.add(favoritedBuilding);
        }
        return buildings;
    }

    private void sortBuildings() {

        Collections.sort(mBuildings, new Comparator<Building>() {

            @Override
            public int compare(Building b1, Building b2) {
                return b1.getName().compareToIgnoreCase(b2.getName());
            }
        });
    }
}
