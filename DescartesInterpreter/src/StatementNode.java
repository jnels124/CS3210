import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class StatementNode implements TokenRule {
    private static final String ruleOwner = "stmt";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();

    // Initialize valid token set
    static {
        validTokenSet.put(".", 10);
        validTokenSet.put(";", 10);
        validTokenSet.put("IF", 4);
        validTokenSet.put("ELSE", 10);
        validTokenSet.put("FI", 10);
        validTokenSet.put("LOOP", 5);
        validTokenSet.put("ID", 7);
        validTokenSet.put("REPEAT", 10);
        validTokenSet.put("BREAK", 6);
        validTokenSet.put("PRINT", 9);
        validTokenSet.put("READ", 8);
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
    public StatementNode build() throws Exception{
        return getStatement();
    }

    @Override // Override in subclass
    public String evaluate() {

        return null;
    }

    private StatementNode getStatement () throws Exception {
        switch (Program.tokenUtil.getCurrentToken()) {
            case "ID" :
                return new AssignmentStatement();
            case "." :
                //fall through
            case ";" :
                return null;
            default :
                throw new Exception("Expected statement but got " +
                                    Program.tokenUtil.getCurrentToken()); // Error
        }
    }
}
