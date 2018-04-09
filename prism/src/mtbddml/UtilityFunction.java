package mtbddml;

import java.util.Collection;

/**
 * Created by russel on 10.04.18.
 */
public abstract class UtilityFunction {
    private Collection<PermutationEval> evaluations;

    public UtilityFunction(Collection<PermutationEval> evaluations) {
        this.evaluations = evaluations;
    }

    public Collection<PermutationEval> getEvaluations() { return evaluations; }

    public abstract double measure(VarPair pair);

    public static boolean matches(VarPair pair, PermutationEval permutationEval) {
        int n = permutationEval.getPermutationSize();
        for (int i = 0; i < n; i++) {
            int value = permutationEval.getPermutationValue(i);

            if (value == pair.firstIndex) {
                return true;
            }

            if (value == pair.secondIndex) {
                return false;
            }
        }

        return false;
    }
}
