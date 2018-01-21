import java.io.PrintWriter;
import java.io.IOException;

public class NetBuilder {
    static int NMOSCount = 0;
    static PrintWriter writer;
    public static void writePMOS(String line) throws IOException {
        if (line.length() == 3) {
            writer.print("MOS" + NMOSCount + " ");
            expandCharacter(writer, line, 2, false);
            expandCharacter(writer, line, 1, false);
            expandCharacter(writer, line, 0, false);
            expandCharacter(writer, line, 0, false);
            writer.print("PMOS \r\n");
        }
        else if (line.length() > 3) {
            writer.print("MOS" + NMOSCount + " ");
            expandCharacter(writer, line, 0, false);
            expandCharacter(writer, line, 1, false);
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("PMOS \r\n");
            writePMOSChain(line.substring(1, line.length()));
        }
    }

    public static void writePMOSChain(String line) throws IOException {
        writer.print("MOS" + NMOSCount + " ");
        if (line.length() == 3) {
            writer.print("" + line.charAt(0) + "" + line.charAt(1) + " ");
            writer.print("" + line.charAt(1) + " ");
            expandCharacter(writer, line, 2, false);
            expandCharacter(writer, line, 2, false);
            writer.print("PMOS \r\n");
            NMOSCount++;
        }
        else if (line.length() > 3) {
            writer.print("" + line.charAt(0) + "" + line.charAt(1) + " ");
            writer.print("" + line.charAt(1) + " ");
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("PMOS \r\n");
            NMOSCount++;
            writePMOSChain(line.substring(1, line.length()));
        }
    }

    public static void writeNMOS(String line) throws IOException {
        if (line.length() == 3) {
            writer.print("MOS" + NMOSCount + " ");
            expandCharacter(writer, line, 0, true);
            expandCharacter(writer, line, 1, true);
            expandCharacter(writer, line, 2, true);
            expandCharacter(writer, line, 2, true);
            writer.print("NMOS \r\n");
            NMOSCount++;
        }
        else if (line.length() > 3) {
            writer.print("MOS" + NMOSCount + " ");
            expandCharacter(writer, line, 0, true);
            expandCharacter(writer, line, 1, true);
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("NMOS \r\n");
            NMOSCount++;
            writeNMOSChain(line.substring(1, line.length()));
        }
    }

    public static void writeNMOSChain(String line) throws IOException {
        writer.print("MOS" + NMOSCount + " ");
        if (line.length() == 3) {
            writer.print("" + line.charAt(0) + "" + line.charAt(1) + " ");
            writer.print("" + line.charAt(1) + " ");
            expandCharacter(writer, line, 2, true);
            expandCharacter(writer, line, 2, true);
            writer.print("NMOS \r\n");
        }
        else if (line.length() > 3) {
            writer.print("" + line.charAt(0) + "" + line.charAt(1) + " ");
            writer.print("" + line.charAt(1) + " ");
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("NMOS \r\n");
            writeNMOSChain(line.substring(1, line.length()));
        }
    }

    public static void expandCharacter(PrintWriter writer, String line, int index, boolean NMOS) {
        switch(line.charAt(index)) {
            case 'v':
                writer.print(NMOS ? "Vout " : "Vdd " );
                break;
            case 'g':
                writer.print(NMOS ? "GND " : "Vout " );
                break;
            default:
                writer.print(line.charAt(index) + " ");
                break;
        }
    }
    public static void startNet() throws IOException {
        writer = new PrintWriter("./SchematicBuilder/src/Output2.net");
        //writer.println("* /Users/sean/Desktop/Q2.asc");
    }
    public static void endNet() {
        /*
        writer.println(".lib /Users/sean/Library/Application Support/LTspice/lib/cmp/standard.mos");
        writer.println(".backanno");
        writer.println(".end");
        */
        writer.close();
    }
}
