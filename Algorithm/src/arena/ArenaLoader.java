//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package arena;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ArenaLoader {
    public ArenaLoader() {
    }

    public static void loadMapFromDisk(ArenaView arena, String filename) {
        try {
            InputStream inputStream = new FileInputStream("Maps/" + filename + ".txt");
            BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));
            String line = buf.readLine();

            StringBuilder sb;
            for(sb = new StringBuilder(); line != null; line = buf.readLine()) {
                sb.append(line);
            }

            String bin = sb.toString();
            int binPtr = 0;

            for(int row = 0; row <= 19; row++) {
                for(int col = 0; col < 15; ++col) {
                    if (bin.charAt(binPtr) == '1') {
                        //arena.getGrid(row, col).setObstacle(true);
                        arena.simulatedObstacleGrids[row][col] =1;
                    }
                    ++binPtr;
                }
            }

            arena.setAllUnexplored();
        } catch (IOException var10) {
            var10.printStackTrace();
        }

    }

    public static String explorationMapAndroid(ArenaView arena){
        StringBuilder exploredMapGeneration = new StringBuilder();
        StringBuilder currentExpoMap = new StringBuilder();
        String exploredMapAD = new String();
        String exploredMapBinary = new String();

        for(int row = 19; row >= 0; --row) {
            for(int col = 0; col < 15; ++col) {
                if (arena.getGrid(row, col).isExplored()) {
                    if (arena.getGrid(row, col).isObstacle()) {
                        exploredMapGeneration.append("1");
                    } else {
                        exploredMapGeneration.append("0");
                    }
                }
                else{
                    exploredMapGeneration.append("0");
                }
                if (exploredMapGeneration.length() == 4) {
                    currentExpoMap.append(binToHex(exploredMapGeneration.toString()));
                    exploredMapGeneration.setLength(0);
                }
            }
        }

        if (exploredMapGeneration.length() > 0) {
            currentExpoMap.append(binToHex(exploredMapGeneration.toString()));
        }
        exploredMapAD = currentExpoMap.toString();
        exploredMapBinary = hextoBin(exploredMapAD);

        return exploredMapBinary;
//        StringBuilder exploredMapGeneration = new StringBuilder();
//        String exploredMapAD = new String();
//
//
//        for(int row = 0; row < 20; ++row) {
//            for (int col = 0; col < 15; ++col) {
//                if (arena.getGrid(row, col).isExplored()) {
//                    if (arena.getGrid(row, col).isObstacle()) {
//                        exploredMapGeneration.append("1");
//                    } else {
//                        exploredMapGeneration.append("0");
//                    }
//                }
//                else{
//                    exploredMapGeneration.append("0");
//                }
//            }
//        }
//        exploredMapGeneration.reverse();
//        exploredMapAD = exploredMapGeneration.toString();
//        return exploredMapAD;
    }

    private static String binToHex(String bin) {
        int dec = Integer.parseInt(bin, 2);
        return Integer.toHexString(dec);
    }

    public static String hextoBin(String hex) {
        String binary = "";
        hex = hex.toUpperCase();
        HashMap<Character, String> hashMap = new HashMap();
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");

        for(int i = 0; i < hex.length(); ++i) {
            char c = hex.charAt(i);
            if (!hashMap.containsKey(c)) {
                String error = "Invalid Hexadecimal String";
                return error;
            }

            binary = binary + (String)hashMap.get(c);
        }

        return binary;
    }

    public static void createArenaHex(String p1, String p2, ArenaView arena) {
        String binary1 = hextoBin(p1);
        String binary2 = hextoBin(p2);
        int binPtr1 = 0;

        int binPtr2;
        int row1;
        for(binPtr2 = 19; binPtr2 >= 0; --binPtr2) {
            for(row1 = 0; row1 < 15; ++row1) {
                if (binary1.charAt(binPtr1) == '1') {
                }

                ++binPtr1;
            }
        }

        binPtr2 = 0;

        for(row1 = 19; row1 >= 0; --row1) {
            for(int col1 = 0; col1 < 15; ++col1) {
                if (binary2.charAt(binPtr2) == '1') {
                    arena.getGrid(row1, col1).setObstacle(true);
                }

                ++binPtr2;
            }
        }

        arena.setAllUnexplored();
    }

    public static String[] generateMapDescriptor(ArenaView arena) {
        String[] ret = new String[2];
        StringBuilder Part1 = new StringBuilder();
        StringBuilder Part1_bin = new StringBuilder();
        Part1_bin.append("11");

        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 15; ++col) {
                if (arena.getGrid(row, col).isExplored()) {
                    Part1_bin.append("1");
                } else {
                    Part1_bin.append("0");
                }

                if (Part1_bin.length() == 4) {
                    Part1.append(binToHex(Part1_bin.toString()));
                    Part1_bin.setLength(0);
                }
            }
        }

        Part1_bin.append("11");
        Part1.append(binToHex(Part1_bin.toString()));
        System.out.println("P1: " + Part1.toString());
        ret[0] = Part1.toString();
        StringBuilder Part2 = new StringBuilder();
        StringBuilder Part2_bin = new StringBuilder();

        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 15; ++col) {
                if (arena.getGrid(row, col).isExplored()) {
                    if (arena.getGrid(row, col).isObstacle()) {
                        Part2_bin.append("1");
                    } else {
                        Part2_bin.append("0");
                    }

                    if (Part2_bin.length() == 4) {
                        Part2.append(binToHex(Part2_bin.toString()));
                        Part2_bin.setLength(0);
                    }
                }
            }
        }

        if (Part2_bin.length() > 0) {
            Part2.append(binToHex(Part2_bin.toString()));
        }

        System.out.println("P2: " + Part2.toString());
        ret[1] = Part2.toString();
        return ret;
    }
}
