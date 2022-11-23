package mainPackage.SimpLanPlus.utils.errors;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class ErrorsListener implements ANTLRErrorListener {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private final ArrayList<String> syntacticErrorList;

    public ArrayList<CustomError> getSemanticErrorList() {
        return semanticErrorList;
    }

    private final ArrayList<CustomError> semanticErrorList;
    private final ArrayList<CustomError> warnings;

    public ErrorsListener() {
        super();
        syntacticErrorList = new ArrayList<>();
        semanticErrorList = new ArrayList<>();
        warnings = new ArrayList<>();
    }

    public Integer getSyntacticErrorSize() {
        return syntacticErrorList.size();
    }

    public void setCustomErrorList(ArrayList<CustomError> customErrorList) {
        for (CustomError customError : customErrorList) {
            if (customError.isWarning) {
                this.warnings.add(customError);
            } else {
                this.semanticErrorList.add(customError);
            }
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String errorString = "Line: " + line + ", Char Position: " + charPositionInLine + ": " + msg + "\n";

        syntacticErrorList.add(errorString);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
    }

    public void printSyntacticErrorsToFile() {
        printErrorsToFile("syntactic_errors", this.syntacticErrorList);
    }

    public void printSemanticErrorsToFile() {
        printErrorsToFile("semantic_errors", this.semanticErrorList);
    }

    private void printErrorsToFile(String fileName, ArrayList<?> errorsList) {
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/" + fileName + ".txt");

            for (Object error : errorsList) {
                myWriter.write(error + "\n");
            }

            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printSyntacticErrorsToConsole() {
        System.err.println("You had " + syntacticErrorList.size() + " syntactic errors: ");

        printErrorsToConsole(syntacticErrorList);
    }

    public void printCustomErrorsToConsole() {
        if (semanticErrorList.size() > 0) {
            System.err.println("You had " + semanticErrorList.size() + " errors:");

            printErrorsToConsole(semanticErrorList);
        } else if (warnings.size() > 0) {
            printYellow("You had " + warnings.size() + " warnings:");

            printErrorsToConsole(warnings);
        }
    }

    private void printYellow(String msg) {
        System.err.println(ANSI_YELLOW + msg + ANSI_RESET);
    }

    public void printErrorsToConsole(ArrayList<?> errorsList) {
        for (Object error : errorsList) {
            if (error instanceof CustomError) {
                CustomError customError = (CustomError) error;
                if (customError.isWarning) {
                    printYellow(customError.msg);
                } else {
                    System.err.println(customError.msg);
                }
            } else {
                System.err.println(error);
            }
        }
    }
}
