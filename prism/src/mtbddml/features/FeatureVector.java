package mtbddml.features;

import java.text.DecimalFormat;
import java.util.ArrayList;

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

    public String toString() {
        String result = "(";
        for (int i = 0; i < features.size(); i++) {
            if (i > 0) {
                result += "; ";
            }

            result += String.format("%.4f", features.get(i));
        }

        result += ")";

        return result;
    }
}
