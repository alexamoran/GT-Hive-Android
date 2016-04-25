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

    /**
     * Set up a static context for the Campus
     *
     * @param context
     * @return
     */
    public static Campus get(Context context) {
        if (sCampus == null) {
            sCampus = new Campus(context);
        }
        return sCampus;
    }

    /**
     * Constructor for the Campus
     *
     * @param context
     */
    private Campus(Context context) {
        mBuildings = new ArrayList<>();
    }

    /**
     * Get a specific building in the campus bulding list by building id
     *
     * @param id The building id
     * @return The building corresponding to the id
     */
    public Building getBuilding(String id) {
        for (Building building : mBuildings) {
            if (building.getId().equals(id)) {
                return building;
            }
        }
        return null;
    }

    /**
     * Add a building to the campus
     *
     * @param building The building to be added
     */
    public void addBuilding(Building building) {
        mBuildings.add(building);
    }

    /**
     * Sort the building list alphabetically
     *
     */
    public void sortBuildings() {

        Collections.sort(mBuildings, new Comparator<Building>() {

            @Override
            public int compare(Building b1, Building b2) {
                return b1.getName().compareToIgnoreCase(b2.getName());
            }
        });
    }

    public List<Building> getBuildings() {
        return mBuildings;
    }

}
