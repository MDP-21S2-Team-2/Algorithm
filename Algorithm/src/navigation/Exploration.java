//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package navigation;

import arena.ArenaView;
import arena.GridBox;
import java.io.PrintStream;
import java.sql.SQLOutput;
import java.util.Stack;

import robot.Command;
import view.SimulatorGUI;

import static view.SimulatorGUI.arenaView;

public class Exploration {
    private final int coverageLimit;
    private final int timeLimit;
    private int areaExplored;
    private long startTime;
    private long endTime;
    private int lastCalibrate;
    private boolean calibrationMode;
    private boolean realRun;
    private Command previousCommand;
    private boolean firstRun;
    public Exploration(int coverageLimit, int timeLimit, boolean realRun) {
        this.coverageLimit = coverageLimit;
        this.timeLimit = timeLimit;
        this.realRun = realRun;
        this.firstRun = true;
    }

    public void runExploration() {
        if (this.realRun) {
            System.out.println("Calibrated, Starting Exploration...");
        }
        System.out.println("Starting exploration...");
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + (long)(this.timeLimit * 1000);
        this.senseAndRepaint(firstRun, realRun);
        firstRun = false;
        arenaView.repaint();
        this.areaExplored = this.calculateAreaExplored();
        System.out.println("Explored Area: " + this.areaExplored);
        arenaView.calculateSpaceClearanceExploration();
        this.explorationLoop(SimulatorGUI.arenaView.robot.getCurrPosY(),SimulatorGUI.arenaView.robot.getCurrPosX());
        while (this.areaExplored < 300 && System.currentTimeMillis() <= this.endTime){
            fastestPathToUnexploredCells();
            this.explorationLoopUnexploredAreas(SimulatorGUI.arenaView.robot.getCurrPosY(),SimulatorGUI.arenaView.robot.getCurrPosX());
        }
        this.goHome();

    }

    private void senseAndRepaint(boolean firstRun, boolean realRun) {
        SimulatorGUI.arenaView.robot.setSensors();
        if (!firstRun || !realRun)
            SimulatorGUI.arenaView.robot.sense();
        SimulatorGUI.arenaView.repaint();
    }

    private void explorationLoop(int row, int col) {
        do {
            this.decideOnNextMove();
            this.areaExplored = this.calculateAreaExplored();
            System.out.println("Area explored: " + this.areaExplored);
            if (SimulatorGUI.arenaView.robot.getCurrPosY()==row &&  SimulatorGUI.arenaView.robot.getCurrPosX()==col){
                if (areaExplored >=200){
                    arenaView.robot.remote(Command.RIGHT,realRun);
                    break;
                }
            }
        } while(this.areaExplored < this.coverageLimit && System.currentTimeMillis() <= this.endTime);


    }
    private void explorationLoopUnexploredAreas(int row, int col) {
        do {
            this.decideOnNextMove();
            this.areaExplored = this.calculateAreaExplored();
            System.out.println("Area explored: " + this.areaExplored);
            if (SimulatorGUI.arenaView.robot.getCurrPosY()==row &&  SimulatorGUI.arenaView.robot.getCurrPosX()==col){
                if (areaExplored >=200){
                    break;
                }
            }
            else if (arenaView.gridArray[arenaView.robot.getCurrPosY()][arenaView.robot.getCurrPosX()].getEnteredCount() >= 4) break;
        } while(this.areaExplored < this.coverageLimit && System.currentTimeMillis() <= this.endTime);

    }

    private void fastestPathToUnexploredCells(){
        arenaView.calculateSpaceClearance();
        GridBox currentGridBox = arenaView.gridArray[arenaView.robot.getCurrPosY()][arenaView.robot.getCurrPosX()];
        System.out.println("Current Gridbox X: "+ currentGridBox.getX() + " Y:"+currentGridBox.getY());
        GridBox gridNextToUnExplored = findGridNextToUnexploredCell();//arenaView.gridArray[findGridNextToUnexploredCell().getY()][findGridNextToUnexploredCell().getX()];
        System.out.println("Nearest Grid Unxplored X: " + gridNextToUnExplored.getX() + " Y:" + gridNextToUnExplored.getY());
        navigation.FastestPath fp1 = new navigation.FastestPath(currentGridBox, gridNextToUnExplored);
        navigation.FastestPath fp2 = null;
        arenaView.robot.simulateFastestPath(fp1.findFastestPath(),new Stack<GridBox>());
        System.out.println("Fastest Path to Unexplored Completed");
    }

    private GridBox findGridNextToUnexploredCell(){
        System.out.println("Start findGridNextToUnexploredCell");
        int rowDifference;
        int colDifference;
        //Calculate Robot's position and determine where to look first
        if (arenaView.robot.getCurrPosY() > 10)
            rowDifference = 2;
        else rowDifference = -2;
        if (arenaView.robot.getCurrPosX() > 7)
            colDifference = -2;
        else colDifference = 2;

        for (int col = 3; col <=11; col++){
            for (int row = 16; row >=3; row--){
                if (!arenaView.gridArray[row][col].isExplored()) {
                    System.out.println("This grid is unexplored X:" + col + " Y:" + row);
                    // Bottom Grid
                    if (arenaView.checkValidCoordinates(row + rowDifference, col)) {
                        if (arenaView.gridArray[row + rowDifference][col].isExplored() && arenaView.gridArray[row + rowDifference][col].isClearStatus() && !arenaView.gridArray[row + rowDifference][col].isObstacle()) {
                            return arenaView.getGrid(row + rowDifference, col);
                        }
                    }
                    //Left Grid
                     if (arenaView.checkValidCoordinates(row, col - colDifference)) {
                        if (arenaView.gridArray[row][col - colDifference].isExplored() && arenaView.gridArray[row][col - colDifference].isClearStatus() && !arenaView.gridArray[row][col - colDifference].isObstacle()) {
                            return arenaView.getGrid(row, col - colDifference);
                        }
                    }
                    //Right Grid
                    if (arenaView.checkValidCoordinates(row, col + colDifference)) {
                        System.out.println("In here previous");
                        if (arenaView.gridArray[row][col + colDifference].isExplored() && arenaView.gridArray[row][col + colDifference].isClearStatus() && !arenaView.gridArray[row][col + colDifference].isObstacle()) {
                            return arenaView.getGrid(row, col + colDifference);
                        }
                    }
                    //Top Grid
                    if (arenaView.checkValidCoordinates(row - rowDifference, col)) {
                        System.out.println("In here");
                        if (arenaView.gridArray[row - rowDifference][col].isExplored() && arenaView.gridArray[row - rowDifference][col].isClearStatus() && !arenaView.gridArray[row - rowDifference][col].isObstacle()) {
                            return arenaView.getGrid(row - rowDifference, col);
                        }
                    }
                }
            }
        }
        System.out.println("No Fastest Path to Unexplored Areas");
        return null;
    }

    private void decideOnNextMove() {
        //code for image recognition
//        if (this.previousCommand != Command.TURN180 && lookRight()){
//            this.previousCommand = Command.TURN180;
//            this.moveBot(Command.TURN180);
//        }
        if (this.previousCommand == Command.LEFT && this.lookForward()) {
            this.moveBot(Command.FORWARD);
            this.previousCommand = Command.FORWARD;
        } else if (this.lookLeft()) {
            this.moveBot(Command.LEFT);
            this.previousCommand = Command.LEFT;
        } else if (this.lookForward()) {
            this.moveBot(Command.FORWARD);
            this.previousCommand = Command.FORWARD;
        } else {
            this.moveBot(Command.RIGHT);
            this.previousCommand = Command.RIGHT;
        }

    }

    private boolean lookFarRight(){
        if (SimulatorGUI.arenaView.robot.getFacingDirection() == Direction.NORTH
                || SimulatorGUI.arenaView.robot.getFacingDirection() == Direction.SOUTH){
            int rowInc = 0;
            int colInc = 0;
            switch(SimulatorGUI.arenaView.robot.getFacingDirection()) {
                case NORTH:
                    colInc = +1;
                    break;
                case EAST:
                    rowInc= +1;
                    break;
                case SOUTH:
                    colInc = -1;
                    break;
                case WEST:
                    rowInc = -1;
                    break;
                default:
                    return false;
            }
            for (int i = 1; i <= 4; i++) {
                int row = arenaView.robot.getCurrPosY() + (rowInc * i);
                int col = arenaView.robot.getCurrPosX() + (colInc * i);

                if (!arenaView.checkValidCoordinates(row, col)) continue;
                if (arenaView.getGrid(row,col).isObstacle()) return true;
            }
        }
        return false;
    }

    private boolean lookRight() {
        switch(SimulatorGUI.arenaView.robot.getFacingDirection()) {
            case NORTH:
                return this.checkEast();
            case EAST:
                return this.checkSouth();
            case SOUTH:
                return this.checkWest();
            case WEST:
                return this.checkNorth();
            default:
                return false;
        }
    }

    private boolean lookForward() {
        switch(SimulatorGUI.arenaView.robot.getFacingDirection()) {
            case NORTH:
                return this.checkNorth();
            case EAST:
                return this.checkEast();
            case SOUTH:
                return this.checkSouth();
            case WEST:
                return this.checkWest();
            default:
                return false;
        }
    }

    private boolean lookLeft() {
        switch(SimulatorGUI.arenaView.robot.getFacingDirection()) {
            case NORTH:
                return this.checkWest();
            case EAST:
                return this.checkNorth();
            case SOUTH:
                return this.checkEast();
            case WEST:
                return this.checkSouth();
            default:
                return false;
        }
    }

    private boolean isGridExplored(int row, int col) {
        if (SimulatorGUI.arenaView.checkValidCoordinates(row, col)) {
            int c = SimulatorGUI.arenaView.robot.getCurrPosX();
            ArenaView var10000 = SimulatorGUI.arenaView;
            GridBox[][] gridArray = ArenaView.gridArray;
            return gridArray[row][col].isExplored();
        } else {
            return true;
        }
    }

    private boolean checkNorth() {
        int robotRow = SimulatorGUI.arenaView.robot.getCurrPosY();
        int robotCol = SimulatorGUI.arenaView.robot.getCurrPosX();
        return this.isExploredNotObstacle(robotRow - 2, robotCol - 1) && this.isExploredAndFree(robotRow - 2, robotCol) && this.isExploredNotObstacle(robotRow - 2, robotCol + 1);
    }

    private boolean checkEast() {
        int robotRow = SimulatorGUI.arenaView.robot.getCurrPosY();
        int robotCol = SimulatorGUI.arenaView.robot.getCurrPosX();
        return this.isExploredNotObstacle(robotRow - 1, robotCol + 2) && this.isExploredAndFree(robotRow, robotCol + 2) && this.isExploredNotObstacle(robotRow + 1, robotCol + 2);
    }

    private boolean checkSouth() {
        int robotRow = SimulatorGUI.arenaView.robot.getCurrPosY();
        int robotCol = SimulatorGUI.arenaView.robot.getCurrPosX();
        return this.isExploredNotObstacle(robotRow + 2, robotCol + 1) && this.isExploredAndFree(robotRow + 2, robotCol) && this.isExploredNotObstacle(robotRow + 2, robotCol - 1);
    }

    private boolean checkWest() {
        int robotRow = SimulatorGUI.arenaView.robot.getCurrPosY();
        int robotCol = SimulatorGUI.arenaView.robot.getCurrPosX();
        return this.isExploredNotObstacle(robotRow + 1, robotCol - 2) && this.isExploredAndFree(robotRow, robotCol - 2) && this.isExploredNotObstacle(robotRow - 1, robotCol - 2);
    }

    private void goHome() {
        System.out.println("\nExploration complete!");
        this.areaExplored = this.calculateAreaExplored();
        System.out.printf("%.2f%% Coverage", (double)this.areaExplored / 300.0D * 100.0D);
        System.out.println(", " + this.areaExplored + " Cells");
        PrintStream var10000 = System.out;
        long var10001 = System.currentTimeMillis() - this.startTime;
        var10000.println(var10001 / 1000L + " Seconds");
        if (!this.realRun) {
            int row = SimulatorGUI.arenaView.robot.getCurrPosY();
            int col = SimulatorGUI.arenaView.robot.getCurrPosX();
            new GridBox(new Location(SimulatorGUI.arenaView.robot.getCurrPosX(), SimulatorGUI.arenaView.robot.getCurrPosY()), false);
            new GridBox(new Location(1, 18), false);
            ArenaView var10002 = SimulatorGUI.arenaView;
            GridBox var6 = ArenaView.gridArray[row][col];
            ArenaView var10003 = SimulatorGUI.arenaView;
            arenaView.calculateSpaceClearance();
            FastestPath toHome = new FastestPath(var6, ArenaView.gridArray[18][1]);
            SimulatorGUI.arenaView.robot.simulateFastestPath(toHome.findFastestPath(),null);
        } else {
            arenaView.calculateSpaceClearance();
            GridBox currentGridBox = arenaView.gridArray[arenaView.robot.getCurrPosY()][arenaView.robot.getCurrPosX()];
            GridBox startPoint = arenaView.gridArray[18][1];
            FastestPath fp1 = new FastestPath(currentGridBox,startPoint);
            SimulatorGUI.arenaView.robot.executeFastestPath(fp1.findFastestPath(),null);
        }
        this.turnBotDirection(Direction.NORTH);
    }

    private boolean isExploredNotObstacle(int row, int col) {
        if (!SimulatorGUI.arenaView.checkValidCoordinates(row, col)) {
            return false;
        } else {
            GridBox grid = SimulatorGUI.arenaView.getGrid(row, col);
            return grid.isExplored() && !grid.isObstacle();
        }
    }

    private boolean isExploredAndFree(int row, int col) {
        if (!SimulatorGUI.arenaView.checkValidCoordinates(row, col)) {
            return false;
        } else {
            GridBox grid = SimulatorGUI.arenaView.getGrid(row, col);
            return grid.isExplored() && grid.isClearStatus() && !grid.isObstacle();
        }
    }

    private int calculateAreaExplored() {
        int result = 0;

        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 15; ++col) {
                if (SimulatorGUI.arenaView.getGrid(row, col).isExplored()) {
                    ++result;
                }
            }
        }

        return result;
    }

    private void moveBot(Command m) {
        int delay;
        if (realRun)
            delay = 100;
        else
            delay = 100;
        try {
            Thread.sleep(delay);
            SimulatorGUI.arenaView.robot.remote(m,realRun);
//            if (m != Command.CALIBRATE) {
//                this.senseAndRepaint();
//            }
            SimulatorGUI.arenaView.repaint();

//            if (this.realRun && !this.calibrationMode) {
//                this.calibrationMode = true;
//                if (this.canCalibrateOnTheSpot(SimulatorGUI.arenaView.robot.getFacingDirection())) {
//                    this.lastCalibrate = 0;
//                    this.moveBot(Command.CALIBRATE);
//                } else {
//                    ++this.lastCalibrate;
//                    if (this.lastCalibrate >= 5) {
//                        Direction targetDirection = this.getCalibrationDirection();
//                        if (targetDirection != null) {
//                            this.lastCalibrate = 0;
//                            this.calibrateBot(targetDirection);
//                        }
//                    }
//                }
//
//                this.calibrationMode = false;
//            }
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

    }

    private boolean canCalibrateOnTheSpot(Direction targetDirection) {
        int row = SimulatorGUI.arenaView.robot.getCurrPosY();
        int col = SimulatorGUI.arenaView.robot.getCurrPosX();
        switch(targetDirection) {
            case NORTH:
                return SimulatorGUI.arenaView.getIsObstacleOrWall(row + 2, col - 1) && SimulatorGUI.arenaView.getIsObstacleOrWall(row + 2, col) && SimulatorGUI.arenaView.getIsObstacleOrWall(row + 2, col + 1);
            case EAST:
                return SimulatorGUI.arenaView.getIsObstacleOrWall(row + 1, col + 2) && SimulatorGUI.arenaView.getIsObstacleOrWall(row, col + 2) && SimulatorGUI.arenaView.getIsObstacleOrWall(row - 1, col + 2);
            case SOUTH:
                return SimulatorGUI.arenaView.getIsObstacleOrWall(row - 2, col - 1) && SimulatorGUI.arenaView.getIsObstacleOrWall(row - 2, col) && SimulatorGUI.arenaView.getIsObstacleOrWall(row - 2, col + 1);
            case WEST:
                return SimulatorGUI.arenaView.getIsObstacleOrWall(row + 1, col - 2) && SimulatorGUI.arenaView.getIsObstacleOrWall(row, col - 2) && SimulatorGUI.arenaView.getIsObstacleOrWall(row - 1, col - 2);
            default:
                return false;
        }
    }

    private Direction getCalibrationDirection() {
        Direction currentDirection = SimulatorGUI.arenaView.robot.getFacingDirection();
        Direction[] directionToCheck = new Direction[3];
        switch(SimulatorGUI.arenaView.robot.getFacingDirection()) {
            case NORTH:
                directionToCheck[0] = Direction.WEST;
                directionToCheck[1] = Direction.EAST;
                directionToCheck[2] = Direction.SOUTH;
            case SOUTH:
                directionToCheck[0] = Direction.EAST;
                directionToCheck[1] = Direction.WEST;
                directionToCheck[2] = Direction.NORTH;
            case EAST:
                directionToCheck[0] = Direction.NORTH;
                directionToCheck[1] = Direction.SOUTH;
                directionToCheck[2] = Direction.WEST;
            case WEST:
                directionToCheck[0] = Direction.SOUTH;
                directionToCheck[1] = Direction.NORTH;
                directionToCheck[2] = Direction.EAST;
            default:
                if (this.canCalibrateOnTheSpot(directionToCheck[0])) {
                    return directionToCheck[0];
                } else if (this.canCalibrateOnTheSpot(directionToCheck[1])) {
                    return directionToCheck[1];
                } else {
                    return this.canCalibrateOnTheSpot(directionToCheck[2]) ? directionToCheck[2] : null;
                }
        }
    }

    private void calibrateBot(Direction calibrateDirection) {
        Direction origDir = SimulatorGUI.arenaView.robot.getFacingDirection();
        this.turnBotDirection(calibrateDirection);
        this.moveBot(Command.CALIBRATE);
        this.turnBotDirection(origDir);
    }

    private void turnBotDirection(Direction targetDirection) {
        int numOfTurn = Math.abs(SimulatorGUI.arenaView.robot.getFacingDirection().ordinal() - targetDirection.ordinal());
        if (numOfTurn > 2) {
            numOfTurn %= 2;
        }

        if (numOfTurn == 1) {
            if (Direction.getNextDirectionClockwise(SimulatorGUI.arenaView.robot.getFacingDirection()) == targetDirection) {
                this.moveBot(Command.RIGHT);
            } else {
                this.moveBot(Command.LEFT);
            }
        } else if (numOfTurn == 2) {
            this.moveBot(Command.RIGHT);
            this.moveBot(Command.RIGHT);
        }
    }
}
