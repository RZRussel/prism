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
    static final int TRAINING_SAMPLES_COUNT = 10;

    private Prism prism;
    private String trainingDirPath;

    public MTBDDML(Prism prism, String trainingDirPath) {
        this.prism = prism;
        this.trainingDirPath = trainingDirPath;
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

            //SamplesGenerator samplesGenerator = new RandomSamplesGenerator(parsedModel, TRAINING_SAMPLES_COUNT);
            SamplesGenerator samplesGenerator = new SequentialSamplesGenerator(parsedModel, TRAINING_SAMPLES_COUNT);
            while (samplesGenerator.hasNext()) {
                int[] varPermutation = samplesGenerator.next();

                parsedModel.setVarPermutation(varPermutation);

                Modules2MTBDD modelTranslator = new Modules2MTBDD(prism, parsedModel);
                Model model = modelTranslator.translate();

                JDDNode transitions = model.getTrans();

                prism.getMainLog().print(modelFile.getName() + ": number of nodes in MTBDD " + JDD.GetNumNodes(transitions) + "\n");
                prism.getMainLog().print("Permutation:\n");
                for(int i = 0; i < varPermutation.length; i++) {
                    prism.getMainLog().print(varPermutation[i] + " ");
                }
                prism.getMainLog().print("\n");
            }

        }
    }

}
