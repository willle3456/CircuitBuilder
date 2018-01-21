public class ConnectionBuilder {
    static String AlgebraToNMOS(String algebraEquation) {
        String newString = "";
        for (int i=0; i<algebraEquation.length(); i++) {
            if (algebraEquation.charAt(i) == '*') {
                continue;
            }
            newString += algebraEquation.charAt(i);
        }
        return newString + "+";
    }

    static String AlgebraToPMOS(String algebraEquation) {
        String newString = "";
        for (int i = 0; i < algebraEquation.length(); i++) {
            if (algebraEquation.charAt(i) == '+') {
                continue;
            }
            else if (algebraEquation.charAt(i) == '*') {
                newString += "+";
            }
            else {
                newString += algebraEquation.charAt(i);
            }
        }
        return newString + "+";
    }
}
