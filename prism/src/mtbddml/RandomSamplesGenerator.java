package mtbddml;

import parser.ast.ModulesFile;

import java.util.Random;

/**
 * Created by russel on 08.04.18.
 */
public class RandomSamplesGenerator extends SamplesGenerator {
    private Random random;
    private int[] permutation;
    private int currentSampleIndex;

    public RandomSamplesGenerator(ModulesFile modulesFile, int totalSamplesCount) {
        super(modulesFile, totalSamplesCount);

        initializeRandomGenerator();
        initializePermutation();
    }

    @Override
    public boolean hasNext() {
        return currentSampleIndex < this.getTotalSamplesCount();
    }

    @Override
    public int[] next() {
        if (!hasNext()) {
            return null;
        }

        currentSampleIndex++;

        performPermutation();
        return permutation;
    }

    private void initializePermutation() {
        int varCount = this.getModulesFile().getNumVars();

        permutation = new int[varCount];

        for (int i = 0; i < varCount; i++) {
            permutation[i] = i;
        }
    }

    private void initializeRandomGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    private void performPermutation() {
        for (int i = permutation.length - 1; i > 0; i--) {
            int index = random.nextInt(i);
            int tmp = permutation[i];
            permutation[i] = permutation[index];
            permutation[index] = tmp;
        }
    }
}
