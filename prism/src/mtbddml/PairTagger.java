package mtbddml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by russel on 08.04.18.
 */
public abstract class PairTagger {
    public abstract LabeledPair tag(VarPair pair);

    public Set<LabeledPair> tagAll(Collection<VarPair> pairs) {
        HashSet<LabeledPair> tags = new HashSet<>();

        for(VarPair pair: pairs) {
            tags.add(tag(pair));
        }

        return tags;
    }
}
