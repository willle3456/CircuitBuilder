import java.util.ArrayList;
public class Node {
    private ArrayList<Node> nextCluster = new ArrayList<Node>();
    private String label;

    public Node(String label) {
       this.label = label;
    }

    public boolean hasNextCluster() {
        return !(nextCluster == null);
    }
    public ArrayList<Node> getNextCluster() {
        return nextCluster;
    }

    public void pointNextCluster(ArrayList<Node> cluster) {
        nextCluster = cluster;
    }

    public void nextClusterFromNode(Node n) {
        ArrayList<Node> newCluster = new ArrayList<Node>();
        newCluster.add(n);
        nextCluster = newCluster;
    }

    public String getLabel() {
        return this.label;
    }
}
