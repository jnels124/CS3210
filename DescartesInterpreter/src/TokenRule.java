import java.util.Map;

/**
 * Created by jessenelson on 4/17/15.
 */
public interface TokenRule {
    String getRuleOwner ();

    /**
     * Gets the static set of rules this token can become
     *
     * @return a map containing the valid tokens. The key is the token name and the value is the rule to go to.
     */
    Map<String, Integer> getValidTokenSet();

    TokenRule execute() throws Exception;

    String evaluate();
}
