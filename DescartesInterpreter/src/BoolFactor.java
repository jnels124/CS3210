import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class BoolFactor implements TokenRule {
    private static final String ruleOwner = "bool-factor";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private ArithmeticExpression arithmeticExpression;
    // Add relation Option

    // Initialize valid token set
    static {
        validTokenSet.put("(", 29);
        validTokenSet.put("CONST", 29);
        validTokenSet.put("-", 29);
        validTokenSet.put("ID", 29);
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
    public BoolFactor build() throws Exception {
        this.arithmeticExpression = new ArithmeticExpression().build();
        return this;
    }

    @Override
    public String evaluate() {
        return this.arithmeticExpression.evaluate();
    }
}
