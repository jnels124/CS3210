import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class StatementList implements TokenRule {
    private static final String ruleOwner = "stmt-list";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private StatementNode statement;
    private StatementTail statementTail;

    // Initialize valid token set
    static {
        validTokenSet.put("PERIOD", 1);
        validTokenSet.put("SEMICOLON", 1);
        validTokenSet.put("IF", 1);
        validTokenSet.put("ELSE", 1);
        validTokenSet.put("FI", 1);
        validTokenSet.put("LOOP", 1);
        validTokenSet.put("ID", 1);
        validTokenSet.put("REPEAT", 1);
        validTokenSet.put("BREAK", 1);
        validTokenSet.put("PRINT", 1);
        validTokenSet.put("READ", 1);
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
    public StatementList execute() throws Exception {
        this.statement = new StatementNode().execute();
        if (statement != null) {
            // Run execution for specific assignemnt node type
            this.statement = statement.execute();

            Program.addStatement(this.statement);
            this.statementTail = new StatementTail().execute();
            return this;
        }
        return null;
    }

    // Unused
    @Override
    public String evaluate() {
        return "";//this.statement.evaluate() + this.statementTail.evaluate();
    }

}
