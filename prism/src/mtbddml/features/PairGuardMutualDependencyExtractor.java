package mtbddml.features;


import mtbddml.VarPair;
import parser.VarList;
import parser.ast.*;
import prism.PrismLangException;

import java.util.HashSet;

public class PairGuardMutualDependencyExtractor extends VisitorFeatureExtractor {
    private VarPair varPair;
    private VarList varList;
    private int matchedGuardsCount = 0;
    private int totalGuardsCount = 0;

    public PairGuardMutualDependencyExtractor(ModulesFile modulesFile, VarPair varPair) {
        super(modulesFile);

        this.varPair = varPair;
    }

    public double extract() throws PrismLangException {
        if (varList != null) {
            if (totalGuardsCount > 0) {
                return (double) matchedGuardsCount / (double) totalGuardsCount;
            } else {
                return 0.0;
            }
        }

        varList = getModulesFile().createVarList();

        int n = getModulesFile().getNumModules();
        for (int i = 0; i < n; i++) {
            Module module =  getModulesFile().getModule(i);
            module.accept(this);
        }

        if (totalGuardsCount > 0) {
            return (double) matchedGuardsCount / (double) totalGuardsCount;
        } else {
            return 0.0;
        }
    }

    public Object visit(parser.ast.Module e) throws PrismLangException {
        int n = e.getNumCommands();
        for (int i = 0; i < n; i++) {
            Command command = e.getCommand(i);
            command.accept(this);
        }

        totalGuardsCount += n;

        return null;
    }

    public Object visit(Command e) throws PrismLangException {
        String firstName = varList.getName(varPair.firstIndex);
        String secondName = varList.getName(varPair.secondIndex);

        HashSet<String> varNames = (HashSet<String>) e.getGuard().accept(this);
        if (varNames != null && varNames.contains(firstName) && varNames.contains(secondName)) {
            matchedGuardsCount += 1;
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
