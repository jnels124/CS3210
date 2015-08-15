import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class AssignmentStatement extends StatementNode implements TokenRule {
    private static final String ruleOwner = "assign-stmt";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private String id;
    private Expression expression;

    // Initialize valid token set
    static {
        validTokenSet.put("ID", 18);
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
    public AssignmentStatement build() throws Exception {
        if(!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken())) {
            throw new Exception("Expected ID but got " +
                      validTokenSet.containsKey(Program.tokenUtil.getCurrentToken()));
        }
        this.id = Program.tokenUtil.getCurrentTokenValue();
        // Should find :=
        Program.tokenUtil.parse();
        if (!Program.tokenUtil.getCurrentTokenValue().equals("BECOMES")) {
            throw new Exception ("Expected := but got " +
                                 Program.tokenUtil.getCurrentToken());
        }
        Program.tokenUtil.parse();
        this.expression = new Expression().build();

        return this;
    }

    @Override
    public String evaluate() {
        String expressionEval = this.expression.evaluate();
        Program.updateSymbols(this.id, expressionEval);
        return expressionEval;
    }
}
