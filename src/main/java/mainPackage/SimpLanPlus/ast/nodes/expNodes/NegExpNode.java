package mainPackage.SimpLanPlus.ast.nodes.expNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.NumTypeNode;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class NegExpNode implements Node {
    private final Node exp;

    public NegExpNode(Node exp) {
        this.exp = exp;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return exp.checkSemantics(symbolTable);
    }

    @Override
    public String toPrint(String s) {
        return s + "Neg" + "\n"
                + exp.toPrint(s + "  ");
    }

    @Override
    public Node typeCheck() {
        if (!(SimpLanPlusLib.isSametype(exp.typeCheck(), new NumTypeNode()))) {
            new CustomException("Exp type not number in NegExpNode");
        }

        return new NumTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>(exp.checkEffects(symbolTable));
    }

    @Override
    public String codeGeneration() {
        return "       ; BEGIN NegExp \n" +
                exp.codeGeneration() +
                "multi $a0 $a0 -1 ; Mult $a0 with -1 and load it in $a0" + "\n" +
                "       ; END NegExp \n";

    }

}
