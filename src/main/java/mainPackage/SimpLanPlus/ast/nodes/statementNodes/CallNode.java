package mainPackage.SimpLanPlus.ast.nodes.statementNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.VarNode;
import mainPackage.SimpLanPlus.ast.nodes.types.ArrowTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.RefTypeNode;
import mainPackage.SimpLanPlus.utils.symbol_table.Effect;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTableEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CallNode extends StatementNode {
    private final String name;
    private final ArrayList<Node> actualParameters;
    private SymbolTableEntry symbolTableEntry;

    private Integer currentNestingLevel;

    public CallNode(String name, ArrayList<Node> actualParameters) {
        this.name = name;
        this.actualParameters = actualParameters;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        SymbolTableEntry lookupResult = symbolTable.lookup(name);

        if (lookupResult == null) {
            customErrorList.add(new CustomError("Id " + name + " not declared"));
        } else {
            currentNestingLevel = symbolTable.getNestingLevel();
            this.symbolTableEntry = lookupResult;

            // Set function as Used
            this.symbolTableEntry.getStatus().setUsed(1);

            // Check Semantics for each actual parameter
            for (Node argument : actualParameters) {
                customErrorList.addAll(argument.checkSemantics(symbolTable));
            }

            // Check if number of actual parameters is correct
            ArrowTypeNode arrowTypeNode = (ArrowTypeNode) symbolTableEntry.getType();
            ArrayList<VarNode> formalParameters = arrowTypeNode.getArguments();
            if (!(formalParameters.size() == this.actualParameters.size())) {
                customErrorList.add(new CustomError("Wrong number of parameters in the invocation of " + name));
            }
        }

        return customErrorList;
    }

    @Override
    public String toPrint(String s) {
        StringBuilder argumentsString = new StringBuilder();

        for (Node argument : actualParameters) {
            argumentsString.append(argument.toPrint(s + "  "));
        }

        return s + "Call: " + name + "\n"
                + symbolTableEntry.toPrint(s + "  ")
                + argumentsString;
    }

    @Override
    public Node typeCheck() {
        ArrowTypeNode arrowTypeNode = (ArrowTypeNode) symbolTableEntry.getType();
        ArrayList<VarNode> formalParameters = arrowTypeNode.getArguments();

        for (int i = 0; i < this.actualParameters.size(); i++) {

            // If formal parameter is reference and actual parameter is variable
            if (!(SimpLanPlusLib.isSametype(this.actualParameters.get(i).typeCheck(), formalParameters.get(i).typeCheck()))) {
                new CustomException("Wrong type for " + (i + 1) + "-th parameter in the invocation of " + name);
            } else {
                if (formalParameters.get(i).getType() instanceof RefTypeNode && (!(actualParameters.get(i) instanceof VarNode))) {
                    new CustomException((i + 1) + "-th parameter in the invocation of " + name + " must be a variable");
                }
            }
        }

        return arrowTypeNode.getReturnType();
    }

    //


    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        ArrayList<CustomError> effectErrors = new ArrayList<>();

        if (actualParameters.size() > 0) {

            // Set RW as effect for parameters
            for (Node argument : actualParameters) {
                effectErrors.addAll(argument.checkEffects(symbolTable));
            }

            // Get formal parameters
            ArrowTypeNode arrowTypeNode = (ArrowTypeNode) symbolTableEntry.getType();
            ArrayList<VarNode> formalParameters = arrowTypeNode.getArguments();

            // Update effects for each parameter
            for (int i = 0; i < formalParameters.size(); i++) {
                Node actualParameter = actualParameters.get(i);
                if (actualParameter instanceof VarNode) {
                    // Get actual parameter if it's a Variable
                    VarNode actualParameterVar = (VarNode) actualParameter;

                    // Get acual parameter status and max reference level
                    Effect actualParameterStatus = actualParameterVar.getSymbolTableEntry().getStatus();
                    Integer actualParameterMaxRefLevel = actualParameterStatus.getMaxRefLevel();
                    Boolean actualParameterStatusIsUsed = actualParameterStatus.getUsed(actualParameterMaxRefLevel);

                    // Get formal parameter Status and max reference level
                    Effect formalParameterStatus = formalParameters.get(i).getSymbolTableEntry().getStatus();
                    Integer formalParameterMaxRefLevel = formalParameterStatus.getMaxRefLevel();

                    if (formalParameterMaxRefLevel > 1) {
                        actualParameterStatus.newDerefLevel(actualParameterMaxRefLevel + 1);

                        // Get formal parameter status and used
                        Integer formalParamterActualStatus = formalParameterStatus.getActualStatus(formalParameterMaxRefLevel);
                        Boolean formalParamterIsUsed = formalParameterStatus.getUsed(formalParameterMaxRefLevel);

                        // Set Formal parameter status to actual parameter
                        actualParameterStatus.newStatus(actualParameterMaxRefLevel + 1, formalParamterActualStatus);

                        // Set Formal parameter Used to actual parameter
                        if (actualParameterStatusIsUsed || formalParamterIsUsed) {
                            actualParameterStatus.setUsed(actualParameterMaxRefLevel + 1);
                        }
                    }
                }
            }
        }

        ArrowTypeNode arrowTypeNode = (ArrowTypeNode) symbolTableEntry.getType();
        ArrayList<VarNode> formalParameters = arrowTypeNode.getArguments();
        if (areThereRefVariable(formalParameters)) {

            // PART 1 : Set each parameter as RW
            for (Node actualParameter : actualParameters) {
                VarNode actualParameterVar = (VarNode) actualParameter;

                if (!(actualParameterVar.getType() instanceof RefTypeNode)) {
                    effectErrors.addAll(actualParameterVar.checkEffects(symbolTable));
                }
            }

            // PART 2: check if each formal parameter is in error status
            for (VarNode formalParameter : formalParameters) {
                if (!(formalParameter.getType() instanceof RefTypeNode)) {
                    // Get formal parameter actual status
                    Effect formalParameterStatus = formalParameter.getSymbolTableEntry().getStatus();
                    Integer formalParameterMaxRefLevel = formalParameterStatus.getMaxRefLevel();
                    Integer formalParamterActualStatus = formalParameterStatus.getActualStatus(formalParameterMaxRefLevel);

                    if (formalParamterActualStatus.equals(Effect.top)) {
                        effectErrors.add(new CustomError("Formal parameter " + formalParameter.getName() + " was used erroneously inside the body of " + name + "."));
                    }
                }
            }

            // PART 3: par

            // Creating a copy of symbol table
            SymbolTable symbolTable1 = new SymbolTable().replace(symbolTable);

            // Create a new sybol table
            SymbolTable symbolTable2;

            List<SymbolTable> STforPar = new ArrayList<>();

            for (int i = 0; i < actualParameters.size(); i++) {
                VarNode actualParameterVar = (VarNode) actualParameters.get(i);

                if (actualParameterVar.getType() instanceof RefTypeNode) {
                    SymbolTable tmpST = new SymbolTable();
                    tmpST.newScope();

                    HashMap<String, SymbolTableEntry> STAtCurrentNL = symbolTable.getSymbolTableAtCurrentLevel();

                    SymbolTableEntry actualParameterVarST = actualParameterVar.getSymbolTableEntry();
                    SymbolTableEntry symbolTableEntry = new SymbolTableEntry(actualParameterVarST.getNestinglevel(), actualParameterVarST.getType(), symbolTable.getOffset());
                    symbolTable.incrementOffset();

                    STAtCurrentNL.put(actualParameterVar.getName(), symbolTableEntry);

                    // Get acual parameter status and max reference level
                    Effect actualParameterStatus = actualParameterVar.getSymbolTableEntry().getStatus();
                    Integer actualParameterMaxRefLevel = actualParameterStatus.getMaxRefLevel();
                    Boolean actualParameterStatusIsUsed = actualParameterStatus.getUsed(actualParameterMaxRefLevel);

                    // Get formal parameter Status and max reference level
                    Effect formalParameterStatus = formalParameters.get(i).getSymbolTableEntry().getStatus();
                    Integer formalParameterMaxRefLevel = formalParameterStatus.getMaxRefLevel();

                    if (formalParameterMaxRefLevel > 1) {
                        actualParameterStatus.newDerefLevel(actualParameterMaxRefLevel + 1);

                        // Get formal parameter status and used
                        Integer formalParamterActualStatus = formalParameterStatus.getActualStatus(formalParameterMaxRefLevel);
                        Boolean formalParamterIsUsed = formalParameterStatus.getUsed(formalParameterMaxRefLevel);

                        // Set Formal parameter status to actual parameter
                        actualParameterStatus.newStatus(actualParameterMaxRefLevel + 1, formalParamterActualStatus);

                        // Set Formal parameter Used to actual parameter
                        if (actualParameterStatusIsUsed || formalParamterIsUsed) {
                            actualParameterStatus.setUsed(actualParameterMaxRefLevel + 1);
                        }
                    }

                    STforPar.add(tmpST);
                }

                STforPar.add(symbolTable1);
            }


            symbolTable2 = STforPar.get(0);

            for (int i = 1; i < STforPar.size(); i++) {
                symbolTable2 = SymbolTable.par(symbolTable2, STforPar.get(i));
            }

            SymbolTable.update(symbolTable1, symbolTable2);

        }

        return effectErrors;
    }

    boolean areThereRefVariable(ArrayList<VarNode> formalParameters){
        boolean areThereFormalParamterRef = false;
        for (VarNode formalParameter : formalParameters) {
            if (formalParameter.getType().getReferenceLevel().equals(-1)) {
               return true;
            }
        }

        return areThereFormalParamterRef;
    }

    @Override
    public String codeGeneration() {
        StringBuilder generatedCode = new StringBuilder();

        generatedCode.append("       ; BEGIN Call of ").append(name).append("\n");

        generatedCode.append("push $fp ; Push old $fp\n");

        ArrowTypeNode arrowTypeNode = (ArrowTypeNode) symbolTableEntry.getType();
        ArrayList<VarNode> formalParameters = arrowTypeNode.getArguments();

        // For each parameter starting from last one
        for (int i = actualParameters.size() - 1; i >= 0; i--) {
            Node actualParameter = actualParameters.get(i);

            VarNode formalParameter = formalParameters.get(i);

            // If formal parameter is Ref don't push value but address
            if (formalParameter.getType() instanceof RefTypeNode) {
                VarNode actualParameterVarNode = (VarNode) actualParameter;
                if (actualParameterVarNode.getType() instanceof RefTypeNode) {
                    // Used for recursive call
                    generatedCode.append(actualParameterVarNode.codeGenerationForRefType(false));
                } else {
                    generatedCode.append(actualParameterVarNode.codeGenerationForRefType(true));
                }
            } else {
                generatedCode.append(actualParameter.codeGeneration());
            }

            generatedCode.append("push $a0 ; Push of ").append(actualParameter).append("\n");
        }

        generatedCode.append("mv $al $fp\n");

        for (int i = 0; i < (currentNestingLevel - symbolTableEntry.getNestinglevel()); i++) {
            generatedCode.append("lw $al 0($al)\n");
        }

        generatedCode.append("push $al\n");

        generatedCode.append("jal ").append(name).append(" ; Jump to function ").append(name).append("\n");

        generatedCode.append("       ; END Call of ").append(name).append("\n");

        return generatedCode.toString();
    }
}
