package mainPackage.SimpLanPlus.ast.nodes;

import mainPackage.SimpLanPlus.ast.nodes.types.RefTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.TypeNode;
import mainPackage.SimpLanPlus.utils.symbol_table.Effect;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTableEntry;

import java.util.ArrayList;

public class VarNode implements Node {
    private final String name;

    private TypeNode type;

    private SymbolTableEntry symbolTableEntry;

    private Integer currentNestingLevel;

    public TypeNode getType() {
        if (symbolTableEntry == null) {
            return type;
        } else {
            return symbolTableEntry.getType();
        }
    }

    public void setSymbolTableEntry(SymbolTableEntry symbolTableEntry) {
        this.symbolTableEntry = symbolTableEntry;
    }

    public VarNode(String name, TypeNode type) {
        this.name = name;
        this.type = type;
    }

    public VarNode(String name) {
        this.name = name;
    }

    public SymbolTableEntry getSymbolTableEntry() {
        return symbolTableEntry;
    }

    public String getName() {
        return name;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        SymbolTableEntry lookupResult = symbolTable.lookup(name);
        currentNestingLevel = symbolTable.getNestingLevel();

        // Check var declaration
        if (lookupResult == null) {
            customErrorList.add(new CustomError("Variable id " + name + " never declared"));
        } else {
            this.symbolTableEntry = lookupResult;

            if (symbolTableEntry.getStatus().getActualStatus(symbolTableEntry.getStatus().getMaxRefLevel()).equals(Effect.bot)) {
                customErrorList.add(new CustomError("Variable " + name + " used but not initialized"));
            } else {
                symbolTableEntry.getStatus().setUsed(symbolTableEntry.getStatus().getMaxRefLevel());
                symbolTableEntry.getStatus().newStatus(symbolTableEntry.getStatus().getMaxRefLevel(), Effect.rw);
            }
        }

        return customErrorList;
    }

    public void setCurrentNestingLevel(SymbolTable symbolTable) {
        currentNestingLevel = symbolTable.getNestingLevel();
    }

    @Override
    public String toPrint(String s) {
        return s + "Id: " + name + "\n"
                + symbolTableEntry.toPrint(s + "  ");
    }

    @Override
    public Node typeCheck() {
        return symbolTableEntry.getType().typeCheck();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return new ArrayList<>();
    }

    @Override
    public String codeGeneration() {
        StringBuilder generatedCode = new StringBuilder();
        generatedCode.append("       ; BEGIN Var \n");

        generatedCode.append("mv $al $fp\n");

        for (int i = 0; i < ((currentNestingLevel) - symbolTableEntry.getNestinglevel()); i++) {
            generatedCode.append("lw $al 0($al)\n");
        }

        int offsetWithAL = symbolTableEntry.getOffset() + 1;

        generatedCode.append("lw $a0 ").append(offsetWithAL).append("($al) ; Loads in $a0 the value in ").append(name).append("\n");

        if (symbolTableEntry.getType() instanceof RefTypeNode) {
            generatedCode.append("lw $a0 0($a0) ; Store at $a0 the value of ref variable\n");
        }

        generatedCode.append("       ; END Var \n");

        return generatedCode.toString();
    }

    // Generaate ref variable code (don't push value but address)
    public String codeGenerationForRefType(boolean isFirstGeneration) {
        StringBuilder generatedCode = new StringBuilder();
        generatedCode.append("       ; BEGIN Var \n");

        generatedCode.append("mv $al $fp\n");

        for (int i = 0; i < ((currentNestingLevel) - symbolTableEntry.getNestinglevel()); i++) {
            generatedCode.append("lw $al 0($al)\n");
        }

        Integer offsetWithAL = symbolTableEntry.getOffset() + 1;

        if(isFirstGeneration) {
            generatedCode.append("addi $a0 $al ").append(offsetWithAL).append("\n");
        } else {
            generatedCode.append("lw $a0 ").append(offsetWithAL).append("($al) ; Loads in $a0 the value in ").append(name).append("\n");
        }

        generatedCode.append("       ; END Var \n");

        return generatedCode.toString();
    }

}
