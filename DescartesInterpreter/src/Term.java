import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class Term implements TokenRule {
    private static final String ruleOwner = "term";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private Factor factor;
    private FactorTail factorTail;

    // Initialize valid token set
    static {
        validTokenSet.put("(", 41);
        validTokenSet.put("CONST", 41);
        validTokenSet.put("-", 41);
        validTokenSet.put("ID", 41);
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
    public Term build() throws Exception {
        if (!validTokenSet.containsKey(Program.tokenUtil.getCurrentToken()) &&
            !validTokenSet.containsKey(Program.tokenUtil.getCurrentTokenValue())) {
            throw new Exception("Error generating term. Current token: " +
                                Program.tokenUtil.getCurrentToken() + "value: " +
                                Program.tokenUtil.getCurrentTokenValue());
        }

        this.factor = new Factor().build();
        this.factorTail = new FactorTail().build();
        return this;
    }

    @Override
    public String evaluate() {
        String factorEval = this.factor.evaluate();
        String factorTailEval;
        String [] factorTailComponents;
        String result = factorEval;
        if (this.factorTail != null) {
            factorTailEval = this.factorTail.evaluate();
            factorTailComponents = factorTailEval.split(" ");
            for (int i =0; i < factorTailComponents.length; i++) {
                switch (factorTailComponents[i].charAt(0)) {
                    case '/' :
                        result = "" + Program.getNumber(result).doubleValue() /
                                      Program.getNumber(factorTailComponents[++i])
                                              .doubleValue();
                        break;
                    case '*' :
                        result = "" + Program.getNumber(result).doubleValue() *
                                      Program.getNumber(factorTailComponents[++i])
                                              .doubleValue();
                }
            }

            return result;
        }

        return "" + Program.getNumber(result).doubleValue();
    }


}
