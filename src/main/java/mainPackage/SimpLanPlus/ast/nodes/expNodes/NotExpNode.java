package mainPackage.SimpLanPlus.ast.nodes.expNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.BoolTypeNode;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class NotExpNode implements Node {
    private final Node exp;

    public NotExpNode(Node exp) {
        this.exp = exp;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return exp.checkSemantics(symbolTable);
    }

    @Override
    public String toPrint(String s) {
        return s + "Not" + "\n" + exp.toPrint(s + "  ");
    }

    @Override
    public Node typeCheck() {
        if (!(SimpLanPlusLib.isSametype(exp.typeCheck(), new BoolTypeNode()))) {
            new CustomException("Exp type not bool in NotExpNode");
        }

        return new BoolTypeNode();

    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>(exp.checkEffects(symbolTable));
    }

    @Override
    public String codeGeneration() {
        return "       ; BEGIN NegExp \n" +
                exp.codeGeneration() +
                "not $a0 $a0" +
                "       ; END NegExp \n";

    }
}
