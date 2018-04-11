package mtbddml.features;

import prism.PrismException;

import java.util.ArrayList;

/**
 * Created by russel on 11.04.18.
 */
public class FeatureVectorBuilder {
    ArrayList<FeatureExtractor> extractors = new ArrayList<>();

    public FeatureVectorBuilder add(FeatureExtractor extractor) {
        extractors.add(extractor);
        return this;
    }

    public FeatureVector build() throws PrismException {
        FeatureVector featureVector = new FeatureVector();

        for(FeatureExtractor extractor: extractors) {
            featureVector.addFeature(extractor.extract());
        }

        return featureVector;
    }
}
