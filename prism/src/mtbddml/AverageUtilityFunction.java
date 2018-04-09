package mtbddml;

import java.util.Collection;

/**
 * Created by russel on 10.04.18.
 */
public class AverageUtilityFunction extends UtilityFunction {
    public AverageUtilityFunction(Collection<PermutationEval> evaluations) {
        super(evaluations);
    }

    public double measure(VarPair pair) {
        double sum = 0.0;
        int count = 0;

        for (PermutationEval e: getEvaluations()) {
            if (matches(pair, e)) {
                sum += e.getComplexity();
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        return sum / count;
    }
}
