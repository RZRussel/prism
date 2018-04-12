package mtbddml;

import parser.VarList;
import parser.ast.*;
import parser.visitor.ASTVisitor;
import prism.PrismLangException;

import java.util.*;

/**
 * Created by russel on 09.04.18.
 */
public class InteractivePairExtractor extends PairExtractor implements ASTVisitor {
    private HashSet<VarPair> pairs;
    private VarList varList;

    public InteractivePairExtractor(ModulesFile modulesFile) {
        super(modulesFile);
    }

    public Set<VarPair> extract() throws PrismLangException {
        if (pairs != null) {
            return pairs;
        }

        varList = this.getModulesFile().createVarList();
        pairs = new HashSet<>();

        int moduleNum = getModulesFile().getNumModules();
        for (int i = 0; i < moduleNum; i++) {
            Module module = getModulesFile().getModule(i);
            module.accept(this);
        }

        return pairs;
    }

    // ASTElement classes (model/properties file)
    public Object visit(ModulesFile e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to modules file element");
    }

    public Object visit(PropertiesFile e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to property file element");
    }

    public Object visit(Property e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to property element");
    }

    public Object visit(FormulaList e) throws PrismLangException {
        int n = e.size();
        for(int i = 0; i < n; i++) {
            Expression exp = e.getFormula(i);
            exp.accept(this);
        }

        return null;
    }

    public Object visit(LabelList e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to labels list element");
    }

    public Object visit(ConstantList e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to constants list element");
    }

    public Object visit(Declaration e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to constants list element");
    }

    public Object visit(DeclarationInt e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to int declaration element");
    }

    public Object visit(DeclarationBool e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to bool declaration element");
    }

    public Object visit(DeclarationArray e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to declaration array element");
    }

    public Object visit(DeclarationClock e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to clock declaration element");
    }

    public Object visit(DeclarationIntUnbounded e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to unbound declaration element");
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
        HashSet<String> varNames = (HashSet<String>)e.getGuard().accept(this);
        if (varNames != null) {
            createPairs(varNames);
        }

        e.getUpdates().accept(this);
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
            HashSet<String> exprVarNames = (HashSet<String>) expr.accept(this);
            if (exprVarNames != null) {
                exprVarNames.add(e.getVarIdent(i).getName());
                createPairs(exprVarNames);
            }
        }

        return null;
    }

    public Object visit(RenamedModule e) throws PrismLangException {
        return null;
    }

    public Object visit(RewardStruct e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to reward structure element");
    }

    public Object visit(RewardStructItem e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to reward structure item element");
    }

    public Object visit(SystemInterleaved e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system inteleaved element");
    }

    public Object visit(SystemFullParallel e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system full parallel element");
    }

    public Object visit(SystemParallel e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system parallel element");
    }

    public Object visit(SystemHide e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system hide element");
    }

    public Object visit(SystemRename e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system rename element");
    }

    public Object visit(SystemModule e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system module element");
    }

    public Object visit(SystemBrackets e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system bracket element");
    }

    public Object visit(SystemReference e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to system refrence element");
    }

    public Object visit(ExpressionTemporal e) throws PrismLangException {
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
        HashSet<String> varNames = new HashSet<>();
        HashSet<String> opNames = (HashSet<String>) e.getOperand().accept(this);
        if (opNames != null) {
            varNames.addAll(opNames);
        }

        return varNames;
    }

    public Object visit(ExpressionFunc e) throws PrismLangException {
        HashSet<String> varNames = new HashSet<>();

        int n = e.getNumOperands();
        for (int i = 0; i < n; i++) {
            HashSet<String> opNames = (HashSet<String>) e.getOperand(i).accept(this);
            if (opNames != null) {
                varNames.addAll(opNames);
            }
        }

        return varNames;
    }

    public Object visit(ExpressionIdent e) throws PrismLangException {
        String name = e.getName();

        FormulaList formulas = getModulesFile().getFormulaList();
        int index = formulas.getFormulaIndex(name);
        if (index != -1) {
            Expression expr = formulas.getFormula(index);
            return expr.accept(this);
        }

        return null;
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
        HashSet<String> varNames = new HashSet<String>();
        varNames.add(e.getName());
        return varNames;
    }

    public Object visit(ExpressionProb e) throws PrismLangException {
        return null;
    }

    public Object visit(ExpressionReward e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to reward expression element");
    }

    public Object visit(ExpressionSS e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to quantum operator element");
    }

    public Object visit(ExpressionExists e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to temporal exists element");
    }

    public Object visit(ExpressionForAll e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to temporal forall element");
    }

    public Object visit(ExpressionStrategy e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to strategy element");
    }

    public Object visit(ExpressionLabel e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to label expression element");
    }

    public Object visit(ExpressionProp e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to property reference element");
    }

    public Object visit(ExpressionFilter e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to filter expression element");
    }
    // ASTElement classes (misc.)
    public Object visit(Filter e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to filter element");
    }

    public Object visit(ForLoop e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to for loop element");
    }

    private void createPairs(HashSet<String> varNames) {
        for(String varName1: varNames) {
            int varIndex1 = varList.getIndex(varName1);
            for(String varName2: varNames) {
                int varIndex2 = varList.getIndex(varName2);

                if (varIndex1 < varIndex2) {
                    pairs.add(new VarPair(varIndex1, varIndex2));
                }
            }
        }
    }
}
