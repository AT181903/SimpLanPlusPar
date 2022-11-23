package mainPackage.SimpLanPlus.ast.nodes.statementNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.VarNode;
import mainPackage.SimpLanPlus.ast.nodes.types.RefTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.VoidTypeNode;
import mainPackage.SimpLanPlus.utils.symbol_table.Effect;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTableEntry;

import java.util.ArrayList;

public class AssignmentNode extends StatementNode {
    private final VarNode id;
    private final Node exp;

    private Integer currentNestingLevel;

    public AssignmentNode(VarNode id, Node exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        SymbolTableEntry lookupResult = symbolTable.lookup(id.getName());
        currentNestingLevel = symbolTable.getNestingLevel();

        // Check if id is never declared
        if (lookupResult == null) {
            customErrorList.add(new CustomError("Variable id " + id.getName() + " never declared"));
        } else {
            this.id.setSymbolTableEntry(lookupResult);
            this.id.setCurrentNestingLevel(symbolTable);

            // Set id to read write
            id.getSymbolTableEntry().getStatus().newStatus(id.getSymbolTableEntry().getStatus().getMaxRefLevel(), Effect.rw);
        }

        customErrorList.addAll(exp.checkSemantics(symbolTable));

        return customErrorList;
    }

    @Override
    public String toPrint(String s) {
        return s + "Id: " + id.getName() + "\n"
                + exp.toPrint(s + " ");
    }

    @Override
    public Node typeCheck() {
        if (!(SimpLanPlusLib.isSametype(exp.typeCheck(), id.typeCheck()))) {
            new CustomException("Invalid exp type in AssignmentNode");
        }

        return new VoidTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        ArrayList<CustomError> effectErrors = new ArrayList<>();

        effectErrors.addAll(id.checkEffects(symbolTable));
        effectErrors.addAll(exp.checkEffects(symbolTable));

        return effectErrors;
    }

    @Override
    public String codeGeneration() {
        StringBuilder generatedCode = new StringBuilder();

        generatedCode.append("       ; BEGIN Assignment of '").append(id.getName()).append("'\n");

        generatedCode.append(exp.codeGeneration());

        generatedCode.append("push $a0 ; Push exp\n");

        generatedCode.append("mv $al $fp\n");

        for (int i = 0; i < ((currentNestingLevel) - id.getSymbolTableEntry().getNestinglevel()); i++) {
            generatedCode.append("lw $al 0($al)\n");
        }

        int offsetWithAL = id.getSymbolTableEntry().getOffset() + 1;

        if (id.getType() instanceof RefTypeNode) {
            generatedCode.append("lw $a0 ").append(offsetWithAL).append("($al)").append("\n");

            generatedCode.append("lw $t1 0($sp) ; Load exp from top of the stack\n");

            generatedCode.append("pop\n");

            generatedCode.append("sw $t1 0($a0) ; Store at $a0 the addres of reference var ").append(id.getName()).append(" stored in $t1\n");
        } else {
            generatedCode.append("lw $t1 0($sp) ; Load exp from top of the stack\n");
            generatedCode.append("pop\n");

            generatedCode.append("sw $t1 ").append(offsetWithAL).append("($al) ; Store Exp in $al + offset\n");
        }

        generatedCode.append("       ; END Assignment of '").append(id.getName()).append("'\n");

        return generatedCode.toString();
    }
}
