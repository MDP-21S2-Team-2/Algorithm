//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package navigation;

import arena.GridBox;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import robot.Robot;

public class FastestPath {
    public GridBox startGrid;
    public GridBox endGrid;
    Robot robot;

    public FastestPath(GridBox startGrid, GridBox endGrid) {
        this.startGrid = startGrid;
        this.endGrid = endGrid;
    }

    protected static Stack<GridBox> buildPath(GridBox grid) {
        Stack path;
        for(path = new Stack(); grid.getPathParent() != null; grid = grid.getPathParent()) {
            path.push(grid);
        }

        return path;
    }

    public static Stack<GridBox> findPath(GridBox startGrid, GridBox endGrid) {
        boolean isStartGrid = true;
        FastestPath.PriorityLinkedList priorityList = new FastestPath.PriorityLinkedList();
        LinkedList pathList = new LinkedList();
        //startGrid.setPathParent((GridBox)null);
        if (startGrid.getPathParent() != null)
        startGrid.setCostAccumulatedFromStart(0.0F);
        startGrid.setCostEstimatedToGoal(startGrid.estimateCostToGrid(endGrid));
        priorityList.add(startGrid);

        while(!priorityList.isEmpty()) {
            GridBox grid = (GridBox)priorityList.removeFirst();
            isStartGrid = (grid == startGrid);
            if (grid.getPathParent() == null) {
                grid.setRobotFacingDirection(Direction.NORTH);
                System.out.println(grid.getRobotFacingDirection());
            }
            else if (isStartGrid){
                grid.setRobotFacingDirection();
                System.out.println(grid.getRobotFacingDirection());
                startGrid.setPathParent(null);
                grid.setPathParent(null);
            }

            if (grid == endGrid) {
                return buildPath(grid);
            }

            List neighbouringGrids = grid.getNeighbouringGridBoxes();

            for(int i = 0; i < neighbouringGrids.size(); ++i) {
                GridBox neighbourGrid = (GridBox)neighbouringGrids.get(i);
                boolean onPriorityList = priorityList.contains(neighbourGrid);
                boolean onPathList = pathList.contains(neighbourGrid);
                boolean isObstacle = neighbourGrid.isObstacle();
                boolean spaceClearance = neighbourGrid.isClearStatus();
                float costAccumulatedFromStart = grid.getCost(neighbourGrid, endGrid, isStartGrid) + 1.0F;
                if (!onPriorityList && !onPathList || costAccumulatedFromStart < neighbourGrid.getCostAccumulatedFromStart()) {
                    neighbourGrid.setPathParent(grid);
                    neighbourGrid.setCostAccumulatedFromStart(costAccumulatedFromStart);
                    neighbourGrid.setCostEstimatedToGoal(neighbourGrid.estimateCostToGrid(endGrid));
                    if (!onPriorityList && !isObstacle && spaceClearance) {
                        priorityList.add(neighbourGrid);
                    }
                }
            }

            pathList.add(grid);
        }

        System.out.println("Null data, no fastest path");
        return new Stack();
    }

    public Stack<GridBox> findFastestPath() {
        return findPath(this.startGrid, this.endGrid);
    }

    public static class PriorityLinkedList extends LinkedList {
        public PriorityLinkedList() {
        }

        public void add(Comparable o) {
            for(int i = 0; i < this.size(); ++i) {
                if (o.compareTo(this.get(i)) <= 0) {
                    this.add(i, o);
                    return;
                }
            }

            this.addLast(o);
        }
    }
}
