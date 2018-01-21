import java.util.Stack;
public class StackBuilder {
    private int level = 1;
    private boolean needList = true;
    private boolean isSeries = false;
    Stack<String> stack = new Stack<String>();
    public StackBuilder(String inputText) {
        for (int i = inputText.length()-1; i >= 0; i--) {
            stack.push(Character.toString(inputText.charAt(i)));
        }
    }

    public String peek() {
        return stack.peek();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel() {
        level = 1;
    }

    public boolean isSeries() {
        return isSeries;
    }

    public boolean empty(){
        return stack.empty();
    }

    public String pop() {
        String value = stack.pop();
        if (stack.empty()) {
            level = 0;
        }
        // Update needs based on value
        if (value.equals("+")) {
            isSeries = false;
        }
        else {
            isSeries = true;
        }

        return value;
    }

    public boolean needNewList() {
        return needList;
    }

    public void updateNeeds(String value) {
        if (value == "(") {
            needList = true;
        }
        else if (value == ")") {
            needList = false;
        }
        else if (value == "*") {
            if (!isSeries) {
                isSeries = true;
            }
        }
        else if (value == "+") {
            if (isSeries) {
                isSeries = false;
            }
        }
    }

}
