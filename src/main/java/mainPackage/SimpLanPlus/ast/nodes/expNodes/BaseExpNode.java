package mainPackage.SimpLanPlus.ast.nodes.expNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class BaseExpNode implements Node {
    private final Node exp;

    public BaseExpNode(Node exp) {
        this.exp = exp;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return exp.checkSemantics(symbolTable);
    }

    @Override
    public String toPrint(String s) {
        return s + exp.toPrint(s + "  ");
    }

    @Override
    public Node typeCheck() {
        return exp.typeCheck();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>(exp.checkEffects(symbolTable));
    }

    @Override
    public String codeGeneration() {
        return exp.codeGeneration();
    }


}
