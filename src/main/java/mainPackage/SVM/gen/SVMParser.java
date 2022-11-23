// Generated from /Users/at181903/IdeaProjects/ProgettoCEI/src/main/java/mainPackage/SVM/SVM.g4 by ANTLR 4.10.1
package mainPackage.SVM.gen;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SVMParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, REGISTER=3, AND=4, OR=5, NOT=6, ADDI=7, SUBI=8, MULTI=9, 
		DIVI=10, LOADI=11, MOVE=12, JUMPATLABEL=13, JUMPATREGISTER=14, PUSH=15, 
		POP=16, ADD=17, SUB=18, MULT=19, DIV=20, STOREW=21, LOADW=22, BRANCH=23, 
		BRANCHEQ=24, BRANCHLESSEQ=25, PRINT=26, HALT=27, COL=28, LABEL=29, NUMBER=30, 
		WS=31, LINECOMMENTS=32;
	public static final int
		RULE_assembly = 0, RULE_instruction = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"assembly", "instruction"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", null, "'and'", "'or'", "'not'", "'addi'", "'subi'", 
			"'multi'", "'divi'", "'li'", "'mv'", "'jal'", "'jr'", "'push'", "'pop'", 
			"'add'", "'sub'", "'mult'", "'div'", "'sw'", "'lw'", "'b'", "'beq'", 
			"'bleq'", "'print'", "'halt'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "REGISTER", "AND", "OR", "NOT", "ADDI", "SUBI", "MULTI", 
			"DIVI", "LOADI", "MOVE", "JUMPATLABEL", "JUMPATREGISTER", "PUSH", "POP", 
			"ADD", "SUB", "MULT", "DIV", "STOREW", "LOADW", "BRANCH", "BRANCHEQ", 
			"BRANCHLESSEQ", "PRINT", "HALT", "COL", "LABEL", "NUMBER", "WS", "LINECOMMENTS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SVM.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SVMParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AssemblyContext extends ParserRuleContext {
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public AssemblyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assembly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener ) ((SVMListener)listener).enterAssembly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener ) ((SVMListener)listener).exitAssembly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SVMVisitor ) return ((SVMVisitor<? extends T>)visitor).visitAssembly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssemblyContext assembly() throws RecognitionException {
		AssemblyContext _localctx = new AssemblyContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(7);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AND) | (1L << OR) | (1L << NOT) | (1L << ADDI) | (1L << SUBI) | (1L << MULTI) | (1L << DIVI) | (1L << LOADI) | (1L << MOVE) | (1L << JUMPATLABEL) | (1L << JUMPATREGISTER) | (1L << PUSH) | (1L << POP) | (1L << ADD) | (1L << SUB) | (1L << MULT) | (1L << DIV) | (1L << STOREW) | (1L << LOADW) | (1L << BRANCH) | (1L << BRANCHEQ) | (1L << BRANCHLESSEQ) | (1L << PRINT) | (1L << HALT) | (1L << LABEL))) != 0)) {
				{
				{
				setState(4);
				instruction();
				}
				}
				setState(9);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionContext extends ParserRuleContext {
		public Token input;
		public Token output;
		public Token input1;
		public Token input2;
		public TerminalNode PUSH() { return getToken(SVMParser.PUSH, 0); }
		public List<TerminalNode> REGISTER() { return getTokens(SVMParser.REGISTER); }
		public TerminalNode REGISTER(int i) {
			return getToken(SVMParser.REGISTER, i);
		}
		public TerminalNode POP() { return getToken(SVMParser.POP, 0); }
		public TerminalNode LOADW() { return getToken(SVMParser.LOADW, 0); }
		public TerminalNode NUMBER() { return getToken(SVMParser.NUMBER, 0); }
		public TerminalNode STOREW() { return getToken(SVMParser.STOREW, 0); }
		public TerminalNode ADD() { return getToken(SVMParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(SVMParser.SUB, 0); }
		public TerminalNode MULT() { return getToken(SVMParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(SVMParser.DIV, 0); }
		public TerminalNode LABEL() { return getToken(SVMParser.LABEL, 0); }
		public TerminalNode COL() { return getToken(SVMParser.COL, 0); }
		public TerminalNode BRANCH() { return getToken(SVMParser.BRANCH, 0); }
		public TerminalNode BRANCHEQ() { return getToken(SVMParser.BRANCHEQ, 0); }
		public TerminalNode BRANCHLESSEQ() { return getToken(SVMParser.BRANCHLESSEQ, 0); }
		public TerminalNode PRINT() { return getToken(SVMParser.PRINT, 0); }
		public TerminalNode AND() { return getToken(SVMParser.AND, 0); }
		public TerminalNode OR() { return getToken(SVMParser.OR, 0); }
		public TerminalNode NOT() { return getToken(SVMParser.NOT, 0); }
		public TerminalNode ADDI() { return getToken(SVMParser.ADDI, 0); }
		public TerminalNode SUBI() { return getToken(SVMParser.SUBI, 0); }
		public TerminalNode MULTI() { return getToken(SVMParser.MULTI, 0); }
		public TerminalNode DIVI() { return getToken(SVMParser.DIVI, 0); }
		public TerminalNode LOADI() { return getToken(SVMParser.LOADI, 0); }
		public TerminalNode MOVE() { return getToken(SVMParser.MOVE, 0); }
		public TerminalNode JUMPATLABEL() { return getToken(SVMParser.JUMPATLABEL, 0); }
		public TerminalNode JUMPATREGISTER() { return getToken(SVMParser.JUMPATREGISTER, 0); }
		public TerminalNode HALT() { return getToken(SVMParser.HALT, 0); }
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener ) ((SVMListener)listener).enterInstruction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener ) ((SVMListener)listener).exitInstruction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SVMVisitor ) return ((SVMVisitor<? extends T>)visitor).visitInstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instruction);
		try {
			setState(93);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PUSH:
				enterOuterAlt(_localctx, 1);
				{
				setState(10);
				match(PUSH);
				setState(11);
				((InstructionContext)_localctx).input = match(REGISTER);
				}
				break;
			case POP:
				enterOuterAlt(_localctx, 2);
				{
				setState(12);
				match(POP);
				}
				break;
			case LOADW:
				enterOuterAlt(_localctx, 3);
				{
				setState(13);
				match(LOADW);
				setState(14);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(15);
				match(NUMBER);
				setState(16);
				match(T__0);
				setState(17);
				((InstructionContext)_localctx).input = match(REGISTER);
				setState(18);
				match(T__1);
				}
				break;
			case STOREW:
				enterOuterAlt(_localctx, 4);
				{
				setState(19);
				match(STOREW);
				setState(20);
				((InstructionContext)_localctx).input = match(REGISTER);
				setState(21);
				match(NUMBER);
				setState(22);
				match(T__0);
				setState(23);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(24);
				match(T__1);
				}
				break;
			case ADD:
				enterOuterAlt(_localctx, 5);
				{
				setState(25);
				match(ADD);
				setState(26);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(27);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(28);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				}
				break;
			case SUB:
				enterOuterAlt(_localctx, 6);
				{
				setState(29);
				match(SUB);
				setState(30);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(31);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(32);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				}
				break;
			case MULT:
				enterOuterAlt(_localctx, 7);
				{
				setState(33);
				match(MULT);
				setState(34);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(35);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(36);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				}
				break;
			case DIV:
				enterOuterAlt(_localctx, 8);
				{
				setState(37);
				match(DIV);
				setState(38);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(39);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(40);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				}
				break;
			case LABEL:
				enterOuterAlt(_localctx, 9);
				{
				setState(41);
				match(LABEL);
				setState(42);
				match(COL);
				}
				break;
			case BRANCH:
				enterOuterAlt(_localctx, 10);
				{
				setState(43);
				match(BRANCH);
				setState(44);
				match(LABEL);
				}
				break;
			case BRANCHEQ:
				enterOuterAlt(_localctx, 11);
				{
				setState(45);
				match(BRANCHEQ);
				setState(46);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(47);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				setState(48);
				match(LABEL);
				}
				break;
			case BRANCHLESSEQ:
				enterOuterAlt(_localctx, 12);
				{
				setState(49);
				match(BRANCHLESSEQ);
				setState(50);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(51);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				setState(52);
				match(LABEL);
				}
				break;
			case PRINT:
				enterOuterAlt(_localctx, 13);
				{
				setState(53);
				match(PRINT);
				setState(54);
				((InstructionContext)_localctx).input = match(REGISTER);
				}
				break;
			case AND:
				enterOuterAlt(_localctx, 14);
				{
				setState(55);
				match(AND);
				setState(56);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(57);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(58);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				}
				break;
			case OR:
				enterOuterAlt(_localctx, 15);
				{
				setState(59);
				match(OR);
				setState(60);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(61);
				((InstructionContext)_localctx).input1 = match(REGISTER);
				setState(62);
				((InstructionContext)_localctx).input2 = match(REGISTER);
				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 16);
				{
				setState(63);
				match(NOT);
				setState(64);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(65);
				((InstructionContext)_localctx).input = match(REGISTER);
				}
				break;
			case ADDI:
				enterOuterAlt(_localctx, 17);
				{
				setState(66);
				match(ADDI);
				setState(67);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(68);
				((InstructionContext)_localctx).input = match(REGISTER);
				setState(69);
				match(NUMBER);
				}
				break;
			case SUBI:
				enterOuterAlt(_localctx, 18);
				{
				setState(70);
				match(SUBI);
				setState(71);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(72);
				((InstructionContext)_localctx).input = match(REGISTER);
				setState(73);
				match(NUMBER);
				}
				break;
			case MULTI:
				enterOuterAlt(_localctx, 19);
				{
				setState(74);
				match(MULTI);
				setState(75);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(76);
				((InstructionContext)_localctx).input = match(REGISTER);
				setState(77);
				match(NUMBER);
				}
				break;
			case DIVI:
				enterOuterAlt(_localctx, 20);
				{
				setState(78);
				match(DIVI);
				setState(79);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(80);
				((InstructionContext)_localctx).input = match(REGISTER);
				setState(81);
				match(NUMBER);
				}
				break;
			case LOADI:
				enterOuterAlt(_localctx, 21);
				{
				setState(82);
				match(LOADI);
				setState(83);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(84);
				match(NUMBER);
				}
				break;
			case MOVE:
				enterOuterAlt(_localctx, 22);
				{
				setState(85);
				match(MOVE);
				setState(86);
				((InstructionContext)_localctx).output = match(REGISTER);
				setState(87);
				((InstructionContext)_localctx).input = match(REGISTER);
				}
				break;
			case JUMPATLABEL:
				enterOuterAlt(_localctx, 23);
				{
				setState(88);
				match(JUMPATLABEL);
				setState(89);
				match(LABEL);
				}
				break;
			case JUMPATREGISTER:
				enterOuterAlt(_localctx, 24);
				{
				setState(90);
				match(JUMPATREGISTER);
				setState(91);
				((InstructionContext)_localctx).input = match(REGISTER);
				}
				break;
			case HALT:
				enterOuterAlt(_localctx, 25);
				{
				setState(92);
				match(HALT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001 `\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0001\u0000"+
		"\u0005\u0000\u0006\b\u0000\n\u0000\f\u0000\t\t\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001^\b\u0001\u0001"+
		"\u0001\u0000\u0000\u0002\u0000\u0002\u0000\u0000v\u0000\u0007\u0001\u0000"+
		"\u0000\u0000\u0002]\u0001\u0000\u0000\u0000\u0004\u0006\u0003\u0002\u0001"+
		"\u0000\u0005\u0004\u0001\u0000\u0000\u0000\u0006\t\u0001\u0000\u0000\u0000"+
		"\u0007\u0005\u0001\u0000\u0000\u0000\u0007\b\u0001\u0000\u0000\u0000\b"+
		"\u0001\u0001\u0000\u0000\u0000\t\u0007\u0001\u0000\u0000\u0000\n\u000b"+
		"\u0005\u000f\u0000\u0000\u000b^\u0005\u0003\u0000\u0000\f^\u0005\u0010"+
		"\u0000\u0000\r\u000e\u0005\u0016\u0000\u0000\u000e\u000f\u0005\u0003\u0000"+
		"\u0000\u000f\u0010\u0005\u001e\u0000\u0000\u0010\u0011\u0005\u0001\u0000"+
		"\u0000\u0011\u0012\u0005\u0003\u0000\u0000\u0012^\u0005\u0002\u0000\u0000"+
		"\u0013\u0014\u0005\u0015\u0000\u0000\u0014\u0015\u0005\u0003\u0000\u0000"+
		"\u0015\u0016\u0005\u001e\u0000\u0000\u0016\u0017\u0005\u0001\u0000\u0000"+
		"\u0017\u0018\u0005\u0003\u0000\u0000\u0018^\u0005\u0002\u0000\u0000\u0019"+
		"\u001a\u0005\u0011\u0000\u0000\u001a\u001b\u0005\u0003\u0000\u0000\u001b"+
		"\u001c\u0005\u0003\u0000\u0000\u001c^\u0005\u0003\u0000\u0000\u001d\u001e"+
		"\u0005\u0012\u0000\u0000\u001e\u001f\u0005\u0003\u0000\u0000\u001f \u0005"+
		"\u0003\u0000\u0000 ^\u0005\u0003\u0000\u0000!\"\u0005\u0013\u0000\u0000"+
		"\"#\u0005\u0003\u0000\u0000#$\u0005\u0003\u0000\u0000$^\u0005\u0003\u0000"+
		"\u0000%&\u0005\u0014\u0000\u0000&\'\u0005\u0003\u0000\u0000\'(\u0005\u0003"+
		"\u0000\u0000(^\u0005\u0003\u0000\u0000)*\u0005\u001d\u0000\u0000*^\u0005"+
		"\u001c\u0000\u0000+,\u0005\u0017\u0000\u0000,^\u0005\u001d\u0000\u0000"+
		"-.\u0005\u0018\u0000\u0000./\u0005\u0003\u0000\u0000/0\u0005\u0003\u0000"+
		"\u00000^\u0005\u001d\u0000\u000012\u0005\u0019\u0000\u000023\u0005\u0003"+
		"\u0000\u000034\u0005\u0003\u0000\u00004^\u0005\u001d\u0000\u000056\u0005"+
		"\u001a\u0000\u00006^\u0005\u0003\u0000\u000078\u0005\u0004\u0000\u0000"+
		"89\u0005\u0003\u0000\u00009:\u0005\u0003\u0000\u0000:^\u0005\u0003\u0000"+
		"\u0000;<\u0005\u0005\u0000\u0000<=\u0005\u0003\u0000\u0000=>\u0005\u0003"+
		"\u0000\u0000>^\u0005\u0003\u0000\u0000?@\u0005\u0006\u0000\u0000@A\u0005"+
		"\u0003\u0000\u0000A^\u0005\u0003\u0000\u0000BC\u0005\u0007\u0000\u0000"+
		"CD\u0005\u0003\u0000\u0000DE\u0005\u0003\u0000\u0000E^\u0005\u001e\u0000"+
		"\u0000FG\u0005\b\u0000\u0000GH\u0005\u0003\u0000\u0000HI\u0005\u0003\u0000"+
		"\u0000I^\u0005\u001e\u0000\u0000JK\u0005\t\u0000\u0000KL\u0005\u0003\u0000"+
		"\u0000LM\u0005\u0003\u0000\u0000M^\u0005\u001e\u0000\u0000NO\u0005\n\u0000"+
		"\u0000OP\u0005\u0003\u0000\u0000PQ\u0005\u0003\u0000\u0000Q^\u0005\u001e"+
		"\u0000\u0000RS\u0005\u000b\u0000\u0000ST\u0005\u0003\u0000\u0000T^\u0005"+
		"\u001e\u0000\u0000UV\u0005\f\u0000\u0000VW\u0005\u0003\u0000\u0000W^\u0005"+
		"\u0003\u0000\u0000XY\u0005\r\u0000\u0000Y^\u0005\u001d\u0000\u0000Z[\u0005"+
		"\u000e\u0000\u0000[^\u0005\u0003\u0000\u0000\\^\u0005\u001b\u0000\u0000"+
		"]\n\u0001\u0000\u0000\u0000]\f\u0001\u0000\u0000\u0000]\r\u0001\u0000"+
		"\u0000\u0000]\u0013\u0001\u0000\u0000\u0000]\u0019\u0001\u0000\u0000\u0000"+
		"]\u001d\u0001\u0000\u0000\u0000]!\u0001\u0000\u0000\u0000]%\u0001\u0000"+
		"\u0000\u0000])\u0001\u0000\u0000\u0000]+\u0001\u0000\u0000\u0000]-\u0001"+
		"\u0000\u0000\u0000]1\u0001\u0000\u0000\u0000]5\u0001\u0000\u0000\u0000"+
		"]7\u0001\u0000\u0000\u0000];\u0001\u0000\u0000\u0000]?\u0001\u0000\u0000"+
		"\u0000]B\u0001\u0000\u0000\u0000]F\u0001\u0000\u0000\u0000]J\u0001\u0000"+
		"\u0000\u0000]N\u0001\u0000\u0000\u0000]R\u0001\u0000\u0000\u0000]U\u0001"+
		"\u0000\u0000\u0000]X\u0001\u0000\u0000\u0000]Z\u0001\u0000\u0000\u0000"+
		"]\\\u0001\u0000\u0000\u0000^\u0003\u0001\u0000\u0000\u0000\u0002\u0007"+
		"]";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}