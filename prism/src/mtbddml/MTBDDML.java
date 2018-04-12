package mtbddml;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jdd.JDD;
import jdd.JDDNode;
import mtbddml.features.*;
import parser.VarList;
import parser.ast.ModulesFile;
import prism.*;

/**
 * Created by russel on 06.04.18.
 */
public class MTBDDML {
    static final int TRAINING_SAMPLES_COUNT = 200;
    static final double LABEL_TRESHOLD = 50.0;

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
        File[] directoryListing = directory.listFiles(pathname -> !pathname.isDirectory() && !pathname.isHidden());

        if (directoryListing == null || directoryListing.length == 0) {
            prism.getMainLog().print("Warning: Training directory is empty\n");
            return;
        }

        for (File modelFile: directoryListing) {
            ModulesFile parsedModel =  prism.parseModelFile(modelFile);
            VarList varList = parsedModel.createVarList();

            InteractivePairExtractor pairExtractor = new InteractivePairExtractor(parsedModel);
            Set<VarPair> pairs = pairExtractor.extract();

            SamplesGenerator samplesGenerator = new RandomSamplesGenerator(parsedModel, TRAINING_SAMPLES_COUNT);
            //SamplesGenerator samplesGenerator = new SequentialSamplesGenerator(parsedModel, TRAINING_SAMPLES_COUNT);
            HashSet<PermutationEval> permEvals = new HashSet<>();
            while (samplesGenerator.hasNext()) {
                int[] varPermutation = samplesGenerator.next();

                parsedModel.setVarPermutation(varPermutation);

                Modules2MTBDD modelTranslator = new Modules2MTBDD(prism, parsedModel);
                Model model = modelTranslator.translate();

                JDDNode transitions = model.getTrans();
                PermutationEval e = new PermutationEval(varPermutation, JDD.GetNumNodes(transitions));
                permEvals.add(e);
            }

            // reset var permutation
            parsedModel.setVarPermutation(null);

            AverageUtilityFunction utility = new AverageUtilityFunction(permEvals);
            UtilityPairTagger tagger = new UtilityPairTagger(utility, LABEL_TRESHOLD);

            Collection<LabeledPair> labeledPairs = tagger.tagAll(pairs);

            prism.getMainLog().print(modelFile.getName() + ":\n");

            for (LabeledPair labeledPair: labeledPairs) {
                prism.getMainLog().print(varList.getName(labeledPair.pair.firstIndex));
                prism.getMainLog().print(" " + varList.getName(labeledPair.pair.secondIndex));
                prism.getMainLog().print(" " + labeledPair.label + "\n");

                FeatureVector featureVector = buildFeatureVector(parsedModel, labeledPair);

                prism.getMainLog().print(featureVector.toString() + "\n");
            }

        }
    }

    private FeatureVector buildFeatureVector(ModulesFile model, LabeledPair labeledPair) throws PrismException {
        FeatureVectorBuilder featureVectorBuilder = new FeatureVectorBuilder();
        featureVectorBuilder.add(new VarDependenceExtractor(model, labeledPair.pair.firstIndex));
        featureVectorBuilder.add(new VarDependenceExtractor(model, labeledPair.pair.secondIndex));
        featureVectorBuilder.add(new VarDependencyExtractor(model, labeledPair.pair.firstIndex));
        featureVectorBuilder.add(new VarDependencyExtractor(model, labeledPair.pair.secondIndex));
        featureVectorBuilder.add(new VarGuardDependencyExtractor(model, labeledPair.pair.firstIndex));
        featureVectorBuilder.add(new VarGuardDependencyExtractor(model, labeledPair.pair.secondIndex));
        featureVectorBuilder.add(new PairMinDistanceExtractor(model, labeledPair.pair, PairMinDistanceExtractor.SearchType.UPDATE));
        featureVectorBuilder.add(new PairMinDistanceExtractor(model, labeledPair.pair, PairMinDistanceExtractor.SearchType.GUARD));
        featureVectorBuilder.add(new PairVarMutualDependence(model, labeledPair.pair));
        return featureVectorBuilder.build();
    }
}
