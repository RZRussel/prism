package mtbddml.features;

import parser.VarList;
import parser.ast.*;
import parser.visitor.ASTVisitor;
import prism.PrismLangException;

import java.util.HashSet;

public class VarDependencyExtractor extends VisitorFeatureExtractor {
    private int varIndex;
    private VarList varList;
    private double feature;

    public VarDependencyExtractor(ModulesFile modulesFile, int varIndex) {
        super(modulesFile);

        this.varIndex = varIndex;
    }

    @Override
    public double extract() throws PrismLangException {
        if (varList != null) {
            return feature;
        }

        varList = this.getModulesFile().createVarList();

        int moduleNum = getModulesFile().getNumModules();
        HashSet<String> varNames = new HashSet<>();
        for (int i = 0; i < moduleNum; i++) {
            Module module = getModulesFile().getModule(i);
            HashSet<String> moduleVarNames = (HashSet<String>) module.accept(this);
            if (moduleVarNames != null) {
                varNames.addAll(moduleVarNames);
            }
        }

        feature = (double) varNames.size() / (double) varList.getNumVars();
        return feature;
    }

    public Object visit(parser.ast.Module e) throws PrismLangException {
        int n = e.getNumCommands();

        HashSet<String> varNames = new HashSet<>();
        for (int i = 0; i < n; i++) {
            Command command = e.getCommand(i);
            HashSet<String> comVarNames = (HashSet<String>) command.accept(this);
            if (comVarNames != null) {
                varNames.addAll(comVarNames);
            }
        }

        return varNames;
    }

    public Object visit(Command e) throws PrismLangException {
        return e.getUpdates().accept(this);
    }

    public Object visit(Updates e) throws PrismLangException {
        HashSet<String> varNames = new HashSet<>();

        int n = e.getNumUpdates();
        for (int i = 0; i < n; i++) {
            Update update = e.getUpdate(i);
            HashSet<String> updVarNames = (HashSet<String>) update.accept(this);
            if (updVarNames != null) {
                varNames.addAll(updVarNames);
            }
        }

        return varNames;
    }

    public Object visit(Update e) throws PrismLangException {
        HashSet<String> varNames = new HashSet<>();

        int n = e.getNumElements();
        for(int i = 0; i < n; i++) {
            Expression expr = e.getExpression(i);
            if ((Boolean) expr.accept(this)) {
                varNames.add(e.getVarIdent(i).getName());
            }
        }

        return varNames;
    }

    public Object visit(RenamedModule e) throws PrismLangException {
        return null;
    }

    public Object visit(ExpressionITE e) throws PrismLangException {
        if ((Boolean) e.getOperand1().accept(this)) {
            return true;
        }

        if ((Boolean) e.getOperand2().accept(this)) {
            return true;
        }

        return (Boolean) e.getOperand3().accept(this);
    }

    public Object visit(ExpressionBinaryOp e) throws PrismLangException {
        if ((Boolean) e.getOperand1().accept(this)) {
            return true;
        }

        return (Boolean) e.getOperand2().accept(this);
    }

    public Object visit(ExpressionUnaryOp e) throws PrismLangException {
        return e.getOperand().accept(this);
    }

    public Object visit(ExpressionFunc e) throws PrismLangException {
        int n = e.getNumOperands();
        for (int i = 0; i < n; i++) {
            if ((Boolean) e.getOperand(i).accept(this)) {
                return true;
            }
        }

        return false;
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
        return false;
    }

    public Object visit(ExpressionConstant e) throws PrismLangException {
        return false;
    }

    public Object visit(ExpressionFormula e) throws PrismLangException {
        return e.getDefinition().accept(this);
    }

    public Object visit(ExpressionVar e) throws PrismLangException {
        return varList.getIndex(e.getName()) == varIndex;
    }
}
