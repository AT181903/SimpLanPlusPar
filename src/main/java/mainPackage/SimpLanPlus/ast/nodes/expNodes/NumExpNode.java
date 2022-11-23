package mainPackage.SimpLanPlus.ast.nodes.expNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.NumTypeNode;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class NumExpNode implements Node {
    private final Integer value;

    public NumExpNode(Integer value) {
        this.value = value;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String toPrint(String s) {
        return s + "Number: " + value + "\n";
    }

    @Override
    public Node typeCheck() {
        return new NumTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String codeGeneration() {
        return "li $a0 " + value + " ; Load " + value + " in $a0\n";
    }


}
