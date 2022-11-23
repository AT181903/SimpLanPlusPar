package mainPackage.SimpLanPlus.ast.nodes;

import mainPackage.SimpLanPlus.utils.errors.CustomError;
import mainPackage.SimpLanPlus.utils.symbol_table.SymbolTable;

import java.util.ArrayList;

public interface Node {
  ArrayList<CustomError> checkSemantics(SymbolTable symbolTable);

  String toPrint(String s);
  
  Node typeCheck();

  ArrayList<CustomError> checkEffects(SymbolTable symbolTable);
  
  String codeGeneration();
}