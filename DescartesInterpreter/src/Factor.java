import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class Factor implements TokenRule {
    private static final String ruleOwner = "bool-term";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();

    public TokenRule getTokenRule() {
        return tokenRule;
    }

    public void setTokenRule(TokenRule tokenRule) {
        this.tokenRule = tokenRule;
    }

    private TokenRule tokenRule;
    private String operator;
    // Initialize valid token set
    static {
        validTokenSet.put("(", 47);
        validTokenSet.put("CONST", 46);
        validTokenSet.put("-", 45);
        validTokenSet.put("ID", 46);
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
    public Factor execute() throws Exception {
        if (!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken()) && !validTokenSet.containsKey(Program.tokenUtil.getCurrentTokenValue())){
            throw new Exception("Error generating Factor. Current token: " + Program.tokenUtil.getCurrentToken() + "value: " + Program.tokenUtil.getCurrentTokenValue());
        }
        switch (Program.tokenUtil.getCurrentToken()) {
            case "(" :
                Program.tokenUtil.parse();
                this.operator = "(";
                this.tokenRule = new Expression().execute();
                if(!Program.tokenUtil.getCurrentTokenValue().equals(")")) {
                    throw new Exception("Error generating expression for factor. Current token: " + Program.tokenUtil.getCurrentToken() + " value: " + Program.tokenUtil.getCurrentTokenValue());
                }
                Program.tokenUtil.parse();
                break;
            case "-" :
                Program.tokenUtil.parse();
                this.operator = "-";
                Factor expectedFactor = new Factor().execute();
                if (expectedFactor == null) {
                    throw new Exception("Error generating factor for factor -. Current token: " + Program.tokenUtil.getCurrentToken() + " value: " + Program.tokenUtil.getCurrentTokenValue());
                }
                this.tokenRule = expectedFactor;
                this.operator += expectedFactor.operator;
                break;

            default:
                this.operator = Program.tokenUtil.getCurrentTokenValue();
                Program.tokenUtil.parse();
                break;
        }
        return this;
    }

    @Override
    public String evaluate() {
        return !this.operator.equals("(") ? this.operator : this.tokenRule.evaluate();
    }
}
