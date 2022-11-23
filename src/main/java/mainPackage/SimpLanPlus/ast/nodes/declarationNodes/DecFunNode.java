package mainPackage.SimpLanPlus.ast.nodes.declarationNodes;

import mainPackage.SimpLanPlus.ast.nodes.BlockNode;
import mainPackage.SimpLanPlus.ast.nodes.FunNode;
import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.VarNode;
import mainPackage.SimpLanPlus.ast.nodes.types.RefTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.TypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.VoidTypeNode;
import mainPackage.SimpLanPlus.utils.symbol_table.Effect;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTableEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class DecFunNode implements Node {
    private final FunNode fun;

    public DecFunNode(FunNode fun) {
        this.fun = fun;
    }

    @Override
    public String toPrint(String s) {
        StringBuilder argumentsString = new StringBuilder();

        for (Node argument : fun.getArguments()) {
            argumentsString.append(argument.toPrint(s + "  "));
        }

        return s + "Fun: " + fun.getName() + "\n"
                + argumentsString
                + fun.getBody().toPrint(s + "  ");
    }

    @Override
    public Node typeCheck() {
        if (fun.getArguments() != null) {
            for (Node argument : fun.getArguments()) {
                argument.typeCheck();
            }
        }

        if (!(SimpLanPlusLib.isSametype(fun.getBody().typeCheck(), fun.getReturnType()))) {
            new CustomException("Wrong return type for function ");
        }

        return new VoidTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        return fun.checkEffects(symbolTable);
    }


    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        HashMap<String, SymbolTableEntry> STAtCurrentNL = symbolTable.getSymbolTableAtCurrentLevel();

        SymbolTableEntry symbolTableEntry = new SymbolTableEntry(symbolTable.getNestingLevel(), fun.getArrowTypeNode(), -1);

        // Check function name
        if (STAtCurrentNL.put(fun.getName(), symbolTableEntry) != null) { // Se c'è già un id nella ST corrente con questo name dà errore
            customErrorList.add(new CustomError("Function name " + fun.getName() + " already declared"));
        } else { // Altrimenti aggiunge la entry nella ST corrente
            fun.setSymbolTableEntry(symbolTableEntry);

            symbolTable.newScope();

            HashMap<String, SymbolTableEntry> symbolTableEntryHashMap = new HashMap<>();

            symbolTable.addToST(symbolTableEntryHashMap);

            for (VarNode functionArgument : fun.getArguments()) {
                String argumentName = functionArgument.getName();

                HashMap<String, SymbolTableEntry> NewSTAtCurrentNL = symbolTable.getSymbolTableAtCurrentLevel();
                SymbolTableEntry argumentSTEntry = new SymbolTableEntry(symbolTable.getNestingLevel(), functionArgument.getType(), symbolTable.getOffset());

                symbolTable.incrementOffset();

                // Check if parameter is already declared
                if (NewSTAtCurrentNL.put(argumentName, argumentSTEntry) != null) {
                    customErrorList.add(new CustomError("Parameter id " + argumentName + " already declared"));
                } else {
                    functionArgument.setSymbolTableEntry(argumentSTEntry);

                    functionArgument.checkSemantics(symbolTable);

                    TypeNode argumentType = functionArgument.getType();
                    Integer refLevel = argumentType.getReferenceLevel();

                    if (argumentType instanceof RefTypeNode) {
                        functionArgument.getSymbolTableEntry().getStatus().newDerefLevel(refLevel);
                    }

                    // Set parameter to Read-Write
                    functionArgument.getSymbolTableEntry().getStatus().newStatus(refLevel, Effect.rw);
                }
            }

            // Set Function Body Block as function block (for code generation)
            BlockNode blockNode = fun.getBody();
            fun.getBody().setAsFunctionBlock();

            customErrorList.addAll(blockNode.checkSemantics(symbolTable));

            // Set function as read write
            symbolTableEntry.getStatus().newStatus(1, Effect.rw);

            symbolTable.exitScope();

        }

        return customErrorList;
    }

    @Override
    public String codeGeneration() {
        String endFunLabel = SimpLanPlusLib.generateFreshLabel("endFun");

        StringBuilder generatedCode = new StringBuilder();
        String functionLabel = fun.getName();

        generatedCode.append("       ; BEGIN DecFun ").append("'").append(functionLabel).append("'").append("\n");

        generatedCode.append(functionLabel).append(":\n");

        generatedCode.append("mv $fp $sp").append("\n");

        generatedCode.append("push $ra ; Save return address\n");

        BlockNode body = fun.getBody();
        body.setAsFunctionBlock();
        body.setFunEndLabel(endFunLabel);
        generatedCode.append(body.codeGeneration());

        generatedCode.append(endFunLabel).append(":").append("\n");

        generatedCode.append("lw $ra 0($sp) ; Get return address from top of the stack\n");

        generatedCode.append("addi $sp $sp ").append(fun.getArguments().size() + 2).append(" ; Pop all arguments, $al and $ra\n");

        generatedCode.append("lw $fp 0($sp)").append("\n");

        generatedCode.append("pop").append(" ; Pop old $fp\n");

        // Jump to return
        generatedCode.append("jr $ra").append("\n");

        generatedCode.append("       ; END DecFun ").append("'").append(functionLabel).append("'").append("\n");

        return generatedCode.toString();
    }
}
