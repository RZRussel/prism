package mtbddml.features;

import parser.VarList;
import parser.ast.*;
import parser.visitor.ASTVisitor;
import prism.PrismLangException;

import java.util.HashSet;

public class VarDependencyExtractor extends FeatureExtractor implements ASTVisitor {
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
        throw new PrismLangException("Extractor mustn't be applied to formula list element");
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
        throw new PrismLangException("Extractor mustn't be applied to system reference element");
    }

    public Object visit(ExpressionTemporal e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to temporal expression element");
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

    public Object visit(ExpressionProb e) throws PrismLangException {
        return false;
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
}
