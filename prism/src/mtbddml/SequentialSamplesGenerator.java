package mtbddml;

import parser.ast.ModulesFile;

/**
 * Created by russel on 09.04.18.
 */
public class SequentialSamplesGenerator extends SamplesGenerator {
    private int[][] permutations;
    private int currentSampleIndex = 0;

    public SequentialSamplesGenerator(ModulesFile modulesFile, int totalSamplesCount) {
        super(modulesFile, boundNumberOfSamples(modulesFile.getNumVars(), totalSamplesCount));

        if (getTotalSamplesCount() > 0) {
            this.generatePermutations();
        }
    }

    public boolean hasNext() {
        return currentSampleIndex < getTotalSamplesCount();
    }

    public int[] next() {
        if (!hasNext()) {
            return null;
        }

        return permutations[currentSampleIndex++];
    }

    public static int boundNumberOfSamples(int numberOfVars, int prefSamplesCount) {
        int c = 1;

        if (c >= prefSamplesCount) {
            return prefSamplesCount;
        }

        for(int i = 2; i < numberOfVars + 1; i++) {
            c *= i;

            if (c >= prefSamplesCount) {
                return prefSamplesCount;
            }
        }

        return c;
    }

    private void generatePermutations() {
        int n = this.getModulesFile().getNumVars();

        permutations = new int[getTotalSamplesCount()][n];
        this.permute(0, 0);
    }

    private int permute(int position, int start) {
        if (position >= getTotalSamplesCount()) {
            return position;
        }

        int n = this.getModulesFile().getNumVars();

        if (start == n - 1) {
            for(int i = 0; i < n; i++) {
                permutations[position][i] = i;
            }
            return position + 1;
        }

        int newPosition = permute(position, start + 1);
        if (newPosition >= getTotalSamplesCount()) {
            return newPosition;
        }

        int currentPosition = newPosition;
        for(int i = position; i < newPosition; i++) {
            for(int j = start + 1; j < n; j++) {
                System.arraycopy(permutations[i], 0, permutations[currentPosition], 0, n);

                int tmp = permutations[currentPosition][start];
                permutations[currentPosition][start] = permutations[currentPosition][j];
                permutations[currentPosition][j] = tmp;

                currentPosition++;

                if (currentPosition >= getTotalSamplesCount()) {
                    return currentPosition;
                }
            }
        }

        return currentPosition;
    }
}
