package hive.mas.com.gthive;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class APIFetcher {

    private static final String TAG = "APIFetcher";

    /**
     * Function that will connect to the internet and turn our API query into parasable JSON
     *
     * @param urlSpec The http:// address of our API
     * @return A byte array of the API call
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with" + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Function used to pass url to the above function
     *
     * @param urlSpec
     * @return The JSON string queried from our API
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    /**
     * This function sets up the buildings, floors, and occupancies of all buildings on campus
     *
     * @param campus The Georgia Tech campus being set up
     * @return The newly set up campus
     */
    public Campus getOccupancyOfAllBuildings(Campus campus) {

        String ourAPI = "http://gthive.me/API/proxy";

        //String ourAPI = "wifi.dssg.rnoc.gatech.edu:3000/api/count/?details=true";

        try {
            String jsonString = getUrlString(ourAPI);
            JSONObject jsonBody = new JSONObject(jsonString);
            JSONArray accessPoints = jsonBody.optJSONArray("AccessPoints");

            for(int i = 0; i < accessPoints.length(); i++) {
                JSONObject ap = accessPoints.optJSONObject(i);
                String b_id = ap.getString("building_id");
                String buildingName = ap.getString("building_name");
                char floor = ap.getString("floor").charAt(0);
                int occupancy = ap.getInt("clientcount");

                if (campus.getBuilding(b_id) == null) {
                    Building newBuilding = new Building(b_id, buildingName);
                    campus.addBuilding(newBuilding);
                } else {
                    int buildingOcc = campus.getBuilding(b_id).getOccupancy() + occupancy;
                    campus.getBuilding(b_id).setOccupancy(buildingOcc);
                }

                if (campus.getBuilding(b_id).getFloor(floor) == null) {
                    Floor newFloor = new Floor(b_id, floor);
                    campus.getBuilding(b_id).addFloor(newFloor);
                } else {
                    int floorOcc = campus.getBuilding(b_id).getFloor(floor).getOccupancy() + occupancy;
                    campus.getBuilding(b_id).getFloor(floor).setOccupancy(floorOcc);
                }
            }
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        campus.sortBuildings();
        return campus;
    }

    public List<Integer> getAverages(List<Integer> averages, String bid) {
        //for (int i =0; i < 24; i++) {
        String ourAPI = "http://gthive.me/API/historic/" + bid; // + "/hour=" + i;
        try {
            String jsonString = getUrlString(ourAPI);
            JSONObject jsonBody = new JSONObject(jsonString);
            int average = jsonBody.getInt("avg");
            averages.add(average);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        //}
        return averages;
    }
}