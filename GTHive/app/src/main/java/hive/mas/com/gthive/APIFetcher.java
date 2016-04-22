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

public class APIFetcher {

    private static final String TAG = "APIFetcher";

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

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    public Campus getOccupancyOfAllBuildings(Campus campus) {

        String ourAPI = "http://wifi.dssg.rnoc.gatech.edu:3000/api/count/?details=true"; //building_id=" + bid.getID();

        try {
            String jsonString = getUrlString(ourAPI);
            //Log.i(TAG, "TEST: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            //Log.i(TAG, "TEST2: " + jsonBody.optJSONArray("AccessPoints"));
            JSONArray accessPoints = jsonBody.optJSONArray("AccessPoints");

            for(int i = 0; i < accessPoints.length(); i++) {
                JSONObject ap = accessPoints.optJSONObject(i);
                Log.i(TAG, "APS: " + ap);
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

        return campus;
    }
}
