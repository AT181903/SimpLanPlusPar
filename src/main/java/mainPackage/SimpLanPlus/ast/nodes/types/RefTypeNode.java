package mainPackage.SimpLanPlus.ast.nodes.types;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class RefTypeNode extends TypeNode {
    private final TypeNode pointedType;

    public RefTypeNode(TypeNode pointedType) {
        this.pointedType = pointedType;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String toPrint(String s) {
        return s + "RefType " + "\n";
    }

    @Override
    public Node typeCheck() {
        return pointedType.typeCheck();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String codeGeneration() {
        return "";
    }

    public Integer getReferenceLevel(){
        return 1 + pointedType.getReferenceLevel();
    }
}
