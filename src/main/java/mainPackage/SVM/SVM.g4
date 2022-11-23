grammar SVM;

assembly: instruction* ;

instruction:
      PUSH input = REGISTER
	  | POP
	  | LOADW output = REGISTER NUMBER '(' input = REGISTER ')'
	  | STOREW input = REGISTER NUMBER '(' output = REGISTER ')'
	  | ADD output = REGISTER input1 = REGISTER input2 = REGISTER
	  | SUB output = REGISTER input1 = REGISTER input2 = REGISTER
	  | MULT output = REGISTER input1 = REGISTER input2 = REGISTER
	  | DIV output = REGISTER input1 = REGISTER input2 = REGISTER
	  | LABEL COL
	  | BRANCH LABEL
	  | BRANCHEQ input1 = REGISTER input2 = REGISTER LABEL
	  | BRANCHLESSEQ input1 = REGISTER input2 = REGISTER LABEL
	  | PRINT input = REGISTER
	  | AND output = REGISTER input1 = REGISTER input2 = REGISTER
      | OR output = REGISTER input1 = REGISTER input2 = REGISTER
      | NOT output = REGISTER input = REGISTER
      | ADDI output = REGISTER input = REGISTER NUMBER
      | SUBI output = REGISTER input = REGISTER NUMBER
      | MULTI output = REGISTER input = REGISTER NUMBER
      | DIVI output = REGISTER input = REGISTER NUMBER
      | LOADI output = REGISTER NUMBER
      | MOVE output = REGISTER input = REGISTER
      | JUMPATLABEL LABEL
      | JUMPATREGISTER input = REGISTER
      | HALT;

REGISTER: '$a0'  // Accumulator is used to store the computed value of expressions
	    | '$t1'  // General purpose register is used to store temporary values
        | '$sp'  // Stack Pointer points to the top of the stack
	    | '$fp'  // Frame Pointer points to the current Access Link relative to the active frame
	    | '$al'  // Access Link is used to go through the static chain (i.e. scopes)
	    | '$ra'; // Return address stores the return address


AND            : 'and' ;
OR             : 'or';
NOT            : 'not';
ADDI           : 'addi';
SUBI           : 'subi' ;
MULTI          : 'multi';
DIVI           : 'divi' ;
LOADI          : 'li';
MOVE           : 'mv';
JUMPATLABEL    : 'jal';
JUMPATREGISTER : 'jr';
PUSH  	       : 'push' ; 	// pushes constant in the stack
POP	           : 'pop' ; 	// pops from stack
ADD	           : 'add' ;  	// add two values from the stack
SUB	           : 'sub' ;	// add two values from the stack
MULT	       : 'mult' ;  	// add two values from the stack
DIV	           : 'div' ;	// add two values from the stack
STOREW	       : 'sw' ; 	// store in the memory cell pointed by top the value next
LOADW	       : 'lw' ;	    // load a value from the memory cell pointed by top
BRANCH	       : 'b' ;	    // jump to label
BRANCHEQ       : 'beq' ;	// jump to label if top == next
BRANCHLESSEQ   : 'bleq' ;	// jump to label if top <= next
PRINT	       : 'print' ;	// print top of stack
HALT	       : 'halt' ;	// stop execution

COL	     : ':' ;
LABEL	 : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9')* ;
NUMBER	 : '0' | ('-')?(('1'..'9')('0'..'9')*) ;


WS              : (' '|'\t'|'\n'|'\r')-> skip;
LINECOMMENTS 	: ';' (~('\n'|'\r'))* -> skip;