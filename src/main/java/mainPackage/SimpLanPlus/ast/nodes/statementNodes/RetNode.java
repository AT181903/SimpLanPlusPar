package mainPackage.SimpLanPlus.ast.nodes.statementNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.VoidTypeNode;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class RetNode extends StatementNode {
    private final Node exp;

    private String endBlockLabel;
    public RetNode(Node exp) {
        this.exp = exp;
    }

    public void setEndBlockLabel(String endBlockLabel) {
        this.endBlockLabel = endBlockLabel;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        if(exp != null){
            customErrorList.addAll(exp.checkSemantics(symbolTable));
        }

        return customErrorList;
    }

    @Override
    public String toPrint(String s) {
        String returnString = s + "Return\n";

        if( exp != null){
            returnString += exp.toPrint(s + "  ");
        }

        return returnString;
    }

    @Override
    public Node typeCheck() {
        if(exp == null){
            return new VoidTypeNode();
        }

        return exp.typeCheck();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        if (exp == null) {
            return new ArrayList<>();
        } else {
            return exp.checkEffects(symbolTable);
        }
    }

    @Override
    public String codeGeneration() {
        StringBuilder generatedCode = new StringBuilder();

        generatedCode.append("       ; ").append("BEGIN Return").append("'\n");

        if (exp != null) {
            generatedCode.append(exp.codeGeneration());
        }

        // Jump to end block because return reached
        if(this.endBlockLabel != null){
            generatedCode.append("b ").append(this.endBlockLabel).append("\n");
        }

        generatedCode.append("       ; ").append("END Return").append("'\n");

        return generatedCode.toString();
    }
}
