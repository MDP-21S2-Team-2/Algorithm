package view;

import arena.ArenaConstants;

import java.awt.*;
import java.awt.event.ActionEvent;

import arena.GridBox;
import communications.TCPManager;
import navigation.Exploration;
import navigation.FastestPath;

import static arena.ArenaLoader.generateMapDescriptor;
import static arena.ArenaView.gridArray;
import static view.SimulatorGUI.arenaView;
import static communications.TCPManager.tcp;


public class EntryPoint {

    private static SimulatorGUI interfaceGUI = new SimulatorGUI();
    //public static Robot robot;
    public static boolean realRun = false;

    public static void main(String args[]){

        interfaceGUI.initializations();
        interfaceGUI.displayGUI();

        if (interfaceGUI.simulation){
            interfaceGUI.simulateRobotRuns();
        }
        else{
            interfaceGUI.executeRobotRuns();
        }
        //runExploration();
    }

    public void runExploration(){
        Exploration exp = new Exploration(60, 300, realRun);
        if (!realRun) {
            exp.runExploration();
        }else {
            tcp.establishConnection();
            while (true) {
                System.out.println("Waiting for EXP_START...");
                String packet = tcp.receivePacket();
                if (packet.equals(tcp.START_EXP)) break;
            }
            exp.runExploration();
        }
        //System.out.println(generateMapDescriptor(arenaView));
        generateMapDescriptor(arenaView);
    }

    public static void initiateRealRun (){
        tcp.establishConnection();
        while (true) {
            System.out.println("Waiting for Commands...");
            String packet = tcp.receivePacket();
            if (packet.equals(tcp.START_EXP)) break;
        }
    }
}














