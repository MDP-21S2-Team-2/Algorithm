package robot;

import arena.ArenaView;
import arena.GridBox;
import navigation.Direction;

import java.util.HashMap;

import static view.SimulatorGUI.arenaView;

public class  Sensor {

    int xPos;
    int yPos;
    int lowerLimit;
    int upperLimit;
    SensorLocation location;
    Direction direction;
    SensorRange range;
    boolean hitObstacle;
    boolean hitWall;
    int[] sense_XY = new int[2];



    public Sensor(int xPos, int yPos, SensorLocation location, SensorRange range, Direction direction) {
        this.xPos = xPos;
        this.yPos = yPos;

        switch (range){
            case SHORT:
                this.lowerLimit = 1;
                this.upperLimit = 2;
                break;
            case LONG:
                this.lowerLimit = 1;
                this.upperLimit = 4;
        }

        this.location = location;
        this.range = range;
        this.hitObstacle = false;
        this.hitWall = false;
        this.direction = direction;
    }

    public enum SensorRange{
        SHORT,
        LONG,
    }
    public enum SensorLocation{
        LEFT_FRONT(0),
        MIDDLE_FRONT(1),
        RIGHT_FRONT(2),
        LEFT(3),
        BOTTOM_LEFT(4),
        RIGHT(5);


        int type_value = 0;

        private SensorLocation (int type_value){
            this.type_value = type_value;
        }

        public int getValue(){
            return this.type_value;
        }

        public void setValue(int value){
            this.type_value = value;
        }
    }

    public void setSensor(int row, int col, Direction direction) {
        this.xPos = col;
        this.yPos = row;
        this.direction = direction;
    }

    public boolean senseArena (){
        switch (direction) {
            case NORTH:
                return getSensorValue(-1, 0);
            case EAST:
                return getSensorValue(0, 1);
            case SOUTH:
                return getSensorValue(+1, 0);
            case WEST:
                return getSensorValue(0, -1);
        }
        return false;
    }

    private boolean getSensorValue(int rowInc, int colInc) {
//        // Check if starting point is valid for sensors with lowerRange > 1.
//        if (lowerLimit > 1) {
//            for (int i = 1; i < this.lowerLimit; i++) {
//                int row = this.yPos + (rowInc * i);
//                int col = this.xPos + (colInc * i);
//
//                if (!arenaView.checkValidCoordinates(row, col)) return false;
//                if (arenaView.getGrid(row, col).isObstacle()) return true;
//            }
//        }

        for (int i = this.lowerLimit; i <= this.upperLimit; i++) {
            int row = this.yPos + (rowInc * i);
            int col = this.xPos + (colInc * i);

            if (!arenaView.checkValidCoordinates(row, col)) return false;

            arenaView.setExploredGrids(row,col);
            if (arenaView.isSimulatedObstacle(row,col)) {
                arenaView.setIsObstacleGrids(row,col);
                return true;
            }
        }
        return false;
    }

    public void senseRealArena(float sensorValue) {
        switch (direction) {
            case NORTH:
                processInputValue(sensorValue, -1, 0);
                break;
            case EAST:
                processInputValue(sensorValue, 0, 1);
                break;
            case SOUTH:
                processInputValue(sensorValue, +1, 0);
                break;
            case WEST:
                processInputValue(sensorValue, 0, -1);
                break;
        }

    }

    public void processInputValue(float inputValue, int rowInc, int colInc ){
        int sensorValue = 0;
        if (inputValue >= 3 && inputValue <= 8){
            sensorValue = 1;
        }
        // 8 to 13 not detected, can calibrate.
        else if (inputValue >= 13 && inputValue <= 20){
            sensorValue = 2;
        }
        else if (inputValue >= 23 && inputValue < 30){// && inputValue < 28){
            sensorValue = 3;
        }
        else if (inputValue >=33  && inputValue< 40){
            sensorValue = 4;
        }
        //if (sensorValue == 0) return;  // return value for LR sensor if obstacle before lowerRange

//        System.out.println("In processInput Value");
//        System.out.println("Lower and upper limit " + lowerLimit + ", "+ upperLimit);
//        for (int j = this.lowerLimit; j <= this.upperLimit; j++) {
//            System.out.println("In for loop process input value");
//            int row = this.yPos + (rowInc * j);
//            int col = this.xPos + (colInc * j);
//            if (!arenaView.checkValidCoordinates(row, col)) continue;
//            arenaView.setExploredGrids(row,col);
//            arenaView.getGrid(row, col).setExplored(true);
//            System.out.println("In for loop process input value after");
//
//        }

        // Update map according to sensor's value.

        for (int i = this.lowerLimit; i <= this.upperLimit; i++) {
            int row = this.yPos + (rowInc * i);
            int col = this.xPos + (colInc * i);
            //System.out.println("In Second for loop");
            if (!arenaView.checkValidCoordinates(row, col)) continue;

            arenaView.getGrid(row, col).setExplored(true);
            arenaView.setExploredGrids(row,col);

            if (sensorValue == i) {
                arenaView.setIsObstacleGrids(row, col);
                break;
            }
            // Override previous obstacle value if front sensors detect no obstacle.
            if (arenaView.getGrid(row, col).isObstacle()) {
                if (location.equals(0) || location.equals(1) || location.equals(2)) {
                    arenaView.setIsObstacleGrids(row, col);
                } else {
                    break;
                }
            }
        }
    }
}
