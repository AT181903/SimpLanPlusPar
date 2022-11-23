package mainPackage.SVM.ast;

import mainPackage.SVM.gen.SVMBaseVisitor;
import mainPackage.SVM.gen.SVMParser;
import mainPackage.SVM.gen.SVMLexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class SVMVisitorImpl extends SVMBaseVisitor<Void> {
    private final ArrayList<Instruction> code;
    private final HashMap<String, Integer> labelAdd;
    private final HashMap<Integer, String> labelRef;

    public SVMVisitorImpl() {
        this.code = new ArrayList<>();
        this.labelAdd = new HashMap<>();
        this.labelRef = new HashMap<>();
    }

    public ArrayList<Instruction> getCode() {
        return code;
    }

    @Override
    public Void visitAssembly(SVMParser.AssemblyContext ctx) {
        visitChildren(ctx);

        for (Entry<Integer, String> labelToJump : labelRef.entrySet()) {
            Integer codeLine = labelToJump.getKey();
            String label = labelToJump.getValue();
            Integer lineToJump = labelAdd.get(label);

            Instruction instructionToModify = code.get(codeLine);
            String instructionToModifyName = instructionToModify.getName();

            if (instructionToModifyName.equals("beq") || instructionToModifyName.equals("bleq")) {
                code.set(codeLine, new Instruction(instructionToModifyName, instructionToModify.getParam1(), instructionToModify.getParam2(), lineToJump.toString(), null));
            } else {
                // b and jal instructions
                code.set(codeLine, new Instruction(instructionToModifyName, lineToJump.toString(), null, null, null));
            }
        }

        return null;
    }

    @Override
    public Void visitInstruction(SVMParser.InstructionContext ctx) {
        switch (ctx.getStart().getType()) {
            case SVMLexer.PUSH:
                code.add(new Instruction("push", ctx.input.getText(), null, null, null));
                break;
            case SVMLexer.POP:
                code.add(new Instruction("pop", null, null, null, null));
                break;
            case SVMLexer.LOADW:
                code.add(new Instruction("lw", ctx.output.getText(), ctx.input.getText(), null, Integer.parseInt(ctx.NUMBER().getText())));
                break;
            case SVMLexer.STOREW:
                code.add(new Instruction("sw", ctx.input.getText(), ctx.output.getText(), null, Integer.parseInt(ctx.NUMBER().getText())));
                break;
            case SVMLexer.ADD:
                code.add(new Instruction("add", ctx.output.getText(), ctx.input1.getText(), ctx.input2.getText(), null));
                break;
            case SVMLexer.SUB:
                code.add(new Instruction("sub", ctx.output.getText(), ctx.input1.getText(), ctx.input2.getText(), null));
                break;
            case SVMLexer.MULT:
                code.add(new Instruction("mult", ctx.output.getText(), ctx.input1.getText(), ctx.input2.getText(), null));
                break;
            case SVMLexer.DIV:
                code.add(new Instruction("div", ctx.output.getText(), ctx.input1.getText(), ctx.input2.getText(), null));
                break;
            case SVMLexer.LABEL:
                labelAdd.put(ctx.LABEL().getText(), code.size());
                break;
            case SVMLexer.BRANCH:
                code.add(new Instruction("b", ctx.LABEL().getText(), null, null, null));

                labelRef.put(code.size() - 1, ctx.LABEL().getText());
                break;
            case SVMLexer.BRANCHEQ:
                code.add(new Instruction("beq", ctx.input1.getText(), ctx.input2.getText(), ctx.LABEL().getText(), null));
                labelRef.put(code.size() - 1, ctx.LABEL().getText());
                break;
            case SVMLexer.BRANCHLESSEQ:
                code.add(new Instruction("bleq", ctx.input1.getText(), ctx.input2.getText(), ctx.LABEL().getText(), null));
                labelRef.put(code.size() - 1, ctx.LABEL().getText());
                break;
            case SVMLexer.PRINT:
                code.add(new Instruction("print", ctx.input.getText(), null, null, null));
                break;
            case SVMLexer.AND:
                code.add(new Instruction("and", ctx.output.getText(), ctx.input1.getText(), ctx.input2.getText(), null));
                break;
            case SVMLexer.OR:
                code.add(new Instruction("or", ctx.output.getText(), ctx.input1.getText(), ctx.input2.getText(), null));
                break;
            case SVMLexer.NOT:
                code.add(new Instruction("not", ctx.output.getText(), ctx.input.getText(), null, null));
                break;
            case SVMLexer.ADDI:
                code.add(new Instruction("addi", ctx.output.getText(), ctx.input.getText(), ctx.NUMBER().getText(), null));
                break;
            case SVMLexer.SUBI:
                code.add(new Instruction("subi", ctx.output.getText(), ctx.input.getText(), ctx.NUMBER().getText(), null));
                break;
            case SVMLexer.MULTI:
                code.add(new Instruction("multi", ctx.output.getText(), ctx.input.getText(), ctx.NUMBER().getText(), null));
                break;
            case SVMLexer.DIVI:
                code.add(new Instruction("divi", ctx.output.getText(), ctx.input.getText(), ctx.NUMBER().getText(), null));
                break;
            case SVMLexer.LOADI:
                code.add(new Instruction("li", ctx.output.getText(), ctx.NUMBER().getText(), null, null));
                break;
            case SVMLexer.MOVE:
                code.add(new Instruction("mv", ctx.output.getText(), ctx.input.getText(), null, null));
                break;
            case SVMLexer.JUMPATLABEL:
                code.add(new Instruction("jal", ctx.LABEL().getText(), null, null, null));
                labelRef.put(code.size() - 1, ctx.LABEL().getText());
                break;
            case SVMLexer.JUMPATREGISTER:
                code.add(new Instruction("jr", ctx.input.getText(), null, null, null));
                break;
            case SVMLexer.HALT:
                code.add(new Instruction("halt", null, null, null, null));
                break;
            default:
                break;
        }
        return null;
    }
}
