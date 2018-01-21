import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class SchBuilder {
    static int MOSCount = 0;
    static int permanentMOSCount = 0;
    static int yCoordDefault = -144;
    static int xCoordDefault = -16;
    static int columns = 6;
    static int rows = 1;
    static PrintWriter writer;
    static ArrayList<String> voltageSources = new ArrayList<String>();
    public static void incrementMOSCount() {
        permanentMOSCount++;
        MOSCount++;
        if (MOSCount >= columns) {
            yCoordDefault*=3;
            MOSCount%=columns;
            rows+=200;
        }
    }
    public static void writePullDownNetwork() throws FileNotFoundException {
        Scanner scan = new Scanner(new File("./SchematicBuilder/src/Output2.net"));
        while (scan.hasNextLine()) {
            String currentLine = scan.nextLine();
            Scanner lineScanner = new Scanner(currentLine);
            String[] componentValues = new String[6];
            for (int i = 0; i < componentValues.length; i++) {
                if (lineScanner.hasNext()) {
                    componentValues[i] = lineScanner.next();
                }
            }
            writer.println("SYMBOL " + componentValues[5] + " " + symCoords()[0] + " " + symCoords()[1] + " R0");
            writer.println("SYMATTR InstName MOS" + permanentMOSCount);
            writer.println("SYMATTR Value " + componentValues[5]);
            writer.println("FLAG " + flag1Coords()[0] + " " + flag1Coords()[1] + " " + componentValues[1]);
            writer.println("FLAG " + flag2Coords()[0] + " " + flag2Coords()[1] + " " + componentValues[2]);
            writer.println("FLAG " + flag3Coords()[0] + " " + flag3Coords()[1] + " " + componentValues[3]);
            writer.println();
            incrementMOSCount();
            checkVoltageSource(componentValues[2]);
            //MOSCount++;
        }
        addVoltageSources();
    }

    public static void checkVoltageSource(String candidate) {
        if (!voltageSources.contains(candidate)) {
            voltageSources.add(candidate);
        }
    }

    public static void addVoltageSources() {
        for (int i = 0; i < voltageSources.size(); i++) {
            writer.println("FLAG " + flag4Coords()[0] + " " + flag4Coords()[1] + " " + voltageSources.get(i));
            writer.println("FLAG " + flag3Coords()[0] + " " + flag3Coords()[1] + " 0");
            writer.println("SYMBOL voltage " + flag1Coords()[0] + " " + flag1Coords()[1] + " R0");
            writer.println("SYMATTR InstName V" + (i+1));
            writer.println("SYMATTR Value 10");
            incrementMOSCount();
        }
        writer.println("FLAG " + flag4Coords()[0] + " " + flag4Coords()[1] + " Vdd");
        writer.println("FLAG " + flag3Coords()[0] + " " + flag3Coords()[1] + " 0");
        writer.println("SYMBOL voltage " + flag1Coords()[0] + " " + flag1Coords()[1] + " R0");
        writer.println("SYMATTR InstName Vdd");
        writer.println("SYMATTR Value PULSE(0 10 6m 0 0 3m)");
        incrementMOSCount();
        writer.println("TEXT " + flag1Coords()[0] + " " + flag1Coords()[1] + " Left 2 !.tran 10m");
    }

    public static int[] symCoords() {
       int[] cords = new int[2];
       cords[0] = -16+192*MOSCount;
       cords[1] = -144+rows;
       //cords[1] = yCoordDefault;
       return cords;
    }

    public static int[] flag1Coords() {
        int[] cords = new int[2];
        cords[0] = 32+192*MOSCount;
        cords[1] = -144+rows;
        //cords[1] = yCoordDefault;
        return cords;
    }

    public static int[] flag2Coords() {
        int[] cords = new int[2];
        cords[0] = -16+192*MOSCount;
        cords[1] = -64+rows;
        //cords[1] = yCoordDefault/2;
        //cords[1] = 64 + yCoordDefault;
        return cords;
    }

    public static int[] flag3Coords() {
        int[] cords = new int[2];
        cords[0] = 32+192*MOSCount;
        cords[1] = -48+rows;
        //cords[1] = 48 + yCoordDefault;
        return cords;
    }

    public static int[] flag4Coords() {
        int[] cords = new int[2];
        cords[0] = 32+192*MOSCount;
        cords[1] = -128+rows;
        //cords[1] = 48 + yCoordDefault;
        return cords;
    }

    public static void startSch() throws IOException {
        writer = new PrintWriter("LTSpiceSchematic.asc");
        writer.println("VERSION 4");
        writer.println("SHEET 1 960 720\n");
    }
    public static void endSch() throws IOException{
        writer.close();
        File file = new File("LTSpiceSchematic.asc");
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }
}
