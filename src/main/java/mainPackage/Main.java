package mainPackage;

import mainPackage.SVM.ast.SVMVisitorImpl;
import mainPackage.SVM.gen.SVMLexer;
import mainPackage.SVM.gen.SVMParser;
import mainPackage.SVM.interpreter.VM;
import mainPackage.SimpLanPlus.ast.SimpLanPlusVisitorImpl;
import mainPackage.SimpLanPlus.ast.nodes.BlockNode;
import mainPackage.SimpLanPlus.ast.nodes.Node;
import mainPackage.SimpLanPlus.gen.SimpLanPlusLexer;
import mainPackage.SimpLanPlus.gen.SimpLanPlusParser;
import mainPackage.SimpLanPlus.utils.errors.ErrorsListener;
import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        // Get file from resource directory
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("input.txt");
        assert inputStream != null;
        String inputString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        // Create error listener
        ErrorsListener errorsListener = new ErrorsListener();

        // Create lexer
        SimpLanPlusLexer lexer = new SimpLanPlusLexer(CharStreams.fromString(inputString));
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorsListener);

        // Create parser
        CommonTokenStream token = new CommonTokenStream(lexer);
        SimpLanPlusParser parser = new SimpLanPlusParser(token);
        parser.removeErrorListeners();
        parser.addErrorListener(errorsListener);

        // Visit
        SimpLanPlusVisitorImpl visitor = new SimpLanPlusVisitorImpl();
        Node ast = visitor.visit(parser.block());
        BlockNode mainBlock = (BlockNode) ast;
        mainBlock.setAsMainBlock();

        // Syntactic errors
        if (errorsListener.getSyntacticErrorSize() > 0) {
            errorsListener.printSyntacticErrorsToConsole();
            errorsListener.printSyntacticErrorsToFile();
        } else {
            SymbolTable symbolTable = new SymbolTable();
            ArrayList<CustomError> customErrorsList = ast.checkSemantics(symbolTable);

            errorsListener.setCustomErrorList(customErrorsList);
            errorsListener.printCustomErrorsToConsole();

            if (errorsListener.getSemanticErrorList().size() > 0) {
                errorsListener.printSemanticErrorsToFile();
            } else {
                // Print ast
                System.out.println("Visualizing AST...");
                System.out.println(ast.toPrint(""));

                // Type-checking
                Node typeNode = ast.typeCheck();
                System.out.println(typeNode.toPrint("Type checking ok! Type of the program is: "));

                // Check effects
                ArrayList<CustomError> effectsErrorList = ast.checkEffects(symbolTable);

                if (effectsErrorList.size() > 0) {
                    errorsListener.setCustomErrorList(effectsErrorList);
                } else {
                    // Code generation
                    String code = mainBlock.codeGeneration();

                    // Write generated code to file
                    FileWriter myWriter = new FileWriter("src/main/resources/generatedCode.txt");
                    myWriter.write(code);
                    myWriter.close();

                    // SVM
                    ErrorsListener errorsListenerSVM = new ErrorsListener();

                    // Create SVM lexer
                    SVMLexer lexerSVM = new SVMLexer(CharStreams.fromString(code));
                    lexerSVM.removeErrorListeners();
                    lexerSVM.addErrorListener(errorsListenerSVM);

                    // Create SVM parser
                    CommonTokenStream tokenSVM = new CommonTokenStream(lexerSVM);
                    SVMParser parserSVM = new SVMParser(tokenSVM);
                    parserSVM.removeErrorListeners();
                    parserSVM.addErrorListener(errorsListenerSVM);

                    // SVM Visitor
                    SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
                    visitorSVM.visit(parserSVM.assembly());

                    if (errorsListenerSVM.getSyntacticErrorSize() > 0) {
                        errorsListenerSVM.printSyntacticErrorsToConsole();
                    } else {
                        System.out.println("Starting Virtual Machine...");
                        VM vm = new VM(visitorSVM.getCode());
                        vm.execute();
                    }
                }

            }

        }

    }
}

