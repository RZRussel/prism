package mtbddml;

import parser.ast.ModulesFile;

/**
 * Created by russel on 08.04.18.
 */
public abstract class PairExtractor {
    ModulesFile modulesFile;

    public PairExtractor(ModulesFile modulesFile) {
        this.modulesFile = modulesFile;
    }

}
