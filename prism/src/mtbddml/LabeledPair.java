package mtbddml;

/**
 * Created by russel on 10.04.18.
 */
public class LabeledPair {
    public enum Label {
        POSITIVE, NEUTRAL, NEGATIVE
    }

    public final VarPair pair;
    public final Label label;

    public LabeledPair(VarPair pair, Label label) {
        this.pair = pair;
        this.label = label;
    }
}
