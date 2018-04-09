package mtbddml;

/**
 * Created by russel on 10.04.18.
 */
public class UtilityPairTagger extends PairTagger {
    UtilityFunction utility;
    double threshold;

    public UtilityPairTagger(UtilityFunction utility, double threshold) {
        this.utility = utility;
        this.threshold = threshold;
    }

    @Override
    public LabeledPair tag(VarPair pair) {
        double w1 = utility.measure(pair);
        double w2 = utility.measure(pair.inversed());

        LabeledPair.Label label;
        if (Math.abs(w2 - w1) < threshold) {
            label = LabeledPair.Label.NEUTRAL;
        } else if (w1 < w2) {
            label = LabeledPair.Label.POSITIVE;
        } else {
            label = LabeledPair.Label.NEGATIVE;
        }

        return new LabeledPair(pair, label);
    }
}
