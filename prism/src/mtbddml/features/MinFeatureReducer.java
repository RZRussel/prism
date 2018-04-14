package mtbddml.features;

public class MinFeatureReducer extends FeatureReducer {
    private double minValue;
    private boolean canReduce;

    public void collect(double feature) {
        if (!canReduce) {
            canReduce = true;
            minValue = feature;
            return;
        }

        if (feature < minValue) {
            minValue = feature;
        }
    }

    public double reduce() {
        if (!canReduce) {
            throw new Error("Can't reduce");
        }

        return minValue;
    }

    public boolean canReduce() {
        return canReduce;
    }
}
