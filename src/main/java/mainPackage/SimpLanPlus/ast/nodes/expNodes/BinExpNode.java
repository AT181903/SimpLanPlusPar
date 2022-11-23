package mainPackage.SimpLanPlus.ast.nodes.expNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.types.BoolTypeNode;
import mainPackage.SimpLanPlus.ast.nodes.types.NumTypeNode;
import mainPackage.SimpLanPlus.utils.tools.SimpLanPlusLib;
import mainPackage.SimpLanPlus.utils.errors.CustomException;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public class BinExpNode implements Node {
    private final String operator;
    private final Node leftExp;
    private final Node rightExp;

    public BinExpNode(String operator, Node leftExp, Node rightExp) {
        this.operator = operator;
        this.leftExp = leftExp;
        this.rightExp = rightExp;
    }

    @Override
    public ArrayList<CustomError> checkSemantics(SymbolTable symbolTable) {
        ArrayList<CustomError> customErrorList = new ArrayList<>();

        customErrorList.addAll(leftExp.checkSemantics(symbolTable));
        customErrorList.addAll(rightExp.checkSemantics(symbolTable));

        return customErrorList;
    }

    private String getOperatorName() {
        switch (this.operator) {
            case "+":
                return "Plus";
            case "-":
                return "Minus";
            case "*":
                return "Mult";
            case "/":
                return "Div";
            case "<":
                return "Min";
            case "<=":
                return "MinEQ";
            case ">":
                return "Greater";
            case ">=":
                return "GreaterEQ";
            case "==":
                return "EQ";
            case "!=":
                return "Diff";
            case "&&":
                return "And";
            case "||":
                return "Or";
            default:
                return "Operator";
        }
    }


    @Override
    public String toPrint(String s) {
        return s + getOperatorName() + "\n"
                + leftExp.toPrint(s + "  ")
                + rightExp.toPrint(s + "  ");
    }

    @Override
    public Node typeCheck() {
        switch (this.operator) {
            case "+":
            case "-":
            case "*":
            case "/":
                if (!(SimpLanPlusLib.isSametype(leftExp.typeCheck(), new NumTypeNode()) &&
                        SimpLanPlusLib.isSametype(rightExp.typeCheck(), new NumTypeNode()))) {
                    new CustomException("Not integers in " + getOperatorName());
                }
                return new NumTypeNode();
            case "<":
            case "<=":
            case ">":
            case ">=":
                if (!(SimpLanPlusLib.isSametype(leftExp.typeCheck(), new NumTypeNode()) &&
                        SimpLanPlusLib.isSametype(rightExp.typeCheck(), new NumTypeNode()))) {
                    new CustomException("Not integers in " + getOperatorName());
                }
                return new BoolTypeNode();
            case "==":
            case "!=":
                if (!(SimpLanPlusLib.isSametype(leftExp.typeCheck(), rightExp.typeCheck()))) {
                    new CustomException("Incompatible types in " + getOperatorName());
                }
                return new BoolTypeNode();
            case "&&":
            case "||":
                if (!(SimpLanPlusLib.isSametype(leftExp.typeCheck(), new BoolTypeNode()) &&
                        SimpLanPlusLib.isSametype(rightExp.typeCheck(), new BoolTypeNode()))) {
                    new CustomException("Not bool in " + getOperatorName());
                }
                return new BoolTypeNode();
            default:
                return null;
        }
    }

    @Override
    public ArrayList<CustomError> checkEffects(SymbolTable symbolTable) {
        ArrayList<CustomError> errors = new ArrayList<>();

        errors.addAll(leftExp.checkEffects(symbolTable));
        errors.addAll(rightExp.checkEffects(symbolTable));

        return errors;
    }

    @Override
    public String codeGeneration() {
        StringBuilder generatedCode = new StringBuilder();

        generatedCode.append("       ; BEGIN ").append(getOperatorName()).append("\n");

        generatedCode.append(leftExp.codeGeneration());
        generatedCode.append("push $a0 ; Push e1 on the stack\n");
        generatedCode.append(rightExp.codeGeneration());
        generatedCode.append("lw $t1 0($sp) ; $t1 = e1, $a0 = e2\n");
        generatedCode.append("pop ; Pop e1 from the stack\n");

        switch (this.operator) {
            case "+":
                generatedCode.append("add $a0 $t1 $a0\n");
                break;
            case "-":
                generatedCode.append("sub $a0 $t1 $a0\n");
                break;
            case "*":
                generatedCode.append("mult $a0 $t1 $a0\n");
                break;
            case "/":
                generatedCode.append("div $a0 $t1 $a0\n");
                break;
            case "==":
                String trueBranchLabelEqualEqual = SimpLanPlusLib.generateFreshLabel("trueBranch");
                String endCheckLabelEqualEqual = "end" + trueBranchLabelEqualEqual;

                // If equals go to trueBranch
                generatedCode.append("beq $t1 $a0 ").append(trueBranchLabelEqualEqual).append("\n");

                // FALSE BRANCH

                // Return 0 if e1 != e2
                generatedCode.append("li $a0 0 ;$t1 != $a0\n");
                // After false branch go to end true branch
                generatedCode.append("b ").append(endCheckLabelEqualEqual).append("\n");

                // TRUE BRANCH

                generatedCode.append(trueBranchLabelEqualEqual).append(":\n");
                // Return 1 if e1 == e2
                generatedCode.append("li $a0 1 ;$t1 == $a0\n");

                // END CHECK
                generatedCode.append(endCheckLabelEqualEqual).append(":\n");

                break;
            case "!=":
                String trueBranchLabelDiff = SimpLanPlusLib.generateFreshLabel("trueBranch");
                String endCheckLabelDiff = "end" + trueBranchLabelDiff;

                // If equals go to trueBranch
                generatedCode.append("beq $t1 $a0 ").append(trueBranchLabelDiff).append("\n");

                // FALSE BRANCH

                // Return 1 if e1 != e2
                generatedCode.append("li $a0 1 ;$t1 != $a0\n");
                // After false branch go to end check
                generatedCode.append("b ").append(endCheckLabelDiff).append("\n");

                // TRUE BRANCH

                generatedCode.append(trueBranchLabelDiff).append(":\n");
                // Return 0 if e1 == e2
                generatedCode.append("li $a0 0 ;$t1 == $a0\n");

                // END CHECK
                generatedCode.append(endCheckLabelDiff).append(":\n");

                break;
            case "<":
                String equalTrueBranchLess = SimpLanPlusLib.generateFreshLabel("equalTrueBranch");
                String endEqualCheckLess = "end" + equalTrueBranchLess;

                String lesseqTrueBranchLess = SimpLanPlusLib.generateFreshLabel("lesseqTrueBranch");
                String endLesseqCheckLess = "end" + lesseqTrueBranchLess;

                generatedCode.append("beq $t1 $a0 ").append(equalTrueBranchLess).append("\n");

                // EQUAL FALSE BRANCH
                generatedCode.append("bleq $t1 $a0 ").append(lesseqTrueBranchLess).append("\n");

                // InnerFalse branch => e1 > e2
                generatedCode.append("li $a0 0 ;$t1 > $a0\n");
                generatedCode.append("b ").append(endLesseqCheckLess).append("\n");

                // InnerTrue branch => e1 < e2
                generatedCode.append(lesseqTrueBranchLess).append(":\n");
                // e1 < e2
                generatedCode.append("li $a0 1 ;$t1 < $a0\n");

                // End LesseqCheck branch
                generatedCode.append(endLesseqCheckLess).append(":\n");
                generatedCode.append("b ").append(endEqualCheckLess).append("\n");

                // EQUAL TRUE BRANCH
                generatedCode.append(equalTrueBranchLess).append(":\n");
                generatedCode.append("li $a0 0\n");

                // END EQUAL CHECK BRANCH
                generatedCode.append(endEqualCheckLess).append(":\n");

                break;
            case "<=":
                String trueBranchLabelLessEqual = SimpLanPlusLib.generateFreshLabel("lesseqTrueBranch");
                String endCheckLabelLessEqual = "end" + trueBranchLabelLessEqual;

                generatedCode.append("bleq $t1 $a0 ").append(trueBranchLabelLessEqual).append("\n");

                // FALSE BRANCH
                generatedCode.append("li $a0 0 ;$t1 > $a0\n");
                generatedCode.append("b ").append(endCheckLabelLessEqual).append("\n");

                // TRUE BRANCH
                generatedCode.append(trueBranchLabelLessEqual).append(":\n");
                generatedCode.append("li $a0 1 ;$t1 <= $a0\n");

                // END CHECK
                generatedCode.append(endCheckLabelLessEqual).append(":\n");

                break;
            case ">=":
                String equalTrueBranchGreaterEqual = SimpLanPlusLib.generateFreshLabel("equalTrueBranch");
                String endEqualCheckGreaterEqual = "end" + equalTrueBranchGreaterEqual;
                String greatereqTrueBranchGreaterEqual = SimpLanPlusLib.generateFreshLabel("lesseqTrueBranch");
                String endGreatereqCheckGreaterEqual = "end" + greatereqTrueBranchGreaterEqual;

                generatedCode.append("beq $t1 $a0 ").append(equalTrueBranchGreaterEqual).append("\n");

                // EQUAL FALSE BRANCH
                generatedCode.append("bleq $t1 $a0 ").append(greatereqTrueBranchGreaterEqual).append("\n");

                // InnerFalse branch => e1 > e2
                generatedCode.append("li $a0 1 ;$t1 > $a0\n");
                generatedCode.append("b ").append(endGreatereqCheckGreaterEqual).append("\n");

                // InnerTrue branch => e1 < e2
                generatedCode.append(greatereqTrueBranchGreaterEqual).append(":\n");
                // e1 < e2
                generatedCode.append("li $a0 0 ;$t1 <= $a0\n");

                // End LesseqCheck branch
                generatedCode.append(endGreatereqCheckGreaterEqual).append(":\n");
                generatedCode.append("b ").append(endEqualCheckGreaterEqual).append("\n");

                // EQUAL TRUE BRANCH
                generatedCode.append(equalTrueBranchGreaterEqual).append(":\n");
                // e1 == e2
                generatedCode.append("li $a0 1 ;$t1 == $a0\n");

                // END EQUAL CHECK BRANCH
                generatedCode.append(endEqualCheckGreaterEqual).append(":\n");
                break;
            case ">":
                String trueBranchLabelGreater = SimpLanPlusLib.generateFreshLabel("greaterTrueBranch");
                String endCheckLabelGreater = "end" + trueBranchLabelGreater;

                generatedCode.append("bleq $t1 $a0 ").append(trueBranchLabelGreater).append("\n");

                // FALSE BRANCH
                generatedCode.append("li $a0 1 ;$t1 > $a0\n");
                generatedCode.append("b ").append(endCheckLabelGreater).append("\n");

                // TRUE BRANCH
                generatedCode.append(trueBranchLabelGreater).append(":\n");
                generatedCode.append("li $a0 0 ;$t1 <= $a0\n");

                // END CHECK
                generatedCode.append(endCheckLabelGreater).append(":\n");

                break;
            case "&&":
                generatedCode.append("and $a0 $t1 $a0\n");
                break;
            case "||":
                generatedCode.append("or $a0 $t1 $a0\n");
                break;
            default:
                return null;
        }

        generatedCode.append("       ; END ").append(getOperatorName()).append("\n");

        return generatedCode.toString();
    }

}
