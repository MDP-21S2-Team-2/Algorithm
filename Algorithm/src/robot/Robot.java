package robot;

import arena.ArenaView;
import arena.GridBox;
import communications.TCPConstants;
import communications.TCPManager;
import navigation.Direction;
import navigation.Location;
import view.SimulatorGUI;

import javax.swing.*;
import java.util.Optional;
import java.util.Stack;

import static communications.TCPConstants.*;
import static communications.TCPManager.tcp;
import static java.lang.Float.parseFloat;
import static view.SimulatorGUI.arenaView;
import static arena.ArenaLoader.explorationMapAndroid;

public class Robot {
    private GridBox currentGridBox = new GridBox(new Location(RobotConstants.START_POSX, RobotConstants.START_POSY),false);
    private int currPosX = RobotConstants.START_POSX;
    private int currPosY = RobotConstants.START_POSY;

    private Location exactLocation;
    private Direction facingDirection = RobotConstants.INITIAL_FACING_DIRECTION;

    private Sensor BLSensor = new Sensor(RobotConstants.START_POSX-1,RobotConstants.START_POSY,Sensor.SensorLocation.BOTTOM_LEFT, Sensor.SensorRange.SHORT,Direction.WEST);
    private Sensor LSensor = new Sensor(RobotConstants.START_POSX-1,RobotConstants.START_POSY-1,Sensor.SensorLocation.LEFT, Sensor.SensorRange.SHORT,Direction.WEST);
    private Sensor LFSensor = new Sensor(RobotConstants.START_POSX-1,RobotConstants.START_POSY-1,Sensor.SensorLocation.LEFT_FRONT, Sensor.SensorRange.SHORT,Direction.NORTH);
    private Sensor MFSensor = new Sensor(RobotConstants.START_POSX,RobotConstants.START_POSY-1,Sensor.SensorLocation.MIDDLE_FRONT, Sensor.SensorRange.SHORT,Direction.NORTH);
    private Sensor RFSensor = new Sensor(RobotConstants.START_POSX+1,RobotConstants.START_POSY-1,Sensor.SensorLocation.RIGHT_FRONT, Sensor.SensorRange.SHORT,Direction.NORTH);
    private Sensor RSensor =  new Sensor(RobotConstants.START_POSX+1,RobotConstants.START_POSY-1,Sensor.SensorLocation.RIGHT, Sensor.SensorRange.LONG,Direction.EAST);

    private boolean simulatedRun;

    public Robot(boolean simulatedRun) {
        this.simulatedRun = simulatedRun;
    }

    public void remote (Command command,boolean realRun){//}, Optional<Integer> steps) {
        //int s = steps.isPresent() ? steps.get() : 0;
        int delay = 100;
        if (command == Command.FORWARD) {
            move(command,1);
            if (realRun)
                tcp.sendPacket(SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + 0);
                //tcp.receiveBytesArduino();
                //sendArduinoMultipleForward(1,100);
        }
        else{
            rotate(command);
            if (realRun) {
                expSendArduinoRotate(command, 100);
            }
        }
        setSensors();
        sense();
        //arenaView.repaint();
        if (realRun)
            tcp.sendPacket(tcp.updateAndroidExploration("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX()) + "MDF," + explorationMapAndroid(arenaView));
    }


    public void move(Command command, int steps) {
        int s = (command == Command.FORWARD) ? steps : -steps;

        switch (this.facingDirection){
            case NORTH:
                currPosY -= s;
                break;
            case SOUTH:
                currPosY += s;
                break;
            case EAST:
                currPosX += s;
                break;
            case WEST:
                currPosX -= s;
                break;
        }
        arenaView.repaint();
    }

    //Depending on command left or right that is passed in, this.facingDirection will change
    public void rotate(Command command) {
        if (command == Command.RIGHT) {
            switch (this.facingDirection){
                case NORTH:
                    this. facingDirection = Direction.EAST;
                    break;
                case EAST:
                    this.facingDirection = Direction.SOUTH;
                    break;
                case SOUTH:
                    this.facingDirection = Direction.WEST;
                    break;
                case WEST:
                    this.facingDirection = Direction.NORTH;
            }
        }
        else if (command == Command.LEFT){
            switch (this.facingDirection){
                case NORTH:
                    this. facingDirection = Direction.WEST;
                    break;
                case EAST:
                    this.facingDirection = Direction.NORTH;
                    break;
                case SOUTH:
                    this.facingDirection = Direction.EAST;
                    break;
                case WEST:
                    this.facingDirection = Direction.SOUTH;
            }
        }
        else {
            switch (this.facingDirection){
                case NORTH:
                    this. facingDirection = Direction.SOUTH;
                    break;
                case EAST:
                    this.facingDirection = Direction.WEST;
                    break;
                case SOUTH:
                    this.facingDirection = Direction.NORTH;
                    break;
                case WEST:
                    this.facingDirection = Direction.EAST;
            }
        }
    }

    public void calibrate(){}

    public GridBox getCurrentGridCell() {
        return currentGridBox;
    }

    public Location getExactLocation() {
        //exactLocation.setX(currPosX);
        //exactLocation.setY(currPosY);
        return exactLocation;
    }

    public void senseFront() {

    }

    public void senseLeft() {

    }

    public void senseRight() {

    }

    public Direction getHeading() {
        return facingDirection;
    }

    public void simulateFastestPath(Stack<GridBox> path1, Stack<GridBox> path2 ){
        arenaView.generateFastestPathObstacles();
        arenaView.repaint();
        while (!path1.empty() ||!path2.empty() ){
            GridBox grid;
            if (!path1.empty())
                grid = path1.pop();
            else
                grid = path2.pop();
            try{
                Thread.sleep(100);
                if (grid.getX() > currPosX){
                    while(facingDirection != Direction.EAST){
                        if (facingDirection != Direction.SOUTH) {
                            this.rotate(Command.RIGHT);
                            System.out.println(Command.RIGHT);
                        }else {
                            this.rotate(Command.LEFT);
                            System.out.println(Command.LEFT);
                        }
                    }

                    move(Command.FORWARD,1);
                    System.out.println(Command.FORWARD);
                }
                else if (grid.getX()<currPosX){
                    while(facingDirection != Direction.WEST) {
                        if (facingDirection != Direction.SOUTH) {
                            this.rotate(Command.LEFT);
                            System.out.println(Command.LEFT);
                        } else{
                            this.rotate(Command.RIGHT);
                            System.out.println(Command.RIGHT);
                        }
                    }
                    move(Command.FORWARD,1);
                    System.out.println(Command.FORWARD);
                }
                else if (grid.getY()<currPosY){
                    while(facingDirection != Direction.NORTH){
                        if (facingDirection != Direction.WEST) {
                            this.rotate(Command.LEFT);
                            System.out.println(Command.LEFT);
                        } else{
                            this.rotate(Command.RIGHT);
                            System.out.println(Command.RIGHT);
                        }
                    }
                    move(Command.FORWARD,1);
                    System.out.println(Command.FORWARD);
                }
                else {
                    while(facingDirection != Direction.SOUTH){
                        if (facingDirection != Direction.EAST) {
                            this.rotate(Command.LEFT);
                            System.out.println(Command.LEFT);
                        } else{
                            this.rotate(Command.RIGHT);
                            System.out.println(Command.RIGHT);
                        }
                    }
                    move(Command.FORWARD,1);
                    System.out.println(Command.FORWARD);
                }
                arenaView.gridArray[this.currPosY][this.currPosX].setExplored(true);
                arenaView.repaint();
                System.out.println(this.currPosX+ ","+this.currPosY);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void executeFastestPath(Stack<GridBox> path1, Stack<GridBox> path2 )  {
        int delay = 0;
//        tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//        tcp.receivePacket();
        String outputPacket;
        int forwardCount = 0;

        while (!path1.isEmpty()||!path2.empty()){
                GridBox grid;
                if (!path1.empty())
                    grid = path1.pop();
                else
                    grid = path2.pop();

                if (grid.getX() > currPosX){
                    while(facingDirection != Direction.EAST) {
                        System.out.println(forwardCount);
                        forwardCount = sendArduinoMultipleForward(forwardCount,delay);
                        if (facingDirection != Direction.SOUTH) {
                            this.rotate(Command.RIGHT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_RIGHT;
                        } else{
                            this.rotate(Command.LEFT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_LEFT;
                        }
                        try{
                            Thread.sleep(delay);
                        tcp.sendPacket(outputPacket);
                        tcp.receivePacket();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        try{
                            Thread.sleep(delay);
                        tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
                        tcp.receivePacket();}catch (InterruptedException e){
                            e.printStackTrace();}
                    }
                    move(Command.FORWARD,1);
                    forwardCount += 1;
                    //outputPacket = SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + "1";
                }
                else if (grid.getX()<currPosX){
                    while(facingDirection != Direction.WEST) {
                        forwardCount = sendArduinoMultipleForward(forwardCount,delay);
                        if (facingDirection != Direction.SOUTH) {
                            this.rotate(Command.LEFT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_LEFT;
                        } else{
                            this.rotate(Command.RIGHT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_RIGHT;
                        }
                        try{
                            Thread.sleep(delay);
                        tcp.sendPacket(outputPacket);
                        tcp.receivePacket();}catch (InterruptedException e){
                        e.printStackTrace();}
                        try{
                            Thread.sleep(delay);
                        tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
                        tcp.receivePacket();}catch (InterruptedException e){
                                e.printStackTrace();}
                    }
                    move(Command.FORWARD,1);
                    forwardCount += 1;
                    //outputPacket = SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + "1";
                }
                else if (grid.getY()<currPosY){
                    while(facingDirection != Direction.NORTH) {
                        forwardCount = sendArduinoMultipleForward(forwardCount,delay);
                        if (facingDirection != Direction.WEST) {
                            this.rotate(Command.LEFT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_LEFT;
                        } else{
                            this.rotate(Command.RIGHT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_RIGHT;
                        }
                        try{
                            Thread.sleep(delay);
                        tcp.sendPacket(outputPacket);
                        tcp.receivePacket();;}catch (InterruptedException e){
                            e.printStackTrace();}
                        try{
                            Thread.sleep(delay);
                        tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
                        tcp.receivePacket();}catch (InterruptedException e){
                            e.printStackTrace();}
                    }
                    move(Command.FORWARD,1);
                    forwardCount += 1;
                    System.out.println("Forward count increased");
                    //outputPacket = SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + "1";
                }
                else {
                    while(facingDirection != Direction.SOUTH) {
                        forwardCount = sendArduinoMultipleForward(forwardCount,delay);
                        if (facingDirection != Direction.EAST) {
                            this.rotate(Command.LEFT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_LEFT;
                        } else{
                            this.rotate(Command.RIGHT);
                            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_RIGHT;
                        }
                        try{
                            Thread.sleep(delay);
                        tcp.sendPacket(outputPacket);
                        tcp.receivePacket();
                        }catch (InterruptedException e){
                            e.printStackTrace();}
                        try{
                            Thread.sleep(delay);
                        tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
                        tcp.receivePacket();}catch (InterruptedException e){
                            e.printStackTrace();}

                    }
                    move(Command.FORWARD,1);
                    forwardCount += 1;
                    //outputPacket = SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + "1";
                }
                arenaView.repaint();
//            try{
//                Thread.sleep(delay);
//                tcp.sendPacket(outputPacket);
//                tcp.receivePacket();
//            }catch (InterruptedException e){
//                e.printStackTrace();}
//            try{
//                Thread.sleep(delay);
//                tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//                tcp.receivePacket();
//                }catch (InterruptedException e){
//                e.printStackTrace();}
        }
        sendArduinoMultipleForward(forwardCount,delay);
//        try{
//                Thread.sleep(delay);
//                tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//                tcp.receivePacket();
//                }catch (InterruptedException e){
//                e.printStackTrace();}
//
//        outputPacket = SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + "6";
//        try{
//            Thread.sleep(delay);
//        tcp.sendPacket(outputPacket);
//        tcp.receivePacket();}catch (InterruptedException e){
//            e.printStackTrace();}
//        try{
//            Thread.sleep(delay);
//            tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//            tcp.receivePacket();
//        }catch (InterruptedException e){
//            e.printStackTrace();}
//        outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_LEFT;
//        try{
//            Thread.sleep(delay);
//            tcp.sendPacket(outputPacket);
//            tcp.receivePacket();}catch (InterruptedException e){
//            e.printStackTrace();}
//        try{
//            Thread.sleep(delay);
//            tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//            tcp.receivePacket();
//        }catch (InterruptedException e){
//            e.printStackTrace();}
//
//        try{
//            Thread.sleep(delay);
//            tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//            tcp.receivePacket();
//        }catch (InterruptedException e){
//            e.printStackTrace();}
//        outputPacket = SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + "3";
    }

    public int sendArduinoMultipleForward (int forwardCount, int delay){
        if (forwardCount >= 1){
            String outputPacket;
            if (forwardCount <= 10)
                outputPacket = SEND_ARDUINO + SEPARATOR + MOVE_FORWARD + (forwardCount-1);
            else
                outputPacket = SEND_ARDUINO + SEPARATOR + "F" + (forwardCount%10-1);
            try{
                Thread.sleep(delay);
                tcp.sendPacket(outputPacket);
                tcp.receivePacket();
            }catch (InterruptedException e){
                e.printStackTrace();}
            try{
                Thread.sleep(delay);
                tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
                tcp.receivePacket();
            }catch (InterruptedException e){
                e.printStackTrace();}
        }
        else {
            System.out.println("Forward ignored");
        }
        return 0;
    }

    public void sendArduinoRotate(Command command, int delay) {
        String outputPacket;
        if (command == Command.RIGHT) {
            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_RIGHT;
        }
        else if (command == Command.LEFT){
            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_LEFT;
        }
        else {
            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_180;
        }
        try{
            Thread.sleep(delay);
            tcp.sendPacket(outputPacket);
            //tcp.receivePacket();
        }catch (InterruptedException e){
            e.printStackTrace();}
//        try{
//            Thread.sleep(delay);
//            tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//            tcp.receivePacket();
//        }catch (InterruptedException e){
//            e.printStackTrace();}
    }

    public void expSendArduinoRotate(Command command, int delay) {
        String outputPacket;
        if (command == Command.RIGHT) {
            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_RIGHT;
        }
        else if (command == Command.LEFT){
            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_LEFT;
        }
        else {
            outputPacket = SEND_ARDUINO + SEPARATOR + ROTATE_180;
        }
        try{
            Thread.sleep(delay);
            tcp.sendPacket(outputPacket);
            //tcp.receiveBytesArduino();
        }catch (InterruptedException e){
            e.printStackTrace();}
//        try{
//            Thread.sleep(delay);
//            tcp.updateAndroid("RUNNING",this.getFacingDirection(),this.getCurrPosY(),this.getCurrPosX());
//            tcp.receivePacket();
//        }catch (InterruptedException e){
//            e.printStackTrace();}
    }

    public int getCurrPosX() {
        return currPosX;
    }

    public int getCurrPosY() {
        return currPosY;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    public void setSensors() {
        switch (facingDirection) {
            case NORTH:
                BLSensor.setSensor(this.currPosY , this.currPosX - 1, Direction.WEST);
                LSensor.setSensor(this.currPosY - 1, this.currPosX - 1, Direction.WEST);
                LFSensor.setSensor(this.currPosY - 1, this.currPosX - 1, this.facingDirection);
                MFSensor.setSensor(this.currPosY - 1, this.currPosX , this.facingDirection);
                RFSensor.setSensor(this.currPosY -1, this.currPosX + 1, this.facingDirection);
                RSensor.setSensor(this.currPosY - 1, this.currPosX + 1, Direction.EAST);
                break;
            case EAST:
                BLSensor.setSensor(this.currPosY - 1, this.currPosX , Direction.NORTH);
                LSensor.setSensor(this.currPosY - 1, this.currPosX + 1, Direction.NORTH);
                LFSensor.setSensor(this.currPosY - 1, this.currPosX + 1, this.facingDirection);
                MFSensor.setSensor(this.currPosY, this.currPosX + 1, this.facingDirection);
                RFSensor.setSensor(this.currPosY + 1, this.currPosX + 1, this.facingDirection);
                RSensor.setSensor(this.currPosY + 1, this.currPosX + 1, Direction.SOUTH);
                break;
            case SOUTH:
                BLSensor.setSensor(this.currPosY , this.currPosX + 1, Direction.EAST);
                LSensor.setSensor(this.currPosY + 1, this.currPosX + 1, Direction.EAST);
                LFSensor.setSensor(this.currPosY + 1, this.currPosX + 1, this.facingDirection);
                MFSensor.setSensor(this.currPosY + 1, this.currPosX , this.facingDirection);
                RFSensor.setSensor(this.currPosY + 1, this.currPosX - 1, this.facingDirection);
                RSensor.setSensor(this.currPosY + 1 , this.currPosX - 1, Direction.WEST);
                break;
            case WEST:
                BLSensor.setSensor(this.currPosY + 1, this.currPosX, Direction.SOUTH);
                LSensor.setSensor(this.currPosY + 1, this.currPosX - 1, Direction.SOUTH);
                LFSensor.setSensor(this.currPosY + 1, this.currPosX - 1, this.facingDirection);
                MFSensor.setSensor(this.currPosY, this.currPosX - 1, this.facingDirection);
                RFSensor.setSensor(this.currPosY - 1, this.currPosX - 1, this.facingDirection);
                RSensor.setSensor(this.currPosY - 1, this.currPosX - 1, Direction.NORTH);
                break;
        }
    }

    public boolean sense() {
        boolean[] result = new boolean[6];

        if (simulatedRun) {
            System.out.println("Inside simulation loop");
            result[0] = BLSensor.senseArena();
            result[1] = LSensor.senseArena();
            result[2] = LFSensor.senseArena();
            result[3] = MFSensor.senseArena();
            result[4] = RFSensor.senseArena();
            result[5] = RSensor.senseArena();
            //System.out.print("Inside 1st If Loop");
        } else {
            System.out.println("inside else loop");
            boolean getData = true;
            while (getData) {
                System.out.println("Inside while loop, waiting to receive sensor data");
                String msg = tcp.receivePacket();
                System.out.println(msg);
                String[] msgArr = msg.split(",");
                System.out.println(msgArr[0]);
                if (msgArr[0].contains(IR_SENSORS)) {
                    getData = false;
                    for (int i = 1; i <= 6; i++) {
//                        System.out.println("Msg"+ i+ " " + msgArr[i]);
//                        byte[] arr = msgArr[i].getBytes();
//                        int asInt1 = (arr[0] & 0xFF) | ((arr[1] & 0xFF) << 8) | ((arr[2] & 0xFF) << 16) | ((arr[3] & 0xFF) << 24);
//                        int asInt = arr[0] << 24 | (arr[1] & 0xFF) << 16 | (arr[2] & 0xFF) << 8 | (arr[3] & 0xFF);
//                        float sensorValue = Float.intBitsToFloat(asInt);
//                        float sensorValue1 = Float.intBitsToFloat(asInt1);
//                        System.out.printf("Printing Sensor Value: %.2f\n", sensorValue);
//                        System.out.printf("Printing Sensor Value 1: %.2f\n", sensorValue1);
//                        realResult[i - 1] = sensorValue;
                        System.out.println(msgArr[i]);
                    }
                    BLSensor.senseRealArena(parseFloat(msgArr[1]));
                    LSensor.senseRealArena(parseFloat(msgArr[2]));
                    LFSensor.senseRealArena(parseFloat(msgArr[3]));
                    MFSensor.senseRealArena(parseFloat(msgArr[4]));
                    RFSensor.senseRealArena(parseFloat(msgArr[5]));
                    RSensor.senseRealArena(parseFloat(msgArr[6]));
                    System.out.println("Finished Sensed");
                    return true;
                }
            }
//            if (msgArr[0].equals(IR_SENSORS)) {
//                for (int i = 1; i<= 6; i++){
//                    byte[] arr = msgArr[i].getBytes();
//                    int asInt = (arr[0]&0xFF) | ((arr[1]&0xFF)<<0) | ((arr[2]&0xFF)<<16)| ((arr[3]&0xFF)<<24);
//                    float sensorValue = Float.intBitsToFloat(asInt);
//                    realResult[i-1] = sensorValue;
//                    System.out.println("Result is" + realResult[i-1]);
//
//                }

//                realResult[0] = Double.parseDouble(msgArr[1]);
//                realResult[1] = Double.parseDouble(msgArr[2]);
//                realResult[2] = Double.parseDouble(msgArr[3]);
//                realResult[3] = Double.parseDouble(msgArr[4]);
//                realResult[4] = Double.parseDouble(msgArr[5]);
//                realResult[5] = Double.parseDouble(msgArr[6]);
            }
//            else {
//                    System.out.println("Result is (Else)");
//                return false;
//            }
//
//            BLSensor.senseRealArena(realResult[0]);
//            LSensor.senseRealArena(realResult[1]);
//            LFSensor.senseRealArena(realResult[2]);
//            MFSensor.senseRealArena(realResult[3]);
//            RFSensor.senseRealArena(realResult[4]);
//            RSensor.senseRealArena(realResult[5]);

            //String[] mapStrings = MapDescriptor.generateMapDescriptor(exploredArena);
            //comm.sendMsg(mapStrings[0] + " " + mapStrings[1], CommMgr.MAP_STRINGS);
        //}

        return false;
    }

    public boolean isSimulatedRun() {
        return simulatedRun;
    }

    public void setSimulatedRun(boolean simulatedRun) {
        this.simulatedRun = simulatedRun;
    }
}
