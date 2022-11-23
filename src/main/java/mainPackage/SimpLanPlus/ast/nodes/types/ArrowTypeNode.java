package mainPackage.SimpLanPlus.ast.nodes.types;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.VarNode;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class ArrowTypeNode extends TypeNode {

    private final TypeNode returnType;

    private final ArrayList<VarNode> arguments;

    public Node getReturnType() {
        return returnType;
    }

    public ArrayList<VarNode> getArguments() {
        return arguments;
    }

    public ArrowTypeNode(ArrayList<VarNode> arguments, TypeNode returnType) {
        this.arguments = arguments;
        this.returnType = returnType;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    public String toPrint(String s) {
        StringBuilder parlstr = new StringBuilder();

        for (Node par : arguments) {
            parlstr.append(par.toPrint(s + "  "));
        }

        return s + "ArrowType " + parlstr + returnType.toPrint(s + " ->");
    }

    @Override
    public Node typeCheck() {
        return null;
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