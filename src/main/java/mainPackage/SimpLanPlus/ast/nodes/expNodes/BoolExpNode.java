package mainPackage.SimpLanPlus.ast.nodes.expNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.BoolTypeNode;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class BoolExpNode implements Node {
    private final Boolean value;

    public BoolExpNode(Boolean value) {
        this.value = value;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String toPrint(String s) {
        return s + "Bool: " + value.toString() + "\n";
    }

    @Override
    public Node typeCheck() {
        return new BoolTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String codeGeneration() {
        int intValue = value ? 1 : 0;
        return "li $a0 " + intValue + "; Load " + intValue + " in $a0\n";
    }
}
