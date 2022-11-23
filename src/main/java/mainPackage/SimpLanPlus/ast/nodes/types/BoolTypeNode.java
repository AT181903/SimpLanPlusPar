package mainPackage.SimpLanPlus.ast.nodes.types;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class BoolTypeNode extends TypeNode {
    public BoolTypeNode() {
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String toPrint(String s) {
        return s + "BoolType " + "\n";
    }

    @Override
    public Node typeCheck() {
        return this;
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String codeGeneration() {
        return "";
    }
}
