package mainPackage.SimpLanPlus.ast.nodes.statementNodes;

import mainPackage.SimpLanPlus.ast.nodes.BlockNode;
import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.BoolTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.VoidTypeNode;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class IteNode extends StatementNode {
    private final Node condition;
    private final Node thenStatement;
    private final Node elseStatement;

    public IteNode(Node condition, Node thenStatement, Node elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    public ArrayList<Node> getThenAndElseStatements() {
        ArrayList<Node> thenAndElseStatements = new ArrayList<>();

        thenAndElseStatements.addAll(getNestedStatements(thenStatement));

        thenAndElseStatements.addAll(getNestedStatements(elseStatement));

        return thenAndElseStatements;
    }

    // Check nested statement in If and else block
    private ArrayList<Node> getNestedStatements(Node statement) {
        ArrayList<Node> nestedStatements = new ArrayList<>();

        if (statement instanceof BlockNode) {
            BlockNode block = (BlockNode) statement;
            nestedStatements.addAll(block.getStatementList());
        } else if (statement instanceof IteNode) {
            IteNode ite = (IteNode) statement;

            nestedStatements.addAll(getNestedStatements(ite.thenStatement));

            if (ite.elseStatement != null) {
                nestedStatements.addAll(getNestedStatements(ite.elseStatement));
            }
        } else if (statement instanceof RetNode) {
            nestedStatements.add(statement);
        }

        return nestedStatements;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        customErrorList.addAll(condition.checkSemantics(symbolTable));

        customErrorList.addAll(thenStatement.checkSemantics(symbolTable));

        if (elseStatement != null) {
            customErrorList.addAll(elseStatement.checkSemantics(symbolTable));
        }

        return customErrorList;
    }

    @Override
    public String toPrint(String s) {
        String iteString = s + "If" + "\n" + condition.toPrint(s + "  ")
                + thenStatement.toPrint(s + "  ");

        if (elseStatement != null) {
            iteString += s + "Else" + "\n" + elseStatement.toPrint(s + "  ");
        }

        return iteString;
    }

    @Override
    public Node typeCheck() {
        if (!(SimpLanPlusLib.isSametype(condition.typeCheck(), new BoolTypeNode()))) {
            new CustomException("Not boolean condition in if");
        }

        Node thenTypeCheck = thenStatement.typeCheck();

        if (elseStatement != null) {
            Node elseTypeCheck = elseStatement.typeCheck();

            if (SimpLanPlusLib.isSametype(thenTypeCheck, elseTypeCheck)) {
                return elseTypeCheck;
            } else {
                new CustomException("Incompatible types in then else branches");
            }
        }

        return new VoidTypeNode();
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        ArrayList<CustomError> errors = new ArrayList<>(condition.checkEffects(symbolTable));

        if (elseStatement == null) {
            errors.addAll(thenStatement.checkEffects(symbolTable));
        } else {
            // Build Then Statement symbolTable
            SymbolTable thenBranchST = new SymbolTable().replace(symbolTable);
            errors.addAll(thenStatement.checkEffects(thenBranchST));

            // Build else Statement symbolTable
            SymbolTable elseBranchST = new SymbolTable().replace(symbolTable);
            errors.addAll(elseStatement.checkEffects(elseBranchST));

            // Get max of then and else statement
            symbolTable.replace(SymbolTable.max(thenBranchST, elseBranchST));
        }

        return errors;
    }

    @Override
    public String codeGeneration() {
        StringBuilder generatedCode = new StringBuilder();
        String thenBranchLabel = SimpLanPlusLib.generateFreshLabel("thenBranch");
        String endIfLabel = "endIf" + thenBranchLabel;

        generatedCode.append("       ; BEGIN Ite").append("\n");

        generatedCode.append(condition.codeGeneration());

        generatedCode.append("li $t1 1 ; Load true in $t1\n");

        generatedCode.append("beq $a0 $t1 ").append(thenBranchLabel).append(" ; Compare condition with true\n");

        // Else branch -> If condition false
        if (elseStatement != null) {
            generatedCode.append("       ; BEGIN Else ").append("\n");
            generatedCode.append(elseStatement.codeGeneration());
            generatedCode.append("       ; END Else ").append("\n");
        }
        generatedCode.append("b ").append(endIfLabel).append(" ; If the condition was false skip to the end of if-then-else\n");

        // Then branch -> If condition true
        generatedCode.append(thenBranchLabel).append(":\n");
        generatedCode.append("       ; BEGIN Then ").append("\n");
        generatedCode.append(thenStatement.codeGeneration());
        generatedCode.append("       ; END Then ").append("\n");

        // End if
        generatedCode.append(endIfLabel).append(":\n");

        generatedCode.append("       ; END Ite").append("\n");

        return generatedCode.toString();
    }

    // Set label to end block (used for code generation)
    public void setEndBlockLabel(String endBlockLabel) {
        StatementNode thenStatementNode = (StatementNode) thenStatement;
        thenStatementNode.setEndBlockLabel(endBlockLabel);
        if (elseStatement != null) {
            StatementNode elseStatementNode = (StatementNode) elseStatement;
            elseStatementNode.setEndBlockLabel(endBlockLabel);
        }
    }
}
