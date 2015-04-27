import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class StatementTail implements TokenRule {
    private static final String ruleOwner = "stmt-list";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private StatementNode statement;
    private StatementTail statementTail;
    private boolean tailNullIsValid = false;

    // Initialize valid token set
    static {
        validTokenSet.put(".", 3);
        validTokenSet.put(";", 2);
        validTokenSet.put("ELSE", 3);
        validTokenSet.put("FI", 3);
        validTokenSet.put("REPEAT", 3);
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
    public StatementTail execute() throws Exception{
        if(!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken()) && !validTokenSet.containsKey(Program.tokenUtil.getCurrentTokenValue())) {
            return null;
        }
        switch (Program.tokenUtil.getCurrentTokenValue()) {
            case "." :
                this.tailNullIsValid = true;
                return null;
            case ";" :
                Program.tokenUtil.parse();
                this.statement = new StatementNode().execute();
                // Execute the appropriate returned statement
                if (this.statement != null) {
                    this.statement = this.statement.execute();
                    Program.addStatement(this.statement);
                }
                this.statementTail = new StatementTail().execute();
                break;
        }
        return this;
    }

    @Override
    public String evaluate() {
        return (this.statement != null ? this.statement.evaluate() : "") + (this.statementTail != null ? this.statementTail.evaluate() : "");
    }
}
