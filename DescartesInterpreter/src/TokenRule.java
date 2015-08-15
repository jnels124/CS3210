import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public interface TokenRule {
    String getRuleOwner ();

    /**
     * Gets the static set of rules this token can become
     *
     * @return a map containing the valid tokens.
     *  The key is the token name and the value is the rule to go to.
     */
    Map<String, Integer> getValidTokenSet();

    /**
     * Build the tree for the statement
     * @return An instance of itself with all nodes attached
     * @throws Exception
     */
    TokenRule build() throws Exception;

    /**
     * Executes and returns the result of its childeren
     * @return
     */
    String evaluate();
}
