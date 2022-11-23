package mainPackage.SimpLanPlus.utils.symbol_table;

import mainPackage.SimpLanPlus.ast.nodes.types.TypeNode;

public class SymbolTableEntry {
    private final int nestingLevel;
    private TypeNode type;
    private final int offset;

    public Effect getStatus() {
        return status;
    }

    private final Effect status;

    public SymbolTableEntry(int nestingLevel, TypeNode type, int offset) {
        this.nestingLevel = nestingLevel;
        this.type = type;
        this.offset = offset;
        this.status =  new Effect();
    }

    public TypeNode getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public int getNestinglevel() {
        return nestingLevel;
    }

    public String toPrint(String s) { //
        return s + "STentry: nestlev " + nestingLevel + "\n" +
                s + "STentry: type " + type.toPrint(s) +
                s + "STentry: offset " + offset + "\n";
    }

    public void setType(TypeNode type){
        this.type = type;
    }
}  