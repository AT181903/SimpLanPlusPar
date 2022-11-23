package mainPackage.SimpLanPlus.ast.nodes;

import mainPackage.SimpLanPlus.ast.nodes.declarationNodes.DecFunNode;
import mainPackage.SimpLanPlus.ast.nodes.declarationNodes.DecVarNode;
import mainPackage.SimpLanPlus.ast.nodes.statementNodes.StatementNode;
import mainPackage.SimpLanPlus.ast.nodes.types.*;
import mainPackage.SimpLanPlus.ast.nodes.statementNodes.IteNode;
import mainPackage.SimpLanPlus.ast.nodes.statementNodes.RetNode;
import mainPackage.SimpLanPlus.ast.nodes.types.VoidTypeNode;
import mainPackage.SimpLanPlus.utils.symbol_table.Effect;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTableEntry;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import mainPackage.SimpLanPlus.ast.nodes.types.RefTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.TypeNode;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockNode extends StatementNode {

    private final ArrayList<Node> declarationList;

    public ArrayList<Node> getStatementList() {
        return statementList;
    }

    private final ArrayList<Node> statementList;

    private Boolean isMainBlock;
    private Boolean isFunctionBlock;
    private String endFunLabel;

    public void setEndBlockLabel(Node statement, String endBlockLabel) {
        StatementNode statementNode = (StatementNode) statement;
        statementNode.setEndBlockLabel(endBlockLabel);
    }

    public BlockNode(ArrayList<Node> declarationList, ArrayList<Node> statementList) {
        this.declarationList = declarationList;
        this.statementList = statementList;
        this.isMainBlock = false;
        this.isFunctionBlock = false;
    }

    public String toPrint(String s) {
        StringBuilder declarationString = new StringBuilder();

        for (Node declaration : declarationList) {
            declarationString.append(declaration.toPrint(s + " "));
        }

        StringBuilder statementString = new StringBuilder();
        for (Node statement : statementList) {
            statementString.append(statement.toPrint(s + " "));
        }

        return s + "Block\n" + declarationString + statementString;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        symbolTable.newScope();

        HashMap<String, SymbolTableEntry> symbolTableEntryHashMap = new HashMap<>();

        symbolTable.addToST(symbolTableEntryHashMap);

        ArrayList<CustomError> customErrorList = new ArrayList<>();

        ArrayList<DecVarNode> varDecList = new ArrayList<>();
        ArrayList<DecFunNode> funDecList = new ArrayList<>();

        // Separate Var dec from Fun dec
        for (Node declaration : declarationList) {
            if (declaration instanceof DecVarNode) {
                DecVarNode decVarNode = (DecVarNode) declaration;
                varDecList.add(decVarNode);
            } else if (declaration instanceof DecFunNode) {
                DecFunNode decFunNode = (DecFunNode) declaration;
                funDecList.add(decFunNode);
            }
        }


        // Check semantics in declaration list
        for (DecVarNode decVarNode : varDecList) {
            if (isFunctionBlock) {
                customErrorList.addAll(decVarNode.checkDecVarInPreviousNL(symbolTable));
            }
            customErrorList.addAll(decVarNode.checkSemantics(symbolTable));
        }

        for (DecFunNode decFunNode : funDecList) {
            customErrorList.addAll(decFunNode.checkSemantics(symbolTable));
        }

        // Check semantics in statementList list
        for (Node statement : statementList) {
            customErrorList.addAll(statement.checkSemantics(symbolTable));
        }

        customErrorList.addAll(symbolTable.checkForNotInitializedOrNotUsedVariables());

        // Leaving scope
        symbolTable.exitScope();

        // Return the result
        return customErrorList;
    }


    public Node typeCheck() {
        if (declarationList.size() > 0) {
            for (Node declaration : declarationList) {
                declaration.typeCheck();
            }
        }

        if (statementList.size() > 0) {
            for (Node statement : statementList) {
                statement.typeCheck();
            }

            Node returnNode = checkReturnInStatements(statementList);
            if (returnNode != null) {
                return returnNode.typeCheck();
            }
        }

        return new VoidTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        ArrayList<CustomError> effectErrors = new ArrayList<>();

        for (Node declaration : declarationList) {
            effectErrors.addAll(declaration.checkEffects(symbolTable));
        }

        for (Node statement : statementList) {
            effectErrors.addAll(statement.checkEffects(symbolTable));
        }

        return effectErrors;
    }

    // Check if there are return in ITE statements
    private RetNode checkReturnInStatements(ArrayList<Node> statements) {
        RetNode retNode = null;

        for (Node statement : statements) {
            if (statement instanceof RetNode) {
                retNode = (RetNode) statement;
                break;
            } else {
                if (statement instanceof IteNode) {
                    IteNode iteStatement = (IteNode) statement;

                    retNode = checkReturnInStatements(iteStatement.getThenAndElseStatements());
                }
            }
        }

        return retNode;
    }

    public void setAsMainBlock() {
        this.isMainBlock = true;
    }

    public void setAsFunctionBlock() {
        this.isFunctionBlock = true;
    }

    public void setFunEndLabel(String endFunLabel) {
        this.endFunLabel = endFunLabel;
    }

    public String codeGeneration() {
        String endBlockLabel = SimpLanPlusLib.generateFreshLabel("endBlock");
        String endFunctionDefLabel = SimpLanPlusLib.generateFreshLabel("endFunctionDefs");

        StringBuilder generatedCode = new StringBuilder();

        if (isMainBlock) {
            generatedCode.append("       ; BEGIN Main Block").append("\n");
        } else {
            generatedCode.append("       ; BEGIN Block").append("\n");
        }

        generatedCode.append("push $fp ; Push old $fp\n");

        ArrayList<DecVarNode> varDecList = new ArrayList<>();
        ArrayList<DecFunNode> funDecList = new ArrayList<>();

        for (Node declaration : declarationList) {
            if (declaration instanceof DecVarNode) {
                DecVarNode decVarNode = (DecVarNode) declaration;
                varDecList.add(decVarNode);
            } else if (declaration instanceof DecFunNode) {
                DecFunNode decFunNode = (DecFunNode) declaration;
                funDecList.add(decFunNode);
            }
        }

        generatedCode.append("subi $sp $sp ").append(varDecList.size()).append("\n");

        if (isMainBlock) {
            generatedCode.append("push $sp\n");
        } else {
            generatedCode.append("push $fp\n");
        }

        generatedCode.append("mv $fp $sp\n");

        generatedCode.append("li $t1 0\n");
        generatedCode.append("push $t1 ; Push $ra as PlaceHolder\n");

        for (DecVarNode decVarNode : varDecList) {
            generatedCode.append(decVarNode.codeGeneration());
        }

        for (Node statement : statementList) {
            setEndBlockLabel(statement, endBlockLabel);
            generatedCode.append(statement.codeGeneration());
        }

        generatedCode.append(endBlockLabel).append(":\n");

        if (isMainBlock) {
            generatedCode.append("halt\n");

            generatedCode.append("       ; END Main Block").append("\n");
        } else {
            generatedCode.append("addi $sp $sp ").append(varDecList.size() + 2).append(" ; Pop var declarations, $al and $ra\n");

            generatedCode.append("lw $fp 0($sp) ; Restore old $fp\n");

            generatedCode.append("pop ; Pop old $fp\n");

            if (funDecList.size() > 0) {
                generatedCode.append("b ").append(endFunctionDefLabel).append("\n");
            }

            generatedCode.append("       ; END Block").append("\n");
        }

        // Used for nested DecFun
        if (isFunctionBlock) {
            generatedCode.append("b ").append(endFunLabel).append("\n");
        }

        // Functions are declared at the end of the block in order to not interfer with program counter
        for (DecFunNode decFunNode : funDecList) {
            generatedCode.append(decFunNode.codeGeneration());
        }

        if (funDecList.size() > 0) {
            generatedCode.append(endFunctionDefLabel).append(":\n");
        }

        return generatedCode.toString();
    }


}