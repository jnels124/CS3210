import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class Expression implements TokenRule {
    private static final String ruleOwner = "expr";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private BoolTerm boolTerm;
    private BoolTermTail boolTermTail;

    // Initialize valid token set
    static {
        validTokenSet.put("(", 23);
        validTokenSet.put("CONST", 23);
        validTokenSet.put("-", 23);
        validTokenSet.put("ID", 23);
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
    public Expression execute() throws Exception{
        if(!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken()) && !validTokenSet.containsKey(Program.tokenUtil.getCurrentTokenValue())) {
            throw new Exception("Error generating Expression. Current token: " + Program.tokenUtil.getCurrentToken() + "value: " + Program.tokenUtil.getCurrentTokenValue());
        }
        this.boolTerm = new BoolTerm().execute();
        this.boolTermTail = new BoolTermTail().execute();
        return this;
    }

    @Override
    public String evaluate() {
        return this.boolTerm.evaluate();
    }
}
