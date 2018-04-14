package mtbddml.features;


public class AverageFeatureReducer extends FeatureReducer {
    private double sum = 0.0;
    private int count = 0;
    private boolean canReduce;

    public void collect(double feature) {
        canReduce = true;
        sum += feature;
        count++;
    }

    public double reduce() {
        if (!canReduce) {
            throw new Error("Can't reduce");
        }

        return count > 0 ? sum / count : 0.0;
    }

    public boolean canReduce() {
        return canReduce;
    }
}
