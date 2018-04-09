package mtbddml;

import parser.ast.ModulesFile;
import prism.Pair;
import prism.PrismLangException;

import java.util.List;
import java.util.Set;

/**
 * Created by russel on 08.04.18.
 */
public abstract class PairExtractor {
    private ModulesFile modulesFile;

    public PairExtractor(ModulesFile modulesFile) {
        this.modulesFile = modulesFile;
    }

    public ModulesFile getModulesFile() { return this.modulesFile; }

    public abstract Set<VarPair> extract() throws PrismLangException;

}
