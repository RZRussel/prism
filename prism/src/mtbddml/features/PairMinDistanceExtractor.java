package mtbddml.features;

import mtbddml.VarPair;
import parser.VarList;
import parser.ast.*;
import prism.PrismLangException;

import java.util.HashSet;

public class PairMinDistanceExtractor extends VisitorFeatureExtractor {
    public enum SearchType {
        UPDATE, GUARD;
    }

    private class VarDepth {
        int varIndex;
        int depth;

        VarDepth(int varIndex, int varDepth) {
            this.varIndex = varIndex;
            this.depth = varDepth;
        }
    }

    private class Distance {
        private int distance;
        private boolean infinite;

        Distance() {
            this.infinite = true;
        }

        Distance(int distance) {
            this.distance = distance;
            this.infinite = false;
        }

        boolean isInfinite() {
            return infinite;
        }

        int getDistance() {
            return distance;
        }
    }

    private class PairMinDistance {
        Distance minDistance;
        HashSet<VarDepth> varDistances;

        PairMinDistance(HashSet<VarDepth> varDistances, Distance minDistance) {
            this.varDistances = varDistances;
            this.minDistance = minDistance;
        }

        void incrementDepth() {
            for (VarDepth v: varDistances) {
                v.depth++;
            }
        }
    }


    private VarPair varPair;
    private VarList varList;
    private SearchType searchType;
    private FeatureReducer reducer;
    private double feature;

    public PairMinDistanceExtractor(ModulesFile modulesFile, VarPair varPair, SearchType searchType, FeatureReducer reducer) {
        super(modulesFile);

        this.varPair = varPair;
        this.reducer = reducer;
        this.searchType = searchType;
    }

    @Override
    public double extract() throws PrismLangException {
        if (varList != null) {
            return feature;
        }

        varList = getModulesFile().createVarList();

        int n = getModulesFile().getNumModules();
        for (int i = 0; i < n; i++) {
            Module module = getModulesFile().getModule(i);
            module.accept(this);
        }

        if (reducer.canReduce()) {
            double distance = reducer.reduce();
            feature = distance / (10 + distance);
        } else {
            feature = 1.0;
        }

        return feature;
    }

    public Object visit(parser.ast.Module e) throws PrismLangException {
        int n = e.getNumCommands();

        for (int i = 0; i < n; i++) {
            Command command = e.getCommand(i);
            command.accept(this);
        }

        return null;
    }

    public Object visit(Command e) throws PrismLangException {
        if (searchType == SearchType.UPDATE) {
            e.getUpdates().accept(this);
        } else if (searchType == SearchType.GUARD) {
            PairMinDistance md = (PairMinDistance) e.getGuard().accept(this);
            if (!md.minDistance.isInfinite()) {
                reducer.collect(md.minDistance.getDistance());
            }
        }

        return null;
    }

    public Object visit(Updates e) throws PrismLangException {
        int n = e.getNumUpdates();
        for (int i = 0; i < n; i++) {
            Update update = e.getUpdate(i);
            update.accept(this);
        }

        return null;
    }

    public Object visit(Update e) throws PrismLangException {
        int n = e.getNumElements();
        for(int i = 0; i < n; i++) {
            Expression expr = e.getExpression(i);

            PairMinDistance md = (PairMinDistance) expr.accept(this);
            if (!md.minDistance.isInfinite()) {
                reducer.collect(md.minDistance.getDistance());
            }
        }

        return null;
    }

    public Object visit(ExpressionITE e) throws PrismLangException {
        PairMinDistance md1 = (PairMinDistance) e.getOperand1().accept(this);
        md1.incrementDepth();

        PairMinDistance md2 = (PairMinDistance) e.getOperand2().accept(this);
        md2.incrementDepth();

        PairMinDistance md3 = (PairMinDistance) e.getOperand3().accept(this);
        md3.incrementDepth();

        return merge(merge(md1, md2), md3);
    }

    public Object visit(ExpressionBinaryOp e) throws PrismLangException {
        PairMinDistance md1 = (PairMinDistance) e.getOperand1().accept(this);
        md1.incrementDepth();

        PairMinDistance md2 = (PairMinDistance) e.getOperand2().accept(this);
        md2.incrementDepth();

        return merge(md1, md2);
    }

    public Object visit(ExpressionUnaryOp e) throws PrismLangException {
        PairMinDistance md = (PairMinDistance) e.getOperand().accept(this);
        md.incrementDepth();
        return md;
    }

    public Object visit(ExpressionFunc e) throws PrismLangException {
        PairMinDistance md = new PairMinDistance(new HashSet<>(), new Distance());

        int n = e.getNumOperands();
        for (int i = 0; i < n; i++) {
            Expression op = e.getOperand(i);
            PairMinDistance md1 = (PairMinDistance) op.accept(this);
            md1.incrementDepth();
            md = merge(md, md1);
        }

        return md;
    }

    public Object visit(ExpressionLiteral e) throws PrismLangException {
        return new PairMinDistance(new HashSet<>(), new Distance());
    }

    public Object visit(ExpressionConstant e) throws PrismLangException {
        return new PairMinDistance(new HashSet<>(), new Distance());
    }

    public Object visit(ExpressionVar e) throws PrismLangException {
        HashSet<VarDepth> varSet = new HashSet<>();

        int varIndex = varList.getIndex(e.getName());
        if (varIndex == varPair.firstIndex || varIndex == varPair.secondIndex) {
            varSet.add(new VarDepth(varIndex, 0));
        }

        return new PairMinDistance(varSet, new Distance());
    }


    private Distance computeMinDistance(PairMinDistance md1, PairMinDistance md2) {
        Distance minDistance;

        if (!md1.minDistance.isInfinite() && !md2.minDistance.isInfinite()) {
            minDistance = new Distance(Math.min(md1.minDistance.getDistance(), md2.minDistance.getDistance()));
        } else if (!md1.minDistance.isInfinite()) {
            minDistance = new Distance(md1.minDistance.getDistance());
        } else if (!md2.minDistance.isInfinite()) {
            minDistance = new Distance(md2.minDistance.getDistance());
        } else {
            minDistance = new Distance();
        }

        for (VarDepth v1: md1.varDistances) {
            for (VarDepth v2: md2.varDistances) {
                if (v1.varIndex != v2.varIndex) {
                    int distance = v1.depth + v2.depth;
                    if (minDistance.isInfinite() || distance < minDistance.getDistance()) {
                        minDistance = new Distance(distance);
                    }
                }
            }
        }

        return minDistance;
    }

    private PairMinDistance merge(PairMinDistance md1, PairMinDistance md2) {
        Distance minDistance = computeMinDistance(md1, md2);

        HashSet<VarDepth> mergedSet = new HashSet<>();
        mergedSet.addAll(md1.varDistances);
        mergedSet.addAll(md2.varDistances);

        return new PairMinDistance(mergedSet, minDistance);
    }
}
