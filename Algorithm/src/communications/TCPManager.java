//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package communications;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import navigation.Direction;
import view.SimulatorGUI;

import static arena.ArenaLoader.hextoBin;
import static communications.TCPConstants.*;


public class TCPManager {
    public static final String START_FP = "FP";
    public static final String START_EXP = "EXP";
    public static final String START_IMG = "IMG";
    public static final String SET_WP = "WAYPOINT";
    public static final String SET_MDF = "MDF";
    public static Socket socket = null;
    public static TCPManager tcp = new TCPManager();
    public BufferedWriter wb;
    public BufferedReader rb;

    public TCPManager() {
    }

    public void establishConnection() {
        System.out.println("Opening TCP/IP connection...");

        try {
            String HOST = "192.168.2.2";
            int PORT = 8080;
            socket = new Socket(HOST, PORT);
            this.wb = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream())));
            this.rb = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("openConnection() --> Connection established successfully!");
            return;
        } catch (UnknownHostException var3) {
            System.out.println("openConnection() --> UnknownHostException");
        } catch (IOException var4) {
            System.out.println("openConnection() --> IOException");
        } catch (Exception var5) {
            System.out.println("openConnection() --> Exception");
            System.out.println(var5.toString());
        }

        System.out.println("Error in opening connection!");
    }

    public void disconnect() {
        System.out.println("Closing TCP/IP connection...");

        try {
            this.rb.close();
            if (socket != null) {
                socket.close();
                socket = null;
            }

            System.out.println("Connection closed!");
        } catch (IOException var2) {
            System.out.println("closeConnection() --> IOException");
        } catch (NullPointerException var3) {
            System.out.println("closeConnection() --> NullPointerException");
        } catch (Exception var4) {
            System.out.println("closeConnection() --> Exception");
            System.out.println(var4.toString());
        }

    }

    public void sendPacket(String outputPacket) {
        System.out.println("Sending a TCP packet...");

        try {
            System.out.println("Sending out packet:\n" + outputPacket);
            this.wb.write(outputPacket + "\n");
            this.wb.flush();
        } catch (IOException var3) {
            System.out.println("sendPacket(), IOException");
        } catch (Exception var4) {
            System.out.println("sendPacket(), Exception");
            System.out.println(var4.toString());
        }

        //while (receivePacket() != "ACK");

    }

    public String receivePacket() {
        System.out.println("Receiving a TCP packet...");

        try {
            StringBuilder sb = new StringBuilder();
            String inputPacket = this.rb.readLine();
            if (inputPacket != null && inputPacket.length() > 0) {
                sb.append(inputPacket);
                System.out.println(sb.toString());
                return sb.toString();
            }
        } catch (IOException var3) {
            System.out.println("receivePacket(), IOException");
        } catch (Exception var4) {
            System.out.println("receivePacket(), Exception");
            System.out.println(var4.toString());
        }

        return null;
    }

    public void updateAndroid(String state, Direction direction, int row, int col) {
        row = Math.abs(row - 19);
        String outputPacket = "AN-ROBOT," + state + CSEPARATOR;
        switch(direction) {
            case NORTH:
                outputPacket = outputPacket + "0,";
                break;
            case SOUTH:
                outputPacket = outputPacket + "180,";
                break;
            case WEST:
                outputPacket = outputPacket + "270,";
                break;
            case EAST:
                outputPacket = outputPacket + "90,";
        }

        outputPacket = outputPacket + Integer.toString(col) + ":" + Integer.toString(row) + ";";
        //outputPacket += "MDF,000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000;IMAGE";
        tcp.sendPacket(outputPacket);
        //tcp.receivePacket();
    }

    public String updateAndroidExploration(String state, Direction direction, int row, int col) {
        row = Math.abs(row - 19);
        String outputPacket = "AN-ROBOT," + state + CSEPARATOR;
        switch(direction) {
            case NORTH:
                outputPacket = outputPacket + "0,";
                break;
            case SOUTH:
                outputPacket = outputPacket + "180,";
                break;
            case WEST:
                outputPacket = outputPacket + "270,";
                break;
            case EAST:
                outputPacket = outputPacket + "90,";
        }

        outputPacket = outputPacket + Integer.toString(col) + ":" + Integer.toString(row) + ";";
        return outputPacket;
        //tcp.receivePacket();
    }

    public void sendMDFAndroid (String P2){
        System.out.println("Map send to Android");
        String binary1 = hextoBin(P2);
        String outputMDF = "AN-"+"MDF," + binary1;
        tcp.sendPacket(outputMDF);
    }

    public void checkNForwardPacket(String packet){
        if (packet.equals(INITIAL_CALIBRATE) || packet.equals(ROTATE_RIGHT) || packet.equals(ROTATE_LEFT)){
            sendPacket(SEND_ARDUINO+SEPARATOR+packet);
            //receivePacket();
        }
        else if (packet.equals(MOVE_FORWARD)){
            sendPacket(SEND_ARDUINO+SEPARATOR+MOVE_FORWARD+"0");
            //receivePacket();
        }
    }

    public void setWayPoint(String packet1){
        //String waypointMsg = tcp.receivePacket();
        String [] wayPointArr = packet1.split(",");
        if (wayPointArr[0].equals(SET_WP)){
            String [] wayPointArray = wayPointArr[1].split(":");
            SimulatorGUI._waypointX = Integer.parseInt(wayPointArray[0]);
            SimulatorGUI._waypointY = Integer.parseInt(wayPointArray[1]);
        }
    }

    public String receiveBytesArduino (){
        System.out.println("Receiving a TCP packet...");

        try {
            StringBuilder sb = new StringBuilder();
            String inputPacket = this.rb.readLine();
            if (inputPacket != null && inputPacket.length() > 0) {
                return inputPacket;
            }
        } catch (IOException var3) {
            System.out.println("receivePacket(), IOException");
        } catch (Exception var4) {
            System.out.println("receivePacket(), Exception");
            System.out.println(var4.toString());
        }

        return null;
    }


}
