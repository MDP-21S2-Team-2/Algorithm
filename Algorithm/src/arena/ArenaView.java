//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package arena;

import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import navigation.Direction;
import navigation.Location;
import robot.Robot;

public class ArenaView extends JPanel {
    private List<Location> obstacleLocation = new ArrayList();
    private int gridWidth;
    private int gridHeight;
    public Robot robot = new Robot(true);
    public static GridBox[][] gridArray = new GridBox[ArenaConstants.NUM_ROWS][ArenaConstants.NUM_COLS];
    public static int[][] obstacleGrids = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    public static int[][] simulatedObstacleGrids = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    public static int[][] exploredGrids = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    public ArenaView() {
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(ArenaConstants.PANEL_WIDTH, ArenaConstants.PANEL_HEIGHT + 25));
        //this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.calculateDimension();
        this.fillGridBoxes();
        //this.createObstacles();
        System.out.println("finished creating grid box");
    }

    public void generateRandomObstacles(int numObstacles) {
        Random random = new Random();

        for(int i = 0; i < numObstacles; ++i) {
            int x = random.nextInt(15);
            int y = random.nextInt(20);
            Location obstacle = new Location(x, y);
            this.obstacleLocation.add(obstacle);
            gridArray[y][x].setObstacle(true);
            if (y < 19) {
            }
        }

    }

    public void createObstacles() {
        Location obstacle = new Location(7, 19);
        this.obstacleLocation.add(obstacle);
        gridArray[19][7].setObstacle(true);
        Location obstacle1 = new Location(0, 16);
        this.obstacleLocation.add(obstacle1);
        gridArray[16][0].setObstacle(true);
    }

    public void resetObstacles() {
        this.obstacleLocation.clear();
        for (int row = 0; row < ArenaConstants.NUM_ROWS; ++row) {
            for (int col = 0; col < ArenaConstants.NUM_COLS; ++col) {
                if (simulatedObstacleGrids[row][col] == 1){
                    simulatedObstacleGrids[row][col] =0;
                    gridArray[row][col].setObstacle(false);
                }
            }
        }
    }

    public void setAllUnexplored() {
        for(int row = 0; row < ArenaConstants.NUM_ROWS; ++row) {
            for(int col = 0; col < ArenaConstants.NUM_COLS; ++col) {
                if (!this.isInStartPoint(row, col) && !this.isInGoalPoint(row, col)) {
                    gridArray[row][col].setExplored(false);
                }
            }
        }

    }

    public void setAllExplored() {
        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 15; ++col) {
                gridArray[row][col].setExplored(true);
            }
        }

    }

    public void generateFastestPathObstacles() {
        //currently obstacles are generated in random and can be in start and end positions
        for (int i = 0; i < ArenaConstants.NUM_ROWS; i++) {
            for (int j = 0; j < ArenaConstants.NUM_COLS; j++) {
                if (simulatedObstacleGrids[i][j] == 1){
                    this.getGrid(i,j).setObstacle(true);
                    this.obstacleGrids[i][j] = 1;
                }
            }
        }
    }

    public void setExploredGrids(int row, int col) {
        exploredGrids[row][col] = 1;
        gridArray[row][col].setExplored(true);
    }

    public boolean getExploredGrids(int row, int col) {
        return exploredGrids[row][col] == 1;
    }

    public boolean getObstacleGrids(int row, int col) {
        return obstacleGrids[row][col] == 1;
    }

    public void setIsObstacleGrids(int row, int col) {
        obstacleGrids[row][col] = 1;
        gridArray[row][col].setObstacle(true);
    }

    public boolean isSimulatedObstacle(int row, int col) {
        return simulatedObstacleGrids[row][col] == 1;
    }

    public void robotMovement() {
        int x = this.robot.getCurrPosX();
        int y = this.robot.getCurrPosY();
        gridArray[y][x].setExplored(true);

        for(int i = y - 1; i <= y + 1; ++i) {
            for(int j = x - 1; j <= x + 1; ++j) {
                if (i < 20 && i >= 0 && j < 15 && j >= 0) {
                    gridArray[i][j].setExplored(true);
                }
            }
        }

    }


    public boolean isInStartPoint(int x, int y) {
        return x >= 0 && x <= 2 && y >= 17 && y <= 19;
    }

    public boolean isInGoalPoint(int x, int y) {
        return y >= 0 && y <= 2 && x >= 12 && x <= 14;
    }

    private void calculateDimension() {
        this.gridWidth = ArenaConstants.PANEL_WIDTH/ArenaConstants.NUM_COLS;
        this.gridHeight = ArenaConstants.PANEL_HEIGHT/ArenaConstants.NUM_ROWS;
    }

    private void fillGridBoxes() {
        for(int y = 0; y < ArenaConstants.NUM_ROWS; ++y) {
            for(int x = 0; x < ArenaConstants.NUM_COLS; ++x) {
                Location location = new Location(x, y);
                boolean obstaclePresence = this.locationisObstacle(location);
                GridBox gridBox = new GridBox(location, obstaclePresence);
                gridArray[y][x] = gridBox;
            }
        }

    }

    private boolean locationisObstacle(Location location) {
        Iterator var2 = this.obstacleLocation.iterator();

        Location obstacleLocation;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            obstacleLocation = (Location)var2.next();
        } while(!obstacleLocation.isSameLocation(location));

        return true;
    }

    public void paint(Graphics g) {
        this.paintGridBoxs(g);
        this.paintRobot(g);
    }

    private void paintGridBoxs(Graphics g) {
        this.robotMovement();
        Graphics2D g2 = (Graphics2D)g;

        for(int y = 0; y < ArenaConstants.NUM_ROWS; ++y) {
            for(int x = 0; x < ArenaConstants.NUM_COLS; ++x) {
                int x1 = x * this.gridWidth;
                int y1 = y * this.gridHeight;
                g2.setColor(Color.decode("#e6e6fa"));
                if (gridArray[y][x].isExplored()) {
                    g2.setColor(Color.decode("#B491C8"));
                    g2.fillRect(x1, y1, this.gridWidth, this.gridHeight);
                }

                if (gridArray[y][x].isObstacle()) {
                    g2.setColor(Color.BLACK);
                }

                if (gridArray[y][x].isObstacleNeighbour()) {
                    g2.setColor(Color.blue);
                }

                if (this.isInStartPoint(x, y)) {
                    g2.setColor(Color.decode("#40E0D0"));
                }

                if (this.isInGoalPoint(x, y)) {
                    g2.setColor(Color.decode("#F7347A"));
                }

                g2.fillRect(x1, y1, this.gridWidth, this.gridHeight);
                g2.setColor(Color.white);
                g2.drawRect(x1, y1, this.gridWidth, this.gridHeight);
                g2.setStroke(new BasicStroke(4.0F));
            }
        }

    }

    private void createGoal(int x, int y, Graphics g) {
        if (x > 11 && y < 3) {
            g.setColor(Color.decode("#F7347A"));
            g.fillRect(x * this.gridWidth, y * this.gridHeight, this.gridWidth, this.gridHeight);
        }

    }

    private void createStart(int x, int y, Graphics g) {
        if (x < 3 && y > 16) {
            g.setColor(Color.decode("#40E0D0"));
            g.fillRect(x * this.gridWidth, y * this.gridHeight, this.gridWidth, this.gridHeight);
        }

    }

    private void paintRobot(Graphics g) {
        g.setColor(Color.decode("#088DA5"));
        PrintStream var10000 = System.out;
        int var10001 = this.robot.getCurrPosX();
        var10000.println(var10001 + "," + this.robot.getCurrPosY());
        Location robotExactLocation = new Location(this.robot.getCurrPosX(), this.robot.getCurrPosY());
        int robotHeight;
        if (robotExactLocation == null) {
            Location robotGridLocation = this.robot.getCurrentGridCell().getLocation();
            robotHeight = robotGridLocation.getX() * this.gridWidth;
            int y = robotGridLocation.getY() * this.gridHeight;
            robotExactLocation = new Location(robotHeight, y);
        }

        int robotWidth = this.gridWidth * 3;
        robotHeight = this.gridHeight * 3;
        g.fillOval((robotExactLocation.getX() - 1) * this.gridWidth, (robotExactLocation.getY() - 1) * this.gridHeight, robotWidth, robotHeight);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.decode("#FF7373"));
        if (this.robot.getHeading() == Direction.NORTH) {
            g2d.fillOval((robotExactLocation.getX() - 1) * this.gridWidth + 42, robotExactLocation.getY() * this.gridHeight - 25, 10, 10);
        } else if (this.robot.getHeading() == Direction.SOUTH) {
            g2d.fillOval((robotExactLocation.getX() - 1) * this.gridWidth + 42, robotExactLocation.getY() * this.gridHeight + 25, 10, 10);
        } else if (this.robot.getHeading() == Direction.WEST) {
            g2d.fillOval((robotExactLocation.getX() - 1) * this.gridWidth + 5, robotExactLocation.getY() * this.gridHeight + 2, 10, 10);
        } else if (this.robot.getHeading() == Direction.EAST) {
            g2d.fillOval((robotExactLocation.getX() - 1) * this.gridWidth + 60, robotExactLocation.getY() * this.gridHeight + 2, 10, 10);
        }

    }

    public void repaint() {
        super.repaint();
    }

    public void initializeGrids() {
        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 15; ++col) {
                gridArray[row][col] = new GridBox(new Location(col, row), false);
            }
        }

    }

    public void initializeNeighbouringGrids() {
        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 15; ++col) {
                GridBox neighbourBelow;
                if (col > 0) {
                    neighbourBelow = gridArray[row][col - 1];
                    gridArray[row][col].setNeighbouringGridBoxes(neighbourBelow);
                    gridArray[row][col].setGridLeft(neighbourBelow);
                }

                if (col < 14) {
                    neighbourBelow = gridArray[row][col + 1];
                    gridArray[row][col].setNeighbouringGridBoxes(neighbourBelow);
                    gridArray[row][col].setGridRight(neighbourBelow);
                }

                if (row > 0) {
                    neighbourBelow = gridArray[row - 1][col];
                    gridArray[row][col].setNeighbouringGridBoxes(neighbourBelow);
                    gridArray[row][col].setGridRight(neighbourBelow);
                }

                if (row < 19) {
                    neighbourBelow = gridArray[row + 1][col];
                    gridArray[row][col].setNeighbouringGridBoxes(neighbourBelow);
                    gridArray[row][col].setGridBelow(neighbourBelow);
                }
            }
        }

    }

    public void calculateSpaceClearance() {
        GridBox grid;
        for (int row = 0; row < ArenaConstants.NUM_ROWS; row++) {
            colloop:
            for (int col = 0; col < ArenaConstants.NUM_COLS; col++) {
                grid = gridArray[row][col];
                grid.setClearStatus(false);

                if (grid.isObstacle()) {
                    continue;
                }

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (row + i >= ArenaConstants.NUM_ROWS || row + i < 0 || col + j >= ArenaConstants.NUM_COLS || col + j < 0) {
                            continue colloop;
                        }
                        if (gridArray[row + i][col + j].isObstacle())
                            continue colloop;
                    }
                }
                grid.setClearStatus(true);
            }
        }

        /*
        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 15; ++col) {
                gridArray[row][col].setClearStatus(true);
                if (row == -1 || col == -1 || row == 20 || col == 15) {
                    gridArray[row][col].setClearStatus(false);


                }
            }
        }
        */

    }

    public void calculateSpaceClearanceExploration(){
        for(int row = 0; row < ArenaConstants.NUM_ROWS; ++row) {
            for(int col = 0; col < ArenaConstants.NUM_COLS; ++col) {
                gridArray[row][col].setClearStatus(true);
                if (row == -1 || col == -1 || row == 20 || col == 15) {
                    gridArray[row][col].setClearStatus(false);
                }
            }
        }
    }

    public boolean checkValidCoordinates(int row, int col) {
        return row >= 0 && col >= 0 && row < 20 && col < 15;
    }

    public GridBox getGrid(int row, int col) {
        return gridArray[row][col];
    }

    public boolean getIsObstacleOrWall(int row, int col) {
        return !this.checkValidCoordinates(row, col) || this.getGrid(row, col).isObstacle() || !this.getGrid(row, col).isClearStatus();
    }
}
