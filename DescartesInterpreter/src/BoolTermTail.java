import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class BoolTermTail implements TokenRule {
    private static final String ruleOwner = "bool-term-tail";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();

    // Initialize valid token set
    static {
        validTokenSet.put("PERIOD", 25);
        validTokenSet.put("SEMICOLON", 25);
        validTokenSet.put("THEN", 25);
        validTokenSet.put("ELSE", 25);
        validTokenSet.put("FI", 25);
        validTokenSet.put("REPEAT", 25);
        validTokenSet.put("OR", 24);
        validTokenSet.put(")", 25);
    }

    @Override
    public String getRuleOwner() {
        return ruleOwner;
    }

    @Override
    public Map<String, Integer> getValidTokenSet() {
        return null;
    }

    @Override //TODO: IMPLEMENT
    public BoolTermTail execute() throws Exception {
        return this;
    }

    @Override
    public String evaluate() {
        return null;
    }
}
