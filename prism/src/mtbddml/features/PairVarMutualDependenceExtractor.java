package mtbddml.features;

import mtbddml.VarPair;
import parser.VarList;
import parser.ast.*;
import prism.PrismLangException;

import java.util.HashSet;

public class PairVarMutualDependenceExtractor extends VisitorFeatureExtractor {
    private VarPair varPair;
    private VarList varList;
    private HashSet<String> firstVarSet;
    private HashSet<String> secondVarSet;
    private double feature;

    public PairVarMutualDependenceExtractor(ModulesFile modulesFile, VarPair varPair) {
        super(modulesFile);

        this.varPair = varPair;
    }

    public double extract() throws PrismLangException {
        if (varList != null) {
            return feature;
        }

        varList = getModulesFile().createVarList();
        firstVarSet = new HashSet<>();
        secondVarSet = new HashSet<>();

        int n = getModulesFile().getNumModules();
        for (int i = 0; i < n; i++) {
            Module module = getModulesFile().getModule(i);
            module.accept(this);
        }

        int commonVarCount = 0;
        for(String varName: firstVarSet) {
            if (secondVarSet.contains(varName)) {
                commonVarCount++;
            }
        }

        feature = (double) commonVarCount / (double) varList.getNumVars();

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
        return e.getUpdates().accept(this);
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
            int index = varList.getIndex(e.getVarIdent(i).getName());

            if (index == varPair.firstIndex) {
                Expression expr = e.getExpression(i);
                HashSet<String> varNames = (HashSet<String>) expr.accept(this);
                if (varNames != null) {
                    firstVarSet.addAll(varNames);
                }
            }

            if (index == varPair.secondIndex) {
                Expression expr = e.getExpression(i);
                HashSet<String> varNames = (HashSet<String>) expr.accept(this);
                if (varNames != null) {
                    secondVarSet.addAll(varNames);
                }
            }
        }

        return null;
    }

    public Object visit(RenamedModule e) throws PrismLangException {
        return null;
    }

    public Object visit(ExpressionITE e) throws PrismLangException {
        HashSet<String> varNames = new HashSet<>();

        HashSet<String> opNames = (HashSet<String>) e.getOperand1().accept(this);
        if (opNames != null) {
            varNames.addAll(opNames);
        }

        opNames = (HashSet<String>) e.getOperand2().accept(this);
        if (opNames != null) {
            varNames.addAll(opNames);
        }

        opNames = (HashSet<String>) e.getOperand3().accept(this);
        if (opNames != null) {
            varNames.addAll(opNames);
        }

        return varNames;
    }

    public Object visit(ExpressionBinaryOp e) throws PrismLangException {
        HashSet<String> varNames = new HashSet<>();

        HashSet<String> opNames = (HashSet<String>) e.getOperand1().accept(this);
        if (opNames != null) {
            varNames.addAll(opNames);
        }

        opNames = (HashSet<String>) e.getOperand2().accept(this);
        if (opNames != null) {
            varNames.addAll(opNames);
        }

        return varNames;
    }

    public Object visit(ExpressionUnaryOp e) throws PrismLangException {
        return e.getOperand().accept(this);
    }

    public Object visit(ExpressionFunc e) throws PrismLangException {
        HashSet<String> varNames = new HashSet<>();
        int n = e.getNumOperands();
        for (int i = 0; i < n; i++) {
            HashSet<String> opVarNames = (HashSet<String>) e.getOperand(i).accept(this);
            if (opVarNames != null) {
                varNames.addAll(opVarNames);
            }
        }

        return varNames;
    }

    public Object visit(ExpressionIdent e) throws PrismLangException {
        FormulaList formulas = getModulesFile().getFormulaList();
        int index = formulas.getFormulaIndex(e.getName());

        if (index == -1) {
            return null;
        }

        Expression expr = formulas.getFormula(index);
        return expr.accept(this);
    }

    public Object visit(ExpressionLiteral e) throws PrismLangException {
        return null;
    }

    public Object visit(ExpressionConstant e) throws PrismLangException {
        return null;
    }

    public Object visit(ExpressionFormula e) throws PrismLangException {
        return e.getDefinition().accept(this);
    }

    public Object visit(ExpressionVar e) throws PrismLangException {
        HashSet<String> varNames = new HashSet<>();
        varNames.add(e.getName());
        return varNames;
    }

}
