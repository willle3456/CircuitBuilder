import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) throws Exception {
        // Interpret Master Equation
        initializations();
        Scanner masterScanner = createScanner("./SchematicBuilder/src/MasterEquation.txt");
        String masterEquation = masterScanner.nextLine();
        String simplifiedNMOS = ConnectionBuilder.AlgebraToNMOS(masterEquation);
        String simplifiedPMOS = ConnectionBuilder.AlgebraToPMOS(masterEquation);

        // Write MOSFET Internal Representation Files
        InternalTransistorRepresentation.writeInternalNMOSFile(simplifiedNMOS);
        InternalTransistorRepresentation.writeInternalPMOSFile(simplifiedPMOS);


        Scanner fileScannerNMOS = createScanner("./SchematicBuilder/src/internalNMOSConnections.txt");
        Scanner fileScannerPMOS = createScanner("./SchematicBuilder/src/internalPMOSConnections.txt");
        //StackBuilder nmos = new StackBuilder(inputText);
        NetBuilder.startNet();
        while (fileScannerNMOS.hasNextLine()) {
            String lineChain = fileScannerNMOS.nextLine();
            NetBuilder.writeNMOS(lineChain);
        }
        while (fileScannerPMOS.hasNextLine()) {
            String lineChain = fileScannerPMOS.nextLine();
            NetBuilder.writePMOS(lineChain);
        }
        NetBuilder.endNet();
        SchBuilder.startSch();
        SchBuilder.writeCMOSNetwork();
        SchBuilder.endSch();
        closeProgram();
    }

    static void constructPullDownNetwork(StackBuilder nmos) {
        ArrayList<Node> midRail = new ArrayList<Node>();
        ArrayList<Node> botRail = new ArrayList<Node>();
        Node VOUT = new Node("VOUT");
        Node GND  = new Node("GND");
        midRail.add(VOUT);
        botRail.add(GND);

        // Dynamic Builders
        while (!nmos.empty()) {
            nmos.setLevel();
            buildCluster(nmos, null, VOUT, botRail);
        }
        // Print out midRail
        printAllClusters(midRail);
    }
    static void printAllClusters(ArrayList<Node> cluster) {
        for (int i = 0; i < cluster.size(); i++) {
            System.out.println(cluster.get(i).getLabel());
            if (cluster.get(i).hasNextCluster()) {
                printAllClusters(cluster.get(i).getNextCluster());
            }
        }
    }

    static void buildCluster(StackBuilder stack, Node prevNode, Node curNode, ArrayList<Node> botRail) {
        if (stack.getLevel() == 0) { // getLevel is controlled by StackBuilder pops - includes check for empty stack
            curNode.pointNextCluster(botRail);
            return;
        }
        String currentInput = stack.pop();
        buildCluster(stack, prevNode, curNode, botRail);
    }

   static void parseInputText(String inputText) {
        // Text Parsing
        StackBuilder nmos = new StackBuilder(inputText);
        int strLength = inputText.length();
        char currentInput;
        System.out.println(inputText);

        // Internal Schematic
        Node curNode = null;
        for (int i=0; i<strLength; i++) {
            currentInput = inputText.charAt(i);
            if (shouldPop(strLength, i, currentInput)) {
                // Pop node --> Connect prevNode to botRail
            }
            else if (shouldAdd(currentInput)) {
                // Add node
                System.out.print("-o-");
            }
            else {
                // Store current input as a new node
                Node n = new Node(Character.toString(currentInput));
            }
        }
    }

    static boolean shouldPop(int strLength, int index, char currentInput) {
        return (currentInput == '+' || index == strLength - 1);
    }

    static boolean shouldAdd(char currentInput) {
        return (currentInput == '*');
    }

    static Scanner createScanner(String filePath) throws Exception {
        return new Scanner(new File(filePath));
    }

    static void initializations() {
        System.out.println("______________________________");
    }
    static void closeProgram() {
        System.out.println("______________________________");
    }
}
