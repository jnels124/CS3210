import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class ArithmeticExpression extends Expression implements TokenRule {
    private static final String ruleOwner = "arith-expr";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private Term term;
    private TermTail termTail;

    // Initialize valid token set
    static {
        validTokenSet.put("(", 37);
        validTokenSet.put("CONST", 37);
        validTokenSet.put("-", 37);
        validTokenSet.put("ID", 37);
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
    public ArithmeticExpression build() throws Exception {
        if(!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken())
           && !validTokenSet.containsKey(Program.tokenUtil.getCurrentTokenValue())) {
            throw new Exception("Error generating arithmetic expression. Current token: "
                                + Program.tokenUtil.getCurrentToken() + "value: "
                                + Program.tokenUtil.getCurrentTokenValue());
        }
        this.term = new Term().build();
        this.termTail = new TermTail().build();
        return this;
    }

    @Override
    public String evaluate() {
        String termEval = this.term.evaluate();
        String result = termEval;
        if (this.termTail != null) {
            String termTailEval = this.termTail.evaluate();
            String [] termTailComps = termTailEval.split(" ");
            for (int i = 0; i < termTailComps.length; i++) {
                switch (termTailComps[i].charAt(0)) {
                    case '-':
                        result = "" + (Program.getNumber(result).doubleValue() -
                                       Program.getNumber(termTailComps[++i]).doubleValue());
                        break;
                    case '+':
                        result = "" + (Program.getNumber(result).doubleValue() +
                                       Program.getNumber(termTailComps[++i]).doubleValue());
                        break;
                }
            }
            return result;
        }
        return "" + Program.getNumber(result);
    }
}
