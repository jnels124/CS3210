import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class TermTail implements TokenRule {
    private static final String ruleOwner = "term-tail";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private String operator;
    private Term term;
    private TermTail termTail;
    // Initialize valid token set
    static {
        validTokenSet.put(".", 40);
        validTokenSet.put(";", 40);
        validTokenSet.put("THEN", 40);
        validTokenSet.put("ELSE", 40);
        validTokenSet.put("FI", 40);
        validTokenSet.put("REPEAT", 40);
        validTokenSet.put("OR", 40);
        validTokenSet.put("AND", 40);
        validTokenSet.put("<", 40);
        validTokenSet.put("<=", 40);
        validTokenSet.put("=", 40);
        validTokenSet.put(">=", 40);
        validTokenSet.put(">", 40);
        validTokenSet.put("<>", 40);
        validTokenSet.put("+", 38);
        validTokenSet.put("-", 39);
        validTokenSet.put(")", 40);
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
    public TermTail execute() throws Exception {
        switch(Program.tokenUtil.getCurrentTokenValue()) {
            case "+" :
                this.operator = "+";
                Program.tokenUtil.parse();
                this.term = new Term().execute();
                this.termTail = new TermTail().execute();

                break;
            case "-" :
                this.operator = "-";
                Program.tokenUtil.parse();
                this.term = new Term().execute();
                this.termTail = new TermTail().execute();
                break;
            default:
                return null;
        }
        return this;
    }

    @Override
    public String evaluate() {
        return this.operator + " " + this.term.evaluate() + " " + (this.termTail != null ? this.termTail.evaluate() : "");
    }
}
