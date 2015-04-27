import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class BoolTerm implements TokenRule {
    private static final String ruleOwner = "bool-term";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private BoolFactor boolFactor;
    private BoolFactorTail boolFactorTail;

    // Initialize valid token set
    static {
        validTokenSet.put("(", 26);
        validTokenSet.put("CONST", 26);
        validTokenSet.put("-", 26);
        validTokenSet.put("ID", 26);
    }

    @Override
    public String getRuleOwner() {
        return ruleOwner;
    }

    @Override
    public Map<String, Integer> getValidTokenSet() {
        return null;
    }

    @Override
    public BoolTerm execute() throws Exception {
        if (!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken()) && !validTokenSet.containsKey(Program.tokenUtil.getCurrentTokenValue())) {
            throw new Exception("Error generating bool term. Current token: " + Program.tokenUtil.getCurrentToken() + "value: " + Program.tokenUtil.getCurrentTokenValue());
        }
        this.boolFactor = new BoolFactor().execute();
        this.boolFactorTail = new BoolFactorTail().execute();
        return this;
    }

    @Override
    public String evaluate() {
        return this.boolFactor.evaluate();
    }
}
