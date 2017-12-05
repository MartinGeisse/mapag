/* The following code was generated by JFlex 1.7.0-SNAPSHOT tweaked for IntelliJ platform */

package name.martingeisse.mapag.input;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

/**
 * This class is a scanner generated by <a href="http://www.jflex.de/">JFlex</a> 1.7.0-SNAPSHOT from the specification
 * file <tt>lexer.flex</tt>
 */
public class FlexGeneratedMapagLexer implements FlexLexer {

	/**
	 * This character denotes the end of file
	 */
	public static final int YYEOF = -1;
	/**
	 * lexical states
	 */
	public static final int YYINITIAL = 0;
	/* The ZZ_CMAP_Z table has 2176 entries */
	static final char ZZ_CMAP_Z[] = zzUnpackCMap(
		"\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1" +
			"\20\5\21\1\22\1\23\1\24\1\21\14\25\1\26\50\25\1\27\2\25\1\30\1\31\1\32\1\33" +
			"\25\25\1\34\20\21\1\35\1\36\1\37\1\40\1\41\1\42\1\43\1\21\1\44\1\45\1\46\1" +
			"\21\1\47\2\21\1\50\4\21\1\25\1\51\1\52\5\21\2\25\1\53\31\21\1\25\1\54\1\21" +
			"\1\55\40\21\1\56\17\21\1\57\1\60\1\61\1\62\13\21\1\63\10\21\123\25\1\64\7" +
			"\25\1\65\1\66\37\21\1\25\1\66\u0582\21\1\67\u017f\21");
	/* The ZZ_CMAP_Y table has 3584 entries */
	static final char ZZ_CMAP_Y[] = zzUnpackCMap(
		"\1\0\1\1\1\0\1\2\1\3\1\4\1\0\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\4\0" +
			"\1\16\1\17\1\20\1\21\2\10\1\22\3\10\1\22\71\10\1\23\1\10\1\24\1\25\1\26\1" +
			"\27\2\25\16\0\1\30\1\16\1\31\1\32\2\10\1\33\11\10\1\34\21\10\1\35\1\36\23" +
			"\10\1\25\1\37\3\10\1\22\1\40\1\37\4\10\1\41\1\42\4\0\1\43\1\44\1\25\3\10\2" +
			"\45\1\25\1\46\1\47\1\0\1\50\5\10\1\51\3\0\1\52\1\53\13\10\1\54\1\43\1\55\1" +
			"\56\1\0\1\57\1\25\1\60\1\61\3\10\3\0\1\62\12\10\1\63\1\0\1\64\1\25\1\0\1\65" +
			"\3\10\1\51\1\66\1\21\2\10\1\63\1\67\1\70\1\71\2\25\3\10\1\72\10\25\1\73\1" +
			"\26\6\25\1\74\2\0\1\75\1\76\6\10\1\77\2\0\1\100\1\10\1\101\1\0\2\37\1\102" +
			"\1\103\1\104\2\10\1\73\1\105\1\106\1\107\1\110\1\60\1\111\1\101\1\0\1\112" +
			"\1\47\1\102\1\11\1\104\2\10\1\73\1\113\1\114\1\115\1\116\1\117\1\120\1\121" +
			"\1\0\1\122\1\25\1\102\1\34\1\33\2\10\1\73\1\123\1\106\1\43\1\124\1\125\1\25" +
			"\1\101\1\0\1\40\1\25\1\102\1\103\1\104\2\10\1\73\1\123\1\106\1\107\1\116\1" +
			"\121\1\111\1\101\1\0\1\40\1\25\1\126\1\127\1\130\1\131\1\132\1\127\1\10\1" +
			"\133\1\134\1\135\1\136\1\25\1\121\1\0\1\25\1\40\1\102\1\30\1\73\2\10\1\73" +
			"\1\137\1\140\1\141\1\135\1\142\1\24\1\101\1\0\2\25\1\143\1\30\1\73\2\10\1" +
			"\73\1\137\1\106\1\141\1\135\1\142\1\31\1\101\1\0\1\144\1\25\1\143\1\30\1\73" +
			"\4\10\1\145\1\141\1\146\1\60\1\25\1\101\1\0\1\25\1\36\1\143\1\10\1\22\1\36" +
			"\2\10\1\33\1\147\1\22\1\150\1\151\1\0\2\25\1\152\1\25\1\37\5\10\1\153\1\154" +
			"\1\155\1\75\1\0\1\156\4\25\1\157\1\160\1\161\1\37\1\162\1\163\1\153\1\164" +
			"\1\165\1\166\1\0\1\167\4\25\1\125\2\25\1\156\1\0\1\156\1\170\1\171\1\10\1" +
			"\37\3\10\1\26\1\42\1\0\1\141\1\172\1\0\1\42\3\0\1\46\1\173\7\25\5\10\1\51" +
			"\1\0\1\174\1\0\1\156\1\63\1\175\1\176\1\177\1\200\1\10\1\201\1\202\1\0\1\166" +
			"\4\10\1\34\1\20\5\10\1\203\51\10\1\130\1\22\1\130\5\10\1\130\4\10\1\130\1" +
			"\22\1\130\1\10\1\22\7\10\1\130\10\10\1\204\4\25\2\10\2\25\12\10\1\26\1\25" +
			"\1\37\114\10\1\103\2\10\1\37\2\10\1\45\11\10\1\127\1\125\1\25\1\10\1\30\1" +
			"\205\1\25\2\10\1\205\1\25\2\10\1\206\1\25\1\10\1\30\1\207\1\25\6\10\1\210" +
			"\3\0\1\211\1\212\1\0\1\156\3\25\1\213\1\0\1\156\13\10\1\25\5\10\1\214\10\10" +
			"\1\215\1\25\3\10\1\26\1\0\1\2\1\0\1\2\1\121\1\0\3\10\1\215\1\26\1\25\5\10" +
			"\1\112\2\0\1\53\1\156\1\0\1\156\4\25\2\10\1\155\1\2\6\10\1\172\1\75\3\0\1" +
			"\107\1\0\1\156\1\0\1\156\1\41\13\25\1\216\5\10\1\210\1\0\1\216\1\112\1\0\1" +
			"\156\1\25\1\217\1\2\1\25\1\220\3\10\1\100\1\177\1\0\1\65\4\10\1\63\1\0\1\2" +
			"\1\25\4\10\1\210\2\0\1\25\1\0\1\221\1\0\1\65\3\10\1\215\12\25\1\222\2\0\1" +
			"\223\1\224\1\25\30\10\4\0\1\75\2\25\1\74\42\10\2\215\4\10\2\215\1\10\1\225" +
			"\3\10\1\215\6\10\1\30\1\165\1\226\1\26\1\227\1\112\1\10\1\26\1\226\1\26\1" +
			"\25\1\217\3\25\1\230\1\25\1\41\1\125\1\25\1\231\1\25\1\46\1\232\1\40\1\41" +
			"\2\25\1\10\1\26\3\10\1\45\2\25\1\0\1\46\1\233\1\0\1\234\1\25\1\235\1\36\1" +
			"\147\1\236\1\27\1\237\1\10\1\240\1\241\1\242\2\25\5\10\1\125\116\25\5\10\1" +
			"\22\5\10\1\22\20\10\1\26\1\243\1\244\1\25\4\10\1\34\1\20\7\10\1\41\1\25\1" +
			"\60\2\10\1\22\1\25\10\22\4\0\5\25\1\41\72\25\1\241\3\25\1\37\1\201\1\236\1" +
			"\26\1\37\11\10\1\22\1\245\1\37\12\10\1\203\1\241\4\10\1\215\1\37\12\10\1\22" +
			"\2\25\3\10\1\45\6\25\170\10\1\215\11\25\71\10\1\26\6\25\21\10\1\26\10\25\5" +
			"\10\1\215\41\10\1\26\2\10\1\0\1\244\2\25\5\10\1\155\1\74\1\246\3\10\1\60\12" +
			"\10\1\156\3\25\1\41\1\10\1\36\14\10\1\247\1\112\1\25\1\10\1\45\11\25\1\10" +
			"\1\250\1\251\2\10\1\51\2\25\1\125\6\10\1\112\1\25\1\65\5\10\1\210\1\0\1\46" +
			"\1\25\1\0\1\156\2\0\1\65\1\47\1\0\1\65\2\10\1\63\1\166\2\10\1\155\1\0\1\2" +
			"\1\25\3\10\1\26\1\76\5\10\1\51\1\0\1\234\1\41\1\0\1\156\4\25\5\10\1\100\1" +
			"\75\1\25\1\251\1\252\1\0\1\156\2\10\1\22\1\253\6\10\1\176\1\254\1\214\2\25" +
			"\1\255\1\10\1\51\1\256\1\25\3\257\1\25\2\22\22\25\4\10\1\51\1\260\1\0\1\156" +
			"\64\10\1\112\1\25\2\10\1\22\1\261\5\10\1\112\40\25\55\10\1\215\15\10\1\24" +
			"\4\25\1\22\1\25\1\261\1\262\1\10\1\73\1\22\1\165\1\263\15\10\1\24\3\25\1\261" +
			"\54\10\1\215\2\25\10\10\1\36\6\10\5\25\1\10\1\26\2\0\2\25\1\75\1\25\1\132" +
			"\2\25\1\241\3\25\1\40\1\30\20\10\1\264\1\231\1\25\1\0\1\156\1\37\2\10\1\11" +
			"\1\37\2\10\1\45\1\265\12\10\1\22\3\36\1\266\1\267\2\25\1\270\1\10\1\137\2" +
			"\10\1\22\2\10\1\271\1\10\1\215\1\10\1\215\4\25\17\10\1\45\10\25\6\10\1\26" +
			"\20\25\1\272\20\25\3\10\1\26\6\10\1\125\5\25\3\10\1\22\2\25\3\10\1\45\6\25" +
			"\3\10\1\215\4\10\1\112\1\10\1\236\5\25\23\10\1\215\1\0\1\156\52\25\1\215\1" +
			"\73\4\10\1\34\1\273\2\10\1\215\25\25\2\10\1\215\1\25\3\10\1\24\10\25\7\10" +
			"\1\265\10\25\1\274\1\74\1\137\1\37\2\10\1\112\1\115\4\25\3\10\1\26\20\25\6" +
			"\10\1\215\1\25\2\10\1\215\1\25\2\10\1\45\21\25\11\10\1\125\66\25\1\220\6\10" +
			"\1\0\1\75\3\25\1\121\1\0\2\25\1\220\5\10\1\0\1\275\2\25\3\10\1\125\1\0\1\156" +
			"\1\220\3\10\1\155\1\0\1\141\1\0\10\25\1\220\5\10\1\51\1\0\1\276\1\25\1\0\1" +
			"\156\24\25\5\10\1\51\1\0\1\25\1\0\1\156\46\25\55\10\1\22\22\25\14\10\1\45" +
			"\63\25\5\10\1\22\72\25\7\10\1\125\130\25\10\10\1\26\1\25\1\100\4\0\1\75\1" +
			"\25\1\60\1\220\1\10\14\25\1\24\153\25\1\277\1\300\2\0\1\301\1\2\3\25\1\302" +
			"\22\25\1\303\67\25\12\10\1\30\10\10\1\30\1\304\1\305\1\10\1\306\1\137\7\10" +
			"\1\34\1\307\2\30\3\10\1\310\1\165\1\36\1\73\51\10\1\215\3\10\1\73\2\10\1\203" +
			"\3\10\1\203\2\10\1\30\3\10\1\30\2\10\1\22\3\10\1\22\3\10\1\73\3\10\1\73\2" +
			"\10\1\203\1\311\6\0\1\137\3\10\1\157\1\37\1\203\1\312\1\235\1\313\1\157\1" +
			"\225\1\157\2\203\1\120\1\10\1\33\1\10\1\112\1\314\1\33\1\10\1\112\50\25\32" +
			"\10\1\22\5\25\106\10\1\26\1\25\33\10\1\215\74\25\1\117\3\25\14\0\20\25\36" +
			"\0\2\25");
	/* The ZZ_CMAP_A table has 1640 entries */
	static final char ZZ_CMAP_A[] = zzUnpackCMap(
		"\11\7\1\3\1\2\1\0\1\3\1\1\6\7\4\0\1\3\3\0\1\6\1\10\2\0\1\37\1\40\1\5\1\46" +
			"\1\41\2\0\1\4\2\7\1\43\1\42\1\0\1\44\1\0\1\45\1\0\4\6\1\34\11\6\1\33\13\6" +
			"\4\0\1\6\1\0\1\17\1\6\1\23\1\24\1\12\1\25\1\26\1\27\1\15\2\6\1\20\1\14\1\16" +
			"\1\30\1\22\1\6\1\13\1\21\1\11\1\32\1\31\4\6\1\35\1\47\1\36\1\0\1\7\2\0\4\6" +
			"\4\0\1\6\2\0\1\7\7\0\1\6\4\0\1\6\5\0\7\6\1\0\2\6\4\0\4\6\16\0\5\6\7\0\1\6" +
			"\1\0\1\6\1\0\5\6\1\0\2\6\6\0\1\6\1\0\3\6\1\0\1\6\1\0\4\6\1\0\13\6\1\0\3\6" +
			"\1\0\5\7\2\0\6\6\1\0\7\6\1\0\1\6\15\0\1\6\1\0\15\7\1\0\1\7\1\0\2\7\1\0\2\7" +
			"\1\0\1\7\3\6\5\0\5\7\6\0\1\6\4\0\3\7\5\0\3\6\7\7\4\0\2\6\1\7\13\6\1\0\1\6" +
			"\7\7\2\6\2\7\1\0\4\7\2\6\2\7\3\6\2\0\1\6\7\0\1\7\1\6\1\7\6\6\3\7\2\0\11\6" +
			"\3\7\1\6\6\0\2\7\6\6\4\7\2\6\2\0\2\7\1\6\11\7\1\6\3\7\1\6\5\7\2\0\1\6\3\7" +
			"\4\0\1\6\1\0\6\6\4\0\13\7\1\0\4\7\6\6\3\7\1\6\2\7\1\6\7\7\2\6\2\7\2\0\2\7" +
			"\1\0\3\7\1\0\10\6\2\0\2\6\2\0\6\6\1\0\1\6\3\0\4\6\2\0\1\7\1\6\7\7\2\0\2\7" +
			"\2\0\3\7\1\6\5\0\2\6\1\0\5\6\4\0\1\6\1\0\2\6\1\0\2\6\1\0\2\6\2\0\1\7\1\0\5" +
			"\7\4\0\2\7\2\0\3\7\3\0\1\7\7\0\4\6\1\0\1\6\7\0\4\7\3\6\1\7\2\0\1\6\1\0\2\6" +
			"\1\0\3\6\2\7\1\0\3\7\2\0\1\6\11\0\1\7\1\6\1\0\6\6\3\0\3\6\1\0\4\6\3\0\2\6" +
			"\1\0\1\6\1\0\2\6\3\0\2\6\3\0\2\6\4\0\5\7\3\0\3\7\1\0\4\7\2\0\1\6\6\0\1\7\4" +
			"\6\1\0\5\6\3\0\1\6\7\7\1\0\2\7\5\0\2\7\3\0\2\7\1\0\3\6\1\0\2\6\5\0\3\6\2\0" +
			"\1\6\3\7\1\0\4\7\1\6\1\0\4\6\1\0\1\6\4\0\1\7\4\0\6\7\1\0\1\7\3\0\2\7\4\0\1" +
			"\6\1\7\2\6\7\7\4\0\10\6\3\7\7\0\2\6\1\0\1\6\2\0\2\6\1\0\1\6\2\0\1\6\6\0\4" +
			"\6\1\0\3\6\1\0\1\6\1\0\1\6\2\0\2\6\1\0\3\6\2\7\1\0\2\7\1\6\2\0\5\6\1\0\1\6" +
			"\1\0\6\7\2\0\2\7\2\0\4\6\5\0\1\7\1\0\1\7\1\0\1\7\4\0\2\7\5\6\3\7\6\0\1\7\1" +
			"\0\7\7\1\6\2\7\4\6\3\7\1\6\3\7\2\6\7\7\3\6\4\7\5\6\14\7\1\6\1\7\3\6\1\0\7" +
			"\6\2\0\3\7\2\6\3\7\3\0\2\6\2\7\4\0\1\6\1\0\2\7\4\0\4\6\10\7\3\0\1\6\3\0\2" +
			"\6\1\7\5\0\3\7\2\0\1\6\1\7\1\6\5\0\6\6\2\0\5\7\3\6\3\0\10\7\5\6\2\7\3\0\3" +
			"\6\3\7\1\0\5\7\4\6\1\7\4\6\3\7\2\6\2\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6\2\0\3\6" +
			"\1\0\6\6\2\0\2\6\2\0\5\7\5\0\1\6\5\0\6\7\1\0\1\7\3\0\4\7\11\0\1\6\4\0\1\6" +
			"\1\0\5\6\2\0\1\6\1\0\4\6\1\0\3\6\2\0\4\6\5\0\5\6\4\0\1\6\4\0\4\6\3\7\2\6\5" +
			"\0\2\7\2\0\3\6\6\7\1\0\2\6\2\0\4\6\1\0\2\6\1\7\3\6\1\7\4\6\1\7\10\6\2\7\4" +
			"\0\1\6\1\7\4\0\1\7\5\6\2\7\3\0\3\6\4\0\3\6\2\7\2\0\6\6\1\0\3\7\1\0\2\7\5\0" +
			"\5\6\5\0\1\6\1\7\3\6\1\0\2\6\1\0\7\6\2\0\1\7\6\0\2\6\2\0\3\6\3\0\2\6\3\0\2" +
			"\6\2\0\3\7\4\0\3\6\1\0\2\6\1\0\1\6\5\0\1\7\2\0\1\6\3\0\1\6\2\0\2\6\3\7\1\0" +
			"\2\7\1\0\3\7\2\0\1\7\2\0\1\7\4\6\10\0\5\7\3\0\6\7\2\0\3\7\2\0\4\7\4\0\3\7" +
			"\5\0\1\6\2\0\2\6\2\0\4\6\1\0\4\6\1\0\1\6\1\0\6\6\2\0\5\6\1\0\4\6\1\0\4\6\2" +
			"\0\2\7\1\0\1\6\1\0\1\6\5\0\1\6\1\0\1\6\1\0\3\6\1\0\3\6\1\0\3\6");
	/**
	 * initial size of the lookahead buffer
	 */
	private static final int ZZ_BUFFERSIZE = 16384;
	/**
	 * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l ZZ_LEXSTATE[l+1] is the state in the DFA for the
	 * lexical state l at the beginning of a line l is of the form l = 2*k, k a non negative integer
	 */
	private static final int ZZ_LEXSTATE[] = {
		0, 0
	};
	private static final String ZZ_ACTION_PACKED_0 =
		"\1\0\1\1\2\2\1\1\1\3\1\4\1\1\1\5" +
			"\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15" +
			"\1\16\12\0\2\17\13\0\1\20\1\21\2\0\1\22" +
			"\16\0\1\23\4\0\1\24\2\0\1\25\1\0\1\26" +
			"\1\27\3\0\1\30\3\0\1\31\5\0\1\32\1\0" +
			"\1\33\3\0\1\34\2\0\1\35";
	/**
	 * Translates DFA states to action switch labels.
	 */
	private static final int[] ZZ_ACTION = zzUnpackAction();
	private static final String ZZ_ROWMAP_PACKED_0 =
		"\0\0\0\50\0\120\0\50\0\170\0\50\0\240\0\310" +
			"\0\50\0\50\0\50\0\50\0\50\0\50\0\360\0\50" +
			"\0\50\0\50\0\u0118\0\u0140\0\u0168\0\u0190\0\u01b8\0\u01e0" +
			"\0\u0208\0\u0230\0\u0258\0\u0280\0\u02a8\0\50\0\u02d0\0\u02f8" +
			"\0\u0320\0\u0348\0\u0370\0\u0398\0\u03c0\0\u03e8\0\u0410\0\u0438" +
			"\0\u0460\0\50\0\50\0\u0488\0\u04b0\0\50\0\u04d8\0\u0500" +
			"\0\u0528\0\u0550\0\u0578\0\u05a0\0\u05c8\0\u05f0\0\u0618\0\u0640" +
			"\0\u0668\0\u0690\0\u06b8\0\u06e0\0\50\0\u0708\0\u0730\0\u0758" +
			"\0\u0780\0\50\0\u07a8\0\u07d0\0\50\0\u07f8\0\50\0\50" +
			"\0\u0820\0\u0848\0\u0870\0\u0898\0\u08c0\0\u08e8\0\u0910\0\50" +
			"\0\u0938\0\u0960\0\u0988\0\u09b0\0\u09d8\0\50\0\u0a00\0\50" +
			"\0\u0a28\0\u0a50\0\u0a78\0\50\0\u0aa0\0\u0ac8\0\50";
	/**
	 * Translates a state to a row index in the transition table
	 */
	private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
	private static final String ZZ_TRANS_PACKED_0 =
		"\1\2\1\3\2\4\1\5\1\6\1\7\1\2\1\10" +
			"\24\7\1\11\1\12\1\13\1\14\1\15\1\16\1\17" +
			"\1\2\1\20\1\21\1\22\52\0\1\4\51\0\1\23" +
			"\1\24\50\0\2\7\1\0\24\7\24\0\1\25\1\26" +
			"\1\27\2\0\1\30\1\0\1\31\1\32\1\33\70\0" +
			"\1\34\4\0\1\23\1\35\1\36\45\23\5\24\1\37" +
			"\42\24\12\0\1\40\50\0\1\41\14\0\1\42\31\0" +
			"\1\43\2\0\1\44\62\0\1\45\31\0\1\46\46\0" +
			"\1\47\15\0\1\50\33\0\1\51\100\0\1\52\5\0" +
			"\1\36\45\0\4\24\1\53\1\37\42\24\13\0\1\54" +
			"\47\0\1\55\61\0\1\56\43\0\1\57\2\0\1\60" +
			"\51\0\1\61\37\0\1\62\56\0\1\63\41\0\1\64" +
			"\45\0\1\65\44\0\1\66\51\0\1\67\63\0\1\70" +
			"\47\0\1\71\51\0\1\72\44\0\1\73\37\0\1\74" +
			"\41\0\1\75\51\0\1\76\61\0\1\77\45\0\1\100" +
			"\41\0\1\101\45\0\1\102\54\0\1\103\52\0\1\104" +
			"\35\0\1\105\57\0\1\106\37\0\1\107\47\0\1\110" +
			"\50\0\1\111\53\0\1\112\62\0\1\113\30\0\1\114" +
			"\56\0\1\115\52\0\1\116\42\0\1\117\42\0\1\120" +
			"\70\0\1\121\44\0\1\122\31\0\1\123\55\0\1\124" +
			"\45\0\1\125\54\0\1\126\42\0\1\127\52\0\1\130" +
			"\62\0\1\131\36\0\1\132\37\0\1\133\46\0\1\134" +
			"\50\0\1\135\64\0\1\136\32\0\1\137\34\0";
	/**
	 * The transition table of the DFA
	 */
	private static final int[] ZZ_TRANS = zzUnpackTrans();
	/* error codes */
	private static final int ZZ_UNKNOWN_ERROR = 0;
	private static final int ZZ_NO_MATCH = 1;
	private static final int ZZ_PUSHBACK_2BIG = 2;
	/* error messages for the codes above */
	private static final String[] ZZ_ERROR_MSG = {
		"Unknown internal scanner error",
		"Error: could not match input",
		"Error: pushback value was too large"
	};
	private static final String ZZ_ATTRIBUTE_PACKED_0 =
		"\1\0\1\11\1\1\1\11\1\1\1\11\2\1\6\11" +
			"\1\1\3\11\12\0\1\1\1\11\13\0\2\11\2\0" +
			"\1\11\16\0\1\11\4\0\1\11\2\0\1\11\1\0" +
			"\2\11\3\0\1\1\3\0\1\11\5\0\1\11\1\0" +
			"\1\11\3\0\1\11\2\0\1\11";
	/**
	 * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
	 */
	private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
	/**
	 * the input device
	 */
	private java.io.Reader zzReader;
	/**
	 * the current state of the DFA
	 */
	private int zzState;
	/**
	 * the current lexical state
	 */
	private int zzLexicalState = YYINITIAL;
	/**
	 * this buffer contains the current text to be matched and is the source of the yytext() string
	 */
	private CharSequence zzBuffer = "";
	/**
	 * the textposition at the last accepting state
	 */
	private int zzMarkedPos;
	/**
	 * the current text position in the buffer
	 */
	private int zzCurrentPos;
	/**
	 * startRead marks the beginning of the yytext() string in the buffer
	 */
	private int zzStartRead;
	/**
	 * endRead marks the last character in the buffer, that has been read from input
	 */
	private int zzEndRead;
	/**
	 * zzAtBOL == true <=> the scanner is currently at the beginning of a line
	 */
	private boolean zzAtBOL = true;
	/**
	 * zzAtEOF == true <=> the scanner is at the EOF
	 */
	private boolean zzAtEOF;
	/**
	 * denotes if the user-EOF-code has already been executed
	 */
	private boolean zzEOFDone;

	/**
	 * Creates a new scanner
	 *
	 * @param in the java.io.Reader to read input from.
	 */
	public FlexGeneratedMapagLexer(java.io.Reader in) {
		this.zzReader = in;
	}

	/**
	 * Translates characters to character classes Chosen bits are [12, 6, 3] Total runtime size is 14800 bytes
	 */
	public static int ZZ_CMAP(int ch) {
		return ZZ_CMAP_A[(ZZ_CMAP_Y[(ZZ_CMAP_Z[ch >> 9] << 6) | ((ch >> 3) & 0x3f)] << 3) | (ch & 0x7)];
	}

	private static int[] zzUnpackAction() {
		int[] result = new int[95];
		int offset = 0;
		offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackAction(String packed, int offset, int[] result) {
		int i = 0;       /* index in packed string  */
		int j = offset;  /* index in unpacked array */
		int l = packed.length();
		while (i < l) {
			int count = packed.charAt(i++);
			int value = packed.charAt(i++);
			do result[j++] = value; while (--count > 0);
		}
		return j;
	}

	private static int[] zzUnpackRowMap() {
		int[] result = new int[95];
		int offset = 0;
		offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackRowMap(String packed, int offset, int[] result) {
		int i = 0;  /* index in packed string  */
		int j = offset;  /* index in unpacked array */
		int l = packed.length();
		while (i < l) {
			int high = packed.charAt(i++) << 16;
			result[j++] = high | packed.charAt(i++);
		}
		return j;
	}

	private static int[] zzUnpackTrans() {
		int[] result = new int[2800];
		int offset = 0;
		offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackTrans(String packed, int offset, int[] result) {
		int i = 0;       /* index in packed string  */
		int j = offset;  /* index in unpacked array */
		int l = packed.length();
		while (i < l) {
			int count = packed.charAt(i++);
			int value = packed.charAt(i++);
			value--;
			do result[j++] = value; while (--count > 0);
		}
		return j;
	}

	private static int[] zzUnpackAttribute() {
		int[] result = new int[95];
		int offset = 0;
		offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackAttribute(String packed, int offset, int[] result) {
		int i = 0;       /* index in packed string  */
		int j = offset;  /* index in unpacked array */
		int l = packed.length();
		while (i < l) {
			int count = packed.charAt(i++);
			int value = packed.charAt(i++);
			do result[j++] = value; while (--count > 0);
		}
		return j;
	}

	/**
	 * Unpacks the compressed character translation table.
	 *
	 * @param packed the packed character translation table
	 * @return the unpacked character translation table
	 */
	private static char[] zzUnpackCMap(String packed) {
		int size = 0;
		for (int i = 0, length = packed.length(); i < length; i += 2) {
			size += packed.charAt(i);
		}
		char[] map = new char[size];
		int i = 0;  /* index in packed string  */
		int j = 0;  /* index in unpacked array */
		while (i < packed.length()) {
			int count = packed.charAt(i++);
			char value = packed.charAt(i++);
			do map[j++] = value; while (--count > 0);
		}
		return map;
	}

	public final int getTokenStart() {
		return zzStartRead;
	}

	public final int getTokenEnd() {
		return getTokenStart() + yylength();
	}

	public void reset(CharSequence buffer, int start, int end, int initialState) {
		zzBuffer = buffer;
		zzCurrentPos = zzMarkedPos = zzStartRead = start;
		zzAtEOF = false;
		zzAtBOL = true;
		zzEndRead = end;
		yybegin(initialState);
	}

	/**
	 * Refills the input buffer.
	 *
	 * @return <code>false</code>, iff there was new input.
	 * @throws java.io.IOException if any I/O-Error occurs
	 */
	private boolean zzRefill() throws java.io.IOException {
		return true;
	}

	/**
	 * Returns the current lexical state.
	 */
	public final int yystate() {
		return zzLexicalState;
	}

	/**
	 * Enters a new lexical state
	 *
	 * @param newState the new lexical state
	 */
	public final void yybegin(int newState) {
		zzLexicalState = newState;
	}

	/**
	 * Returns the text matched by the current regular expression.
	 */
	public final CharSequence yytext() {
		return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
	}

	/**
	 * Returns the character at position <tt>pos</tt> from the matched text.
	 * <p>
	 * It is equivalent to yytext().charAt(pos), but faster
	 *
	 * @param pos the position of the character to fetch. A value from 0 to yylength()-1.
	 * @return the character at position pos
	 */
	public final char yycharat(int pos) {
		return zzBuffer.charAt(zzStartRead + pos);
	}

	/**
	 * Returns the length of the matched text region.
	 */
	public final int yylength() {
		return zzMarkedPos - zzStartRead;
	}

	/**
	 * Reports an error that occured while scanning.
	 * <p>
	 * In a wellformed scanner (no or only correct usage of yypushback(int) and a match-all fallback rule) this method
	 * will only be called with things that "Can't Possibly Happen". If this method is called, something is seriously
	 * wrong (e.g. a JFlex bug producing a faulty scanner etc.).
	 * <p>
	 * Usual syntax/scanner level error handling should be done in error fallback rules.
	 *
	 * @param errorCode the code of the errormessage to display
	 */
	private void zzScanError(int errorCode) {
		String message;
		try {
			message = ZZ_ERROR_MSG[errorCode];
		} catch (ArrayIndexOutOfBoundsException e) {
			message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
		}

		throw new Error(message);
	}

	/**
	 * Pushes the specified amount of characters back into the input stream.
	 * <p>
	 * They will be read again by then next call of the scanning method
	 *
	 * @param number the number of characters to be read again. This number must not be greater than yylength()!
	 */
	public void yypushback(int number) {
		if (number > yylength())
			zzScanError(ZZ_PUSHBACK_2BIG);

		zzMarkedPos -= number;
	}

	/**
	 * Contains user EOF-code, which will be executed exactly once, when the end of file is reached
	 */
	private void zzDoEOF() {
		if (!zzEOFDone) {
			zzEOFDone = true;

		}
	}

	/**
	 * Resumes scanning until the next regular expression is matched, the end of input is encountered or an I/O-Error
	 * occurs.
	 *
	 * @return the next token
	 * @throws java.io.IOException if any I/O-Error occurs
	 */
	public IElementType advance() throws java.io.IOException {
		int zzInput;
		int zzAction;

		// cached fields:
		int zzCurrentPosL;
		int zzMarkedPosL;
		int zzEndReadL = zzEndRead;
		CharSequence zzBufferL = zzBuffer;

		int[] zzTransL = ZZ_TRANS;
		int[] zzRowMapL = ZZ_ROWMAP;
		int[] zzAttrL = ZZ_ATTRIBUTE;

		while (true) {
			zzMarkedPosL = zzMarkedPos;

			zzAction = -1;

			zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

			zzState = ZZ_LEXSTATE[zzLexicalState];

			// set up zzAction for empty match case:
			int zzAttributes = zzAttrL[zzState];
			if ((zzAttributes & 1) == 1) {
				zzAction = zzState;
			}

			zzForAction:
			{
				while (true) {

					if (zzCurrentPosL < zzEndReadL) {
						zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
						zzCurrentPosL += Character.charCount(zzInput);
					} else if (zzAtEOF) {
						zzInput = YYEOF;
						break zzForAction;
					} else {
						// store back cached positions
						zzCurrentPos = zzCurrentPosL;
						zzMarkedPos = zzMarkedPosL;
						boolean eof = zzRefill();
						// get translated positions and possibly new buffer
						zzCurrentPosL = zzCurrentPos;
						zzMarkedPosL = zzMarkedPos;
						zzBufferL = zzBuffer;
						zzEndReadL = zzEndRead;
						if (eof) {
							zzInput = YYEOF;
							break zzForAction;
						} else {
							zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
							zzCurrentPosL += Character.charCount(zzInput);
						}
					}
					int zzNext = zzTransL[zzRowMapL[zzState] + ZZ_CMAP(zzInput)];
					if (zzNext == -1) break zzForAction;
					zzState = zzNext;

					zzAttributes = zzAttrL[zzState];
					if ((zzAttributes & 1) == 1) {
						zzAction = zzState;
						zzMarkedPosL = zzCurrentPosL;
						if ((zzAttributes & 8) == 8) break zzForAction;
					}

				}
			}

			// store back cached position
			zzMarkedPos = zzMarkedPosL;

			if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
				zzAtEOF = true;
				zzDoEOF();
				return null;
			} else {
				switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
					case 1: {
						return TokenType.BAD_CHARACTER;
					}
					case 30:
						break;
					case 2: {
						return TokenType.WHITE_SPACE;
					}
					case 31:
						break;
					case 3: {
						return Symbols.ASTERISK;
					}
					case 32:
						break;
					case 4: {
						return Symbols.IDENTIFIER;
					}
					case 33:
						break;
					case 5: {
						return Symbols.OPENING_CURLY_BRACE;
					}
					case 34:
						break;
					case 6: {
						return Symbols.CLOSING_CURLY_BRACE;
					}
					case 35:
						break;
					case 7: {
						return Symbols.OPENING_PARENTHESIS;
					}
					case 36:
						break;
					case 8: {
						return Symbols.CLOSING_PARENTHESIS;
					}
					case 37:
						break;
					case 9: {
						return Symbols.COMMA;
					}
					case 38:
						break;
					case 10: {
						return Symbols.SEMICOLON;
					}
					case 39:
						break;
					case 11: {
						return Symbols.COLON;
					}
					case 40:
						break;
					case 12: {
						return Symbols.QUESTION_MARK;
					}
					case 41:
						break;
					case 13: {
						return Symbols.PLUS;
					}
					case 42:
						break;
					case 14: {
						return Symbols.BAR;
					}
					case 43:
						break;
					case 15: {
						return Symbols.LINE_COMMENT;
					}
					case 44:
						break;
					case 16: {
						return Symbols.EXPANDS_TO;
					}
					case 45:
						break;
					case 17: {
						return Symbols.BLOCK_COMMENT;
					}
					case 46:
						break;
					case 18: {
						return Symbols.KW_EOF;
					}
					case 47:
						break;
					case 19: {
						return Symbols.KW_LEFT;
					}
					case 48:
						break;
					case 20: {
						return Symbols.KW_ERROR;
					}
					case 49:
						break;
					case 21: {
						return Symbols.KW_RIGHT;
					}
					case 50:
						break;
					case 22: {
						return Symbols.KW_START;
					}
					case 51:
						break;
					case 23: {
						return Symbols.KW_SHIFT;
					}
					case 52:
						break;
					case 24: {
						return Symbols.KW_REDUCE;
					}
					case 53:
						break;
					case 25: {
						return Symbols.KW_RESOLVE;
					}
					case 54:
						break;
					case 26: {
						return Symbols.KW_NONASSOC;
					}
					case 55:
						break;
					case 27: {
						return Symbols.KW_TERMINALS;
					}
					case 56:
						break;
					case 28: {
						return Symbols.KW_PRECEDENCE;
					}
					case 57:
						break;
					case 29: {
						return Symbols.KW_REDUCE_ON_ERROR;
					}
					case 58:
						break;
					default:
						zzScanError(ZZ_NO_MATCH);
				}
			}
		}
	}

}
