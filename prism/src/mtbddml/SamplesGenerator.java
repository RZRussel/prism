package mtbddml;

import parser.ast.ModulesFile;

import java.util.Iterator;

/**
 * Created by russel on 08.04.18.
 */
public abstract class SamplesGenerator implements Iterator {
    private ModulesFile modulesFile;
    private int totalSamplesCount;

    public SamplesGenerator(ModulesFile modulesFile, int totalSamplesCount) {
        this.modulesFile = modulesFile;
        this.totalSamplesCount = totalSamplesCount;
    }

    public ModulesFile getModulesFile() {
        return this.modulesFile;
    }
    public int getTotalSamplesCount() { return this.totalSamplesCount; }

    public abstract boolean hasNext();
    public abstract int[] next();

    public long getNumberOfPossiblePermutations() {
        long c = 1;
        long n = this.getModulesFile().getNumVars();

        if (n == 0) {
            return 0;
        }

        for (int i = 2; i < n+1; i++) {
            c *= i;
        }

        return c;
    }
}
