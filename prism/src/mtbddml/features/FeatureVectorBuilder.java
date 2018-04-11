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

        for(int i = 0; i < extractors.size(); i++) {
            FeatureExtractor extractor = extractors.get(i);
            featureVector.addFeature(extractor.extract());
        }

        return featureVector;
    }
}
