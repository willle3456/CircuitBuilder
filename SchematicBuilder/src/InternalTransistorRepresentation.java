import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class InternalTransistorRepresentation {
    static void writeInternalMOSFile(PrintWriter writer, String MOSEquation) {
        String currentLine = "";
        for (int i = 0; i < MOSEquation.length(); i++) {
            if (MOSEquation.charAt(i) == '+') {
                writer.print("v");
                writer.print(currentLine);
                writer.print("g\n");
                currentLine = "";
            }
            else {
                currentLine += MOSEquation.charAt(i);
            }
        }
        writer.close();
    }
    static void writeInternalNMOSFile(String NMOSEquation) throws FileNotFoundException {
        //PrintWriter writer = new PrintWriter("./SchematicBuilder/src/internalNMOSConnections.txt");
        System.out.println("NMOSEquation: " + NMOSEquation);
        PrintWriter writer = new PrintWriter("./SchematicBuilder/src/internalNMOSConnections.txt");
        writeInternalMOSFile(writer, NMOSEquation);
    }
    static void writeInternalPMOSFile(String PMOSEquation) throws FileNotFoundException {
        System.out.println("PMOSEquation: " + PMOSEquation);
        //PrintWriter writer = new PrintWriter("./SchematicBuilder/src/internalPMOSConnections.txt");
        PrintWriter writer = new PrintWriter("./SchematicBuilder/src/internalPMOSConnections.txt");
        writeInternalMOSFile(writer, PMOSEquation);
    }
}
