package name.martingeisse.mapag.input;

import com.intellij.lexer.FlexAdapter;

/**
 *
 */
public class MapagLexer extends FlexAdapter {

	public MapagLexer() {
		super(new FlexGeneratedMapagLexer(null));
	}

}
