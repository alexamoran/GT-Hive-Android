package hive.mas.com.gthive;

/**
 * Parent class of Building and floor. Set up to follow
 * OOP principles.
 *
 */
public class Location {

    private String mId;
    private String mBId;
    private String mName;
    private int mOccupancy;

    /**
     * Constructor for the location
     *
     * @param id The id of the building
     */
    public Location(String id) {
        mId = id;
        loadBId();
        mOccupancy = 0;
    }

    /**
     * Constructor for the location
     *
     * @param id The id of the building
     * @param name The Building name
     */
    public Location(String id, String name) {
        mId = id;
        loadBId();
        mName = name;
        mOccupancy = 0;
    }

    /**
     * Make sure Building IDs are in the proper form
     *
     */
    private void loadBId() {
        mBId = (mId.split("-|_"))[0];
    }

    /* Accessors and Modifiers */

    public String getId() {
        return mId;
    }

    public String getBId() {
        return mBId;
    }

    public String getName() {
        return mName;
    }

    public int getOccupancy() {
        return mOccupancy;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setBId(String BId) {
        mBId = BId;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setOccupancy(int occupancy) {
        mOccupancy = occupancy;
    }
}
