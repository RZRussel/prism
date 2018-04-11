package mtbddml.features;

import parser.VarList;
import parser.ast.*;
import parser.visitor.ASTVisitor;
import prism.PrismLangException;

public class VarGuardDependencyExtractor extends VisitorFeatureExtractor {
    private int varIndex;
    private VarList varList;
    private int matchedGuardsCount = 0;
    private int totalGuardsCount = 0;

    public VarGuardDependencyExtractor(ModulesFile modulesFile, int varIndex) {
        super(modulesFile);

        this.varIndex = varIndex;
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
        if ((Boolean) e.getGuard().accept(this)) {
            matchedGuardsCount += 1;
        }

        return null;
    }

    public Object visit(Updates e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to updates element");
    }

    public Object visit(Update e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to update element");
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

        return e.getOperand3().accept(this);
    }

    public Object visit(ExpressionBinaryOp e) throws PrismLangException {
        if ((Boolean) e.getOperand1().accept(this)) {
            return true;
        }

        return e.getOperand2().accept(this);
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
