package mtbddml.features;

import java.util.ArrayList;

/**
 * Created by russel on 11.04.18.
 */
public class FeatureVector {
    ArrayList<Double> features = new ArrayList<>();

    public void addFeature(double feature) {
        features.add(feature);
    }

    public int getNumOfFeatures() {
        return features.size();
    }

    public double getFeatureAtIndex(int index) {
        return features.get(index);
    }
}
