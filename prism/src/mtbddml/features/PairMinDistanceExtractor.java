package mtbddml.features;

import mtbddml.VarPair;
import parser.VarList;
import parser.ast.*;
import prism.PrismLangException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PairMinDistanceExtractor extends VisitorFeatureExtractor {
    public enum SearchType {
        UPDATE, GUARD;
    }

    private class PairVarDistance {
        int varIndex;
        int varDepth;

        public PairVarDistance(int varIndex, int varDepth) {
            this.varIndex = varIndex;
            this.varDepth = varDepth;
        }
    }

    private VarPair varPair;
    private VarList varList;
    private double minDistance = 1.0;
    private SearchType searchType;

    public PairMinDistanceExtractor(ModulesFile modulesFile, VarPair varPair, SearchType searchType) {
        super(modulesFile);

        this.varPair = varPair;
        this.searchType = searchType;
    }

    @Override
    public double extract() throws PrismLangException {
        if (varList != null) {
            return minDistance;
        }

        varList = getModulesFile().createVarList();

        int n = getModulesFile().getNumModules();
        for (int i = 0; i < n; i++) {
            Module module = getModulesFile().getModule(i);
            module.accept(this);
        }

        return minDistance;
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
            e.getGuard().accept(this);
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
            ExpressionIdent ident = e.getVarIdent(i);
            int varIndex = varList.getIndex(ident.getName());

            if (varIndex == varPair.firstIndex || varIndex == varPair.secondIndex) {
                ArrayList<HashSet<PairVarDistance>> varSets = new ArrayList<>();

                HashSet<PairVarDistance> varSet = new HashSet<>();
                varSet.add(new PairVarDistance(varIndex, 1));

                varSets.add(varSet);

                Expression expr = e.getExpression(i);
                HashSet<PairVarDistance> exprSet = (HashSet<PairVarDistance>) expr.accept(this);
                incrementDepth(exprSet);

                varSets.add(exprSet);

                updateMinDistance(varSets);
            }
        }

        return null;
    }

    public Object visit(ExpressionITE e) throws PrismLangException {
        ArrayList<HashSet<PairVarDistance>> varSets = new ArrayList<>();

        HashSet<PairVarDistance> opSet = (HashSet<PairVarDistance>) e.getOperand1().accept(this);
        incrementDepth(opSet);

        varSets.add(opSet);

        opSet = (HashSet<PairVarDistance>) e.getOperand2().accept(this);
        incrementDepth(opSet);

        varSets.add(opSet);

        opSet = (HashSet<PairVarDistance>) e.getOperand3().accept(this);
        incrementDepth(opSet);

        varSets.add(opSet);

        updateMinDistance(varSets);

        return merge(varSets);
    }

    public Object visit(ExpressionBinaryOp e) throws PrismLangException {
        ArrayList<HashSet<PairVarDistance>> varSets = new ArrayList<>();

        HashSet<PairVarDistance> opSet = (HashSet<PairVarDistance>) e.getOperand1().accept(this);
        incrementDepth(opSet);

        varSets.add(opSet);

        opSet = (HashSet<PairVarDistance>) e.getOperand2().accept(this);
        incrementDepth(opSet);

        varSets.add(opSet);

        updateMinDistance(varSets);

        return merge(varSets);
    }

    public Object visit(ExpressionUnaryOp e) throws PrismLangException {
        HashSet<PairVarDistance> varSet = (HashSet<PairVarDistance>) e.getOperand().accept(this);
        incrementDepth(varSet);
        return varSet;
    }

    public Object visit(ExpressionFunc e) throws PrismLangException {
        ArrayList<HashSet<PairVarDistance>> varSets = new ArrayList<>();

        int n = e.getNumOperands();
        for (int i = 0; i < n; i++) {
            Expression op = e.getOperand(i);
            HashSet<PairVarDistance> varSet = (HashSet<PairVarDistance>) op.accept(this);
            incrementDepth(varSet);
            varSets.add(varSet);
        }

        updateMinDistance(varSets);

        return merge(varSets);
    }

    public Object visit(ExpressionIdent e) throws PrismLangException {
        FormulaList formulas = getModulesFile().getFormulaList();
        int index = formulas.getFormulaIndex(e.getName());

        if (index == -1) {
            return false;
        }

        Expression expr = formulas.getFormula(index);
        return expr.accept(this);
    }

    public Object visit(ExpressionLiteral e) throws PrismLangException {
        return new HashSet<>();
    }

    public Object visit(ExpressionConstant e) throws PrismLangException {
        return new HashSet<>();
    }

    public Object visit(ExpressionVar e) throws PrismLangException {
        HashSet<PairVarDistance> varSet = new HashSet<>();

        int varIndex = varList.getIndex(e.getName());
        if (varIndex == varPair.firstIndex || varIndex == varPair.secondIndex) {
            varSet.add(new PairVarDistance(varIndex, 0));
        }

        return varSet;
    }

    private void incrementDepth(HashSet<PairVarDistance> varSet) {
        for (PairVarDistance alt: varSet) {
            alt.varDepth++;
        }
    }

    private void updateMinDistance(List<HashSet<PairVarDistance>> varSets) {
        int n = varSets.size();
        for (int i = 0; i < n; i++) {
            HashSet<PairVarDistance> s1 = varSets.get(i);
            for(int j = i + 1; j < n; j++) {
                HashSet<PairVarDistance> s2 = varSets.get(j);

                for (PairVarDistance p1: s1) {
                    for (PairVarDistance p2: s2) {
                        if (p1.varIndex != p2.varIndex) {
                            double x = Math.sqrt(p1.varDepth + p2.varDepth);
                            double distance = x / (1 + x);
                            if (distance < this.minDistance) {
                                this.minDistance = distance;
                            }
                        }
                    }
                }
            }
        }
    }

    private HashSet<PairVarDistance> merge(List<HashSet<PairVarDistance>> varSet) {
        HashSet<PairVarDistance> mergedSet = new HashSet<>();
        for (HashSet<PairVarDistance> alt: varSet) {
            mergedSet.addAll(alt);
        }
        return mergedSet;
    }
}
