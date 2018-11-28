package simplega;
/**
 *
 * @author BPop
 */
public class Rule {
    private int[] condition;
    private int output;
    
    public Rule(int[] condition, int output){
        this.condition = condition;
        this.output = output;
    }
    
    public int[] getCondition() {
        return condition;
    }

    public void setCondition(int[] condition) {
        this.condition = condition;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
}
