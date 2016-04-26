package hive.mas.com.gthive;

/**
 * Class for the different floors of buildings
 *
 */
public class Floor extends Location {

    private char mFloorNumber;

    /**
     * Constructor for the floor
     *
     * @param bId The Floors building number
     * @param floorNumber The floor number
     */
    public Floor(String bId, char floorNumber) {
        super(bId, bId + "_" + floorNumber);
        mFloorNumber = floorNumber;
    }

    /* Accessors and Modifiers */

    public char getFloorNumber() {
        return mFloorNumber;
    }

    public void setFloorNumber(char floorNumber) {
        mFloorNumber = floorNumber;
    }

}
