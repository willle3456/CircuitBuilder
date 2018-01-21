import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;

public class SchBuilder {
    static int MOSCount = 0;
    static PrintWriter writer;
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
            writer.println("SYMBOL nmos " + symCoords()[0] + " " + symCoords()[1] + " R0");
            writer.println("SYMATTR InstName MOS" + MOSCount);
            writer.println("SYMATTR Value " + componentValues[5]);
            writer.println("FLAG " + flag1Coords()[0] + " " + flag1Coords()[1] + " " + componentValues[1]);
            writer.println("FLAG " + flag2Coords()[0] + " " + flag2Coords()[1] + " " + componentValues[2]);
            writer.println("FLAG " + flag3Coords()[0] + " " + flag3Coords()[1] + " " + componentValues[3]);
            writer.println();
            MOSCount++;
        }
    }

    public static int[] symCoords() {
       int[] cords = new int[2];
       cords[0] = -16+192*MOSCount;
       cords[1] = -144;
       return cords;
    }

    public static int[] flag1Coords() {
        int[] cords = new int[2];
        cords[0] = 32+192*MOSCount;
        cords[1] = -144;
        return cords;
    }

    public static int[] flag2Coords() {
        int[] cords = new int[2];
        cords[0] = -16+192*MOSCount;
        cords[1] = -64;
        return cords;
    }

    public static int[] flag3Coords() {
        int[] cords = new int[2];
        cords[0] = 32+192*MOSCount;
        cords[1] = -48;
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
