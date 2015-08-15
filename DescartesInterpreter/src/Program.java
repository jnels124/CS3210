import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public class Program implements TokenRule {
    public static TokenUtil tokenUtil;
    private static final String ruleOwner = "prog";
    private static final Map<String, Integer> validTokenSet = new HashMap<String, Integer>();
    private static final ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
    private static final Map<String, String> symbolTable = new HashMap<>();
    // Initialize valid token set
    static {
        validTokenSet.put(".", 0);
        validTokenSet.put(";", 0);
        validTokenSet.put("IF", 0);
        validTokenSet.put("LOOP", 0);
        validTokenSet.put("ID", 0);
        validTokenSet.put("BREAK", 0);
        validTokenSet.put("PRINT", 0);
        validTokenSet.put("READ", 0);
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
    public Program build() throws Exception {
        tokenUtil.parse();
        if (!validTokenSet.containsKey(tokenUtil.getCurrentToken())) {
            throw new Exception("Error generating program. Current token: " +
                                Program.tokenUtil.getCurrentToken() + " value: "
                                + Program.tokenUtil.getCurrentTokenValue());
        }
        StatementList statementList = new StatementList().build();
        if (!tokenUtil.getCurrentToken().equals(".")) {
            throw new Exception("Unable to locate end of program. Current Token:  " +
                                Program.tokenUtil.getCurrentToken() +  "Token value: " +
                                Program.tokenUtil.getCurrentTokenValue());
        }

        for(StatementNode stmnt : statements) {
            System.out.println("Executing " + stmnt.getRuleOwner() +
                                " Result: " + stmnt.evaluate());
        }
        return this;
    }

    @Override
    public String evaluate() {
        return "";
    }

    public static void main (String args []) {
        tokenUtil = new TokenUtil(args[0]);
        try {
            Program prog = new Program().build();
            for (Map.Entry<String, String> entry : symbolTable.entrySet()) {
                System.out.println("ID: " + entry.getKey() + " VALUE: " +
                                   entry.getValue());
            }
        } catch (Exception e) {
            System.out.println("Caught exception building statements:\n" +
                               e.getMessage());
        }
    }

    public static void addStatement(StatementNode stmnt) {
        statements.add(stmnt);
    }
    public static void updateSymbols(String key, String value) {
        symbolTable.put(key, value);
        System.out.println("Updated symbol table with key: " + key +
                            " value: " + value);
    }

    public static Number getNumber(String str) {
        String idVal = symbolTable.get(str);
        String toEval;
        if (idVal == null) {
            toEval = str;
        }
        else {
            toEval = idVal;
        }
        try {
            return NumberFormat.getInstance().parse(toEval);
        } catch (Exception e) {
            System.out.println("Unable to evaluate " + toEval
                                + "\nException:\n" + e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public static String getSymbolValue(String key) {
        return symbolTable.get(key);
    }
}
