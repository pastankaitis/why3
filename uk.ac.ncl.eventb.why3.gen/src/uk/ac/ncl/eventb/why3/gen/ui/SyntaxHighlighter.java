package uk.ac.ncl.eventb.why3.gen.ui;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import uk.ac.ncl.eventb.why3.gen.ui.IWhy3FormulaSource.SYMBOL_CLASS;

public class SyntaxHighlighter {
	private static final List<String> KEYWORDS = Arrays.asList(
			"forall", "exists", "not");

	private static final List<String> PREDEFINED_TYPES = Arrays.asList("int",
			"bool", "set", "rel");

	private static final List<String> PREDEFINED_LITERALS = Arrays.asList(
			"true", "false", "empty");

	private IWhy3FormulaSource source;
	private TranslationRule parent;
	private TranslationCatalog catalog;

	public SyntaxHighlighter(IWhy3FormulaSource source) {
		this.source = source;
		if (source.getSourceElement().parent().element() instanceof TranslationRule) {
			parent = (TranslationRule) source.getSourceElement().parent().element();
			catalog = (TranslationCatalog) parent.parent().element();
		} else if (source.getSourceElement().parent().element() instanceof TranslationCatalog) {
			catalog = (TranslationCatalog) source.getSourceElement().parent().element();
		}
	}

	private String getText() {
		return source.getSourceElement().getBody().content();
	}

	public void syntaxHighlight() {
		String text = getText();
		if (text == null)
			return;
		MyReader reader = new MyReader(text);
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.eolIsSignificant(false);
		tokenizer.slashStarComments(false);
		tokenizer.ordinaryChars('.', '.');
		tokenizer.ordinaryChars(':', ':');	
		tokenizer.ordinaryChars('(', '(');
		tokenizer.ordinaryChars('[', '[');
		tokenizer.ordinaryChars(')', ')');
		tokenizer.ordinaryChars(']', ']');	
		tokenizer.ordinaryChars(',', ',');	
		tokenizer.ordinaryChars('/', '/');
		tokenizer.ordinaryChars('\\', '\\');
		tokenizer.quoteChar('"');
		tokenizer.parseNumbers();

		try {
			while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
				switch (tokenizer.ttype) {
				case StreamTokenizer.TT_WORD:
					source.colour(
							reader.position - tokenizer.sval.length() - 1,
							tokenizer.sval.length(),
							getSymbolClass(tokenizer.sval));
					break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class MyReader extends StringReader {
		public int position;

		public MyReader(String s) {
			super(s);
			position = 0;
		}

		@Override
		public int read() throws IOException {
			position++;
			return super.read();
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			int read = super.read(cbuf, off, len);
			position += read;
			return read;
		}

		@Override
		public void reset() throws IOException {
			position = 0;
			super.reset();
		}
	}

	private SYMBOL_CLASS getSymbolClass(String t) {
		if (t == null || t.length() == 0)
			return SYMBOL_CLASS.OTHER;
		else if (isType(t))
			return SYMBOL_CLASS.BUILTIN_TYPE;
		else if (isKeyword(t))
			return SYMBOL_CLASS.KEYWORD;
		else if (isLiteral(t))
			return SYMBOL_CLASS.LITERAL;
		else if (isRuleConstant(t))
			return SYMBOL_CLASS.RULE_CONSTANT;
		else if (isContributedConstant(t))
			return SYMBOL_CLASS.CONTRIBUTED_CONSTANT;
		else
			return SYMBOL_CLASS.OTHER;
	}

	private boolean isLiteral(String text) {
		return PREDEFINED_LITERALS.contains(text);
	}

	private boolean isContributedConstant(String text) {
		for (TranslationRule rule : catalog.getRules()) {
			if (text.equals(rule.getTarget()))
				return true;
		}

		return false;
	}

	private boolean isRuleConstant(String text) {
		return parent != null && text.equals(parent.getTarget().content());
	}

	private boolean isType(String text) {
		return PREDEFINED_TYPES.contains(text);
	}
	
	private boolean isKeyword(String text) {
		return KEYWORDS.contains(text);
	}
}
