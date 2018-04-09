package mtbddml;

/**
 * Created by russel on 09.04.18.
 */
public class VarPair {
    public final int firstIndex;
    public final int secondIndex;

    public VarPair(int firstIndex, int secondIndex) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }

    public VarPair inversed() {
        return new VarPair(secondIndex, firstIndex);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object pair) {
        if (pair == null || !(pair instanceof VarPair)) {
            return false;
        }

        return firstIndex == ((VarPair) pair).firstIndex && secondIndex == ((VarPair) pair).secondIndex;
    }

    public String toString() {
        return "(" + firstIndex + ", " + secondIndex + ")";
    }
}
