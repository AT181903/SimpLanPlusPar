package mainPackage.SimpLanPlus.ast.nodes.declarationNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.VarNode;
import mainPackage.SimpLanPlus.ast.nodes.types.VoidTypeNode;
import mainPackage.SimpLanPlus.utils.symbol_table.Effect;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTableEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class DecVarNode implements Node {
    private final VarNode id;
    private final Node exp;

    private Integer currentNestingLevel;

    public DecVarNode(VarNode id, Node exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        HashMap<String, SymbolTableEntry> STAtCurrentNL = symbolTable.getSymbolTableAtCurrentLevel();

        int nestingLevel = symbolTable.getNestingLevel();
        currentNestingLevel = symbolTable.getNestingLevel();
        int offset = symbolTable.getOffset();

        SymbolTableEntry symbolTableEntry = new SymbolTableEntry(nestingLevel, id.getType(), offset);
        symbolTable.incrementOffset();

        // Check if var name already exists, if not add it to current NL symbol table
        if (STAtCurrentNL.put(id.getName(), symbolTableEntry) != null) {
            customErrorList.add(new CustomError("Variable id " + id.getName() + " already declared"));
        } else {
            id.setSymbolTableEntry(symbolTableEntry);
            id.setCurrentNestingLevel(symbolTable);

            // Check exp
            if (exp != null) {
                customErrorList.addAll(exp.checkSemantics(symbolTable));
                // Set id as read write becouse there is an Exp
                id.getSymbolTableEntry().getStatus().newStatus(1, Effect.rw);
            }
        }

        return customErrorList;
    }

    // Check var nam in previous NL (used for parameters)
    public ArrayList<CustomError> checkDecVarInPreviousNL(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        HashMap<String, SymbolTableEntry> STAtPreviousNL = symbolTable.getSymbolTableAtPreviousLevel();

        if (STAtPreviousNL.get(id.getName()) != null) {
            customErrorList.add(new CustomError("Variable id " + id.getName() + " already declared"));
        }

        return customErrorList;
    }

    @Override
    public String toPrint(String s) {
        String expString = "";

        if (exp != null) {
            expString = exp.toPrint(s + "  ");
        }

        return s + "Var: " + id.getName() + "\n"
                + expString;
    }

    @Override
    public Node typeCheck() {
        if (exp != null) {
            if (!(SimpLanPlusLib.isSametype(exp.typeCheck(), id.getType()))) {
                new CustomException("Invalid exp type in DecVarNode");
            }
        }

        return new VoidTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        ArrayList<CustomError> errors = new ArrayList<>();

        if (exp != null) {
            errors.addAll(exp.checkEffects(symbolTable));
        }

        return errors;
    }


    @Override
    public String codeGeneration() {
        StringBuilder generatedCode = new StringBuilder();
        generatedCode.append("       ; BEGIN DecVar '").append(id.getName()).append("' \n");

        if (exp != null) {
            generatedCode.append(exp.codeGeneration());

            generatedCode.append("mv $al $fp\n");

            for (int i = 0; i < (currentNestingLevel - id.getSymbolTableEntry().getNestinglevel()); i++) {
                generatedCode.append("lw $al 0($al)\n");
            }


            generatedCode.append("sw $a0 ").append(id.getSymbolTableEntry().getOffset() + 1).append("($al) ; Store $a0 in $al + offset\n");
        }

        generatedCode.append("       ; END DecVar '").append(id.getName()).append("' \n");

        return generatedCode.toString();
    }
}
