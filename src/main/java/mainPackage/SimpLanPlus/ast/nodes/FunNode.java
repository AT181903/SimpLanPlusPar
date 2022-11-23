package mainPackage.SimpLanPlus.ast.nodes;

import mainPackage.SimpLanPlus.ast.nodes.types.ArrowTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.TypeNode;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTableEntry;

import java.util.ArrayList;

public class FunNode implements Node {
    private final String name;
    private final BlockNode body;
    private final ArrowTypeNode arrowTypeNode;

    public void setSymbolTableEntry(SymbolTableEntry symbolTableEntry) {
        this.symbolTableEntry = symbolTableEntry;
    }

    private SymbolTableEntry symbolTableEntry;
    public ArrowTypeNode getArrowTypeNode() {
        return arrowTypeNode;
    }

    public BlockNode getBody() {
        return body;
    }


    public String getName() {
        return name;
    }

    public Node getReturnType() {
        return arrowTypeNode.getReturnType();
    }

    public ArrayList<VarNode> getArguments() {
        return arrowTypeNode.getArguments();
    }

    public FunNode(String name, TypeNode returnType, ArrayList<VarNode> arguments, BlockNode body) {
        this.name = name;
        this.body = body;
        this.arrowTypeNode = new ArrowTypeNode(arguments, returnType);
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    public String toPrint(String s) {
        StringBuilder parlstr = new StringBuilder();

        for (Node par : arrowTypeNode.getArguments()) {
            parlstr.append(par.toPrint(s + "  "));
        }

        return s + "ArrowType " + parlstr + arrowTypeNode.getReturnType().toPrint(s + " ->");
    }

    @Override
    public Node typeCheck() {
        return null;
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>(body.checkEffects(symbolTable));
    }

    @Override
    public String codeGeneration() {
        return "";
    }


}
