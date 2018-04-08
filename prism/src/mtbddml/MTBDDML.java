package mtbddml;

import java.io.File;
import java.io.FileFilter;

import jdd.JDD;
import jdd.JDDNode;
import parser.ast.ModulesFile;
import prism.*;

/**
 * Created by russel on 06.04.18.
 */
public class MTBDDML {

    private Prism prism;
    private String trainingDirPath;

    public MTBDDML(Prism prism, String trainingDirPath) {
        this.prism = prism;
        this.trainingDirPath = trainingDirPath;

        optimizePrismSettings();
    }

    public void loadAndTrain() throws Exception {
        if (trainingDirPath == null) {
            throw new Exception("Training directory must be setup");
        }

        File directory = new File(trainingDirPath);
        File[] directoryListing = directory.listFiles(pathname -> !pathname.isDirectory());

        if (directoryListing == null || directoryListing.length == 0) {
            prism.getMainLog().print("Warning: Training directory is empty\n");
            return;
        }

        for (File modelFile: directoryListing) {
            ModulesFile parsedModel =  prism.parseModelFile(modelFile);
            Modules2MTBDD modelTranslator = new Modules2MTBDD(prism, parsedModel);
            Model model = modelTranslator.translate();

            JDDNode transitions = model.getTrans();

            prism.getMainLog().print(modelFile.getName() + ": number of nodes in MTBDD " + JDD.GetNumNodes(transitions));
        }
    }

    private void optimizePrismSettings() {

    }
}
