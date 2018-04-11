package mtbddml.features;

import parser.ast.ModulesFile;
import prism.PrismLangException;

/**
 * Created by russel on 11.04.18.
 */
public abstract class FeatureExtractor {
    private ModulesFile modulesFile;

    public FeatureExtractor(ModulesFile modulesFile) {
        this.modulesFile = modulesFile;
    }

    public ModulesFile getModulesFile() {
        return modulesFile;
    }

    public abstract double extract() throws PrismLangException;
}
