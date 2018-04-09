package mtbddml;

/**
 * Created by russel on 10.04.18.
 */
public class PermutationEval {
    private int[] permutation;
    private int complexity;

    public PermutationEval(int[] permutation, int complexity) {
        this.permutation = permutation;
        this.complexity = complexity;
    }

    public int getComplexity() { return complexity; }
    public int getPermutationSize() {return permutation.length; }
    public int getPermutationValue(int index) { return permutation[index]; }
}
