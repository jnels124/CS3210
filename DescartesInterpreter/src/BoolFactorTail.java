import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class BoolFactorTail implements TokenRule {
    private static final String ruleOwner = "bool-factor-tail";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();

    // Initialize valid token set
    static {
        validTokenSet.put("PERIOD", 28);
        validTokenSet.put("SEMICOLON", 28);
        validTokenSet.put("THEN", 28);
        validTokenSet.put("ELSE", 28);
        validTokenSet.put("FI", 28);
        validTokenSet.put("REPEAT", 28);
        validTokenSet.put("OR", 28);
        validTokenSet.put("AND", 27);
        validTokenSet.put(")", 28);
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
    public BoolFactorTail execute() throws Exception {
        return this;
    }

    @Override
    public String evaluate() {
        return null;
    }
}
