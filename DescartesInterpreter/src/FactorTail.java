import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class FactorTail implements TokenRule {
    private static final String ruleOwner = "term-tail";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private String operator;
    private Factor factor;
    private FactorTail factorTail;

    // Initialize valid token set
    static {
        validTokenSet.put(".", 44);
        validTokenSet.put(";", 44);
        validTokenSet.put("THEN", 44);
        validTokenSet.put("ELSE", 44);
        validTokenSet.put("FI", 44);
        validTokenSet.put("REPEAT", 44);
        validTokenSet.put("OR", 44);
        validTokenSet.put("AND", 44);
        validTokenSet.put("<", 44);
        validTokenSet.put("<=", 44);
        validTokenSet.put("=", 44);
        validTokenSet.put(">=", 44);
        validTokenSet.put(">", 44);
        validTokenSet.put("<>", 44);
        validTokenSet.put("+", 44);
        validTokenSet.put("-", 44);
        validTokenSet.put("*", 42);
        validTokenSet.put("/", 43);
        validTokenSet.put(")", 44);
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
    public FactorTail execute() throws Exception {
        if (!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken()) && !validTokenSet.containsKey(Program.tokenUtil.getCurrentTokenValue())) {
            return null;
        }
        switch (Program.tokenUtil.getCurrentTokenValue()) {
            case "*" :
                this.operator = "*";
                break;
            case "/" :
                this.operator = "/";
                break;
            default :
                return null;
        }

        Program.tokenUtil.parse();
        this.factor = new Factor().execute();
        this.factorTail = new FactorTail().execute();
        return this;
    }

    @Override
    public String evaluate() {

        if (this.operator != null) {
            return this.operator + " " + this.factor.evaluate() + " " + (this.factorTail != null ? this.factorTail.evaluate() : "");
        }

        assert(this.factor == null);
        assert(this.factorTail == null);
        return "";
    }

}
