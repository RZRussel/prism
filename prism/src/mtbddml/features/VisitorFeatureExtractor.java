package mtbddml.features;

import parser.ast.*;
import parser.visitor.ASTVisitor;
import prism.PrismLangException;

public abstract class VisitorFeatureExtractor extends FeatureExtractor implements ASTVisitor {
    public VisitorFeatureExtractor(ModulesFile modulesFile) {
        super(modulesFile);
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
        throw new PrismLangException("Extractor mustn't be applied to module element");
    }

    public Object visit(Command e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to command element");
    }

    public Object visit(Updates e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to updates element");
    }

    public Object visit(Update e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to update element");
    }

    public Object visit(RenamedModule e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to renamed module element");
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
        throw new PrismLangException("Extractor mustn't be applied to ite element");
    }

    public Object visit(ExpressionBinaryOp e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to binary operation element");
    }

    public Object visit(ExpressionUnaryOp e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to unary operation element");
    }

    public Object visit(ExpressionFunc e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to expression func element");
    }

    public Object visit(ExpressionIdent e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to expression ident element");
    }

    public Object visit(ExpressionLiteral e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to expression literal element");
    }

    public Object visit(ExpressionConstant e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to expression constant element");
    }

    public Object visit(ExpressionFormula e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to expression formula element");
    }

    public Object visit(ExpressionVar e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to expression var element");
    }

    public Object visit(ExpressionProb e) throws PrismLangException {
        throw new PrismLangException("Extractor mustn't be applied to prob expression element");
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
