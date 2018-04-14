package mtbddml.features;

public abstract class FeatureReducer {
    public abstract void collect(double feature);
    public abstract double reduce();
    public abstract boolean canReduce();
}
