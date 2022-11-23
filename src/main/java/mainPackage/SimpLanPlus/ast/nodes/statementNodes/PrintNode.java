package mainPackage.SimpLanPlus.ast.nodes.statementNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.VoidTypeNode;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class PrintNode extends StatementNode {
    private final Node exp;

    public PrintNode(Node exp) {
        this.exp = exp;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return exp.checkSemantics(symbolTable);
    }

    @Override
    public String toPrint(String s) {
        return s + "Print" + "\n"
                + exp.toPrint(s + "  ");
    }

    @Override
    public Node typeCheck() {
        Node expType = exp.typeCheck();

        if (expType instanceof VoidTypeNode) {
            new CustomException("Type Error: need cannot print void expressions");
        }

        return new VoidTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return exp.checkEffects(symbolTable);
    }

    @Override
    public String codeGeneration() {

        return "       ; BEGIN Print \n" +
                exp.codeGeneration() +
                "print $a0\n" +
                "       ; END Print \n";

    }
}
