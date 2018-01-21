import java.io.PrintWriter;
import java.io.IOException;

public class NetBuilder {
    static int NMOSCount = 0;
    static PrintWriter writer;
    public static void writeNMOS(String line) throws IOException {
        if (line.length() == 3) {
            writer.print("MOS" + NMOSCount + " ");
            expandCharacter(writer, line, 0);
            expandCharacter(writer, line, 1);
            expandCharacter(writer, line, 2);
            expandCharacter(writer, line, 2);
            writer.print("NMOS \r\n");
        }
        else if (line.length() > 3) {
            writer.print("MOS" + NMOSCount + " ");
            expandCharacter(writer, line, 0);
            expandCharacter(writer, line, 1);
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("" + line.charAt(1) + "" + line.charAt(2) + " ");
            writer.print("NMOS \r\n");
            writeNMOSChain(line.substring(1, line.length()));
        }
    }

    public static void writeNMOSChain(String line) throws IOException {
        writer.print("MOS" + NMOSCount + " ");
        if (line.length() == 3) {
            writer.print("" + line.charAt(0) + "" + line.charAt(1) + " ");
            writer.print("" + line.charAt(1) + " ");
            expandCharacter(writer, line, 2);
            expandCharacter(writer, line, 2);
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

    public static void expandCharacter(PrintWriter writer, String line, int index) {
        switch(line.charAt(index)) {
            case 'v':
                writer.print("Vdd ");
                break;
            case 'g':
                writer.print("GND ");
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
