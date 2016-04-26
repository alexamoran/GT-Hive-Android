package hive.mas.com.gthive;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that controls the contents of the Favorites list of the application
 */
public class Favorites {

    private static Favorites sFavorites;
    private Context mContext;
    private Set<String> mBuildingIds;

    /**
     * Set up a static context for the favorites
     *
     * @param context
     * @return
     */
    public static Favorites get(Context context) {
        if (sFavorites == null) {
            sFavorites = new Favorites(context);
        }
        return sFavorites;
    }

    /**
     * Constructor for the class
     *
     * @param context
     */
    private Favorites(Context context) {
        mBuildingIds = new HashSet<>();
        mContext = context;
    }

    /**
     * Save the favorites list
     *
     * @param context The Favorites context
     */
    private void save(Context context) {
        String filename = "favoritesFile";
        String string = "";
        for (String bid : mBuildingIds) {
            string = string + bid + "-";
        }
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read in the favorites list and mark all favorite buildings
     *
     * @param campus The Georgia Tech Campus
     * @return The list of favorited buildings
     */
    public List<Building> getFavoriteBuildings(Campus campus) {

        List<Building> buildings = new ArrayList<>();

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(mContext.openFileInput("favoritesFile")));
            String inputString;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }
            Log.i("FILE", stringBuffer.toString());
            String[] parts = stringBuffer.toString().split("-");
            for (String bid : parts) {
                if (bid != null) {
                    Building favoritedBuilding = campus.getBuilding(bid);
                    if (favoritedBuilding != null) {
                        buildings.add(favoritedBuilding);
                        favoritedBuilding.setFavorite(true);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buildings;
    }

    /**
     * Add a building to the favorites
     *
     * @param bid The building to be added
     */
    public void addBuildingId(String bid) {
        mBuildingIds.add(bid);
        save(mContext);
    }

    /**
     * Remove a building to the favorites
     *
     * @param bid The buiding to be removed
     */
    public void removeBuildingId(String bid, Campus campus) {
        mBuildingIds.remove(bid);
        campus.getBuilding(bid).setFavorite(false);
        save(mContext);
    }

    /* Getters and Setters */

    public List<String> getBuildingIds() {

        List<String> buildingIds = new ArrayList<>();
        buildingIds.addAll(mBuildingIds);
        return buildingIds;
    }

}
