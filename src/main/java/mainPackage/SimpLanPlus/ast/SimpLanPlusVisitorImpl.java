package mainPackage.SimpLanPlus.ast;

import mainPackage.SimpLanPlus.ast.nodes.BlockNode;
import mainPackage.SimpLanPlus.ast.nodes.FunNode;
import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.ast.nodes.VarNode;
import mainPackage.SimpLanPlus.ast.nodes.types.*;
import mainPackage.SimpLanPlus.ast.nodes.declarationNodes.DecFunNode;
import mainPackage.SimpLanPlus.ast.nodes.declarationNodes.DecVarNode;
import mainPackage.SimpLanPlus.ast.nodes.expNodes.*;
import mainPackage.SimpLanPlus.ast.nodes.statementNodes.*;
import mainPackage.SimpLanPlus.ast.nodes.expNodes.*;
import mainPackage.SimpLanPlus.ast.nodes.statementNodes.*;
import mainPackage.SimpLanPlus.ast.nodes.types.*;
import mainPackage.SimpLanPlus.gen.SimpLanPlusBaseVisitor;
import mainPackage.SimpLanPlus.gen.SimpLanPlusParser.*;
import mainPackage.SimpLanPlus.utils.errors.CustomException;

import java.util.ArrayList;
import java.util.Objects;

public class SimpLanPlusVisitorImpl extends SimpLanPlusBaseVisitor<Node> {

    @Override
    public Node visitBlock(BlockContext ctx) {

        ArrayList<Node> declarationList = new ArrayList<>();
        ArrayList<Node> statementList = new ArrayList<>();

        for (DeclarationContext declarationContext : ctx.declaration()) {
            declarationList.add(visit(declarationContext));
        }

        for (StatementContext statementContext : ctx.statement()) {
            statementList.add(visitStatement(statementContext));
        }

        return new BlockNode(declarationList, statementList);
    }

    public Node visitStatement(StatementContext statementContext) {
        if (statementContext.assignment() != null) {
            return visit(statementContext.assignment());
        } else if (statementContext.print() != null) {
            return visit(statementContext.print());
        } else if (statementContext.ret() != null) {
            return visit(statementContext.ret());
        } else if (statementContext.ite() != null) {
            return visit(statementContext.ite());
        } else if (statementContext.call() != null) {
            return visit(statementContext.call());
        } else if(statementContext.block() != null){
            return visit(statementContext.block());
        } else {
            new CustomException("Error: Errore non riconosciuto da ANTLRv4: controllare l'input inserito");
            return null;
        }
    }

    @Override
    public Node visitDecFun(DecFunContext ctx) {
        String funName = ctx.ID().getText();

        ArrayList<VarNode> funArgumentNodes = new ArrayList<>();

        for (ArgContext argContext : ctx.arg()) {
            String argName = argContext.ID().getText();

            boolean isRef = argContext.getText().startsWith("var");

            String typeText = argContext.type().getText();

            TypeNode argTypeNode;
            if (Objects.equals(typeText, "int")) {
                if (isRef) {
                    argTypeNode = new RefTypeNode(new NumTypeNode());
                } else {
                    argTypeNode = new NumTypeNode();
                }
            } else {
                if (isRef) {
                    argTypeNode = new RefTypeNode(new BoolTypeNode());
                } else {
                    argTypeNode = new BoolTypeNode();
                }
            }

            VarNode argumentNode = new VarNode(argName, argTypeNode);
            funArgumentNodes.add(argumentNode);
        }

        BlockNode funBody = (BlockNode) visitBlock(ctx.block());

        TypeNode funType;

        if (ctx.getText().startsWith("void")) {
            funType = new VoidTypeNode();
        } else if (Objects.equals(ctx.type().getText(), "int")) {
            funType = new NumTypeNode();
        } else {
            funType = new BoolTypeNode();
        }

        return new DecFunNode(new FunNode(funName, funType, funArgumentNodes, funBody));
    }

    @Override
    public Node visitDecVar(DecVarContext ctx) {
        ExpContext expContext = ctx.exp();

        Node expNode = null;
        if (expContext != null) {
            expNode = visit(expContext);
        }

        TypeNode type;

        if (Objects.equals(ctx.type().getText(), "int")) {
            type = new NumTypeNode();
        } else {
            type = new BoolTypeNode();
        }

        VarNode id = new VarNode(ctx.ID().getText(), type);

        return new DecVarNode(id, expNode);
    }

    @Override
    public Node visitAssignment(AssignmentContext ctx) {
        VarNode id = new VarNode(ctx.ID().getText());

        Node expNode = visit(ctx.exp());

        return new AssignmentNode(id, expNode);
    }

    @Override
    public Node visitNumExp(NumExpContext ctx) {
        return new NumExpNode(Integer.parseInt(ctx.NUMBER().getText()));
    }

    @Override
    public Node visitBoolExp(BoolExpContext ctx) {
        return new BoolExpNode(Boolean.parseBoolean(ctx.BOOL().getText()));
    }

    @Override
    public Node visitVarExp(VarExpContext ctx) {
        return new VarNode(ctx.ID().getText());
    }

    @Override
    public Node visitCall(CallContext ctx) {
        String name = ctx.ID().getText();

        ArrayList<Node> argumentNodes = new ArrayList<>();

        for (ExpContext expContext : ctx.exp()) {
            argumentNodes.add(visit(expContext));
        }

        return new CallNode(name, argumentNodes);
    }

    @Override
    public Node visitPrint(PrintContext ctx) {
        return new PrintNode(visit(ctx.exp()));
    }

    @Override
    public Node visitRet(RetContext ctx) {
        Node exp = null;

        if (ctx.exp() != null) {
            exp = visit(ctx.exp());
        }

        return new RetNode(exp);
    }

    @Override
    public Node visitIte(IteContext ctx) {
        Node condition = visit(ctx.exp());

        Node ifStatement = null;
        if (ctx.statement().size() > 0) {
            ifStatement = visitStatement(ctx.statement().get(0));
        }

        // Check if else exists
        Node elseStatement = null;
        if (ctx.statement().size() == 2) {
            elseStatement = visitStatement(ctx.statement().get(1));
        }

        return new IteNode(condition, ifStatement, elseStatement);
    }

    @Override
    public Node visitBaseExp(BaseExpContext ctx) {
        return new BaseExpNode(visit(ctx.exp()));
    }

    @Override
    public Node visitNotExp(NotExpContext ctx) {
        return new NotExpNode(visit(ctx.exp()));
    }

    @Override
    public Node visitNegExp(NegExpContext ctx) {
        return new NegExpNode(visit(ctx.exp()));
    }

    @Override
    public Node visitBinExp(BinExpContext ctx) {
        return new BinExpNode(ctx.op.getText(), visit(ctx.left), visit(ctx.right));
    }
}
