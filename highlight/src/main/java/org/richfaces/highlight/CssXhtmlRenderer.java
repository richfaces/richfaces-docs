/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.highlight;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Formattable;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.css.core.internal.parser.CSSTokenizer;
import org.eclipse.wst.css.core.internal.parserz.CSSRegionContexts;

import com.uwyn.jhighlight.highlighter.ExplicitStateHighlighter;
import com.uwyn.jhighlight.renderer.XhtmlRenderer;
import com.uwyn.jhighlight.tools.StringUtils;


public class CssXhtmlRenderer extends XhtmlRenderer {

	static class WrapperBean<T> {
		private T wrapped;

		public WrapperBean(T wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public boolean equals(Object obj) {
			return ((WrapperBean<?>)obj).get() == wrapped;
		}
		
		public T get() {
			return wrapped;
		}
		
		@Override
		public int hashCode() {
			return wrapped.hashCode();
		}
		
		@Override
		public String toString() {
			return super.toString();
		}
	}
	
	class FormattableToken implements Formattable {
		private String token;

		public FormattableToken(String token) {
			this.token = token;
		}

		public void formatTo(Formatter formatter, int flags, int width,
				int precision) {
			try {
				formatter.out().append(getClassForToken(token));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class XHTMLString implements Formattable {
		private String text;

		public XHTMLString(String text) {
			this.text = text;
		}
		
		public void formatTo(Formatter formatter, int flags, int width,
				int precision) {
			String s = 
				StringUtils.replace(StringUtils.encodeHtml(text), " ", "&nbsp;")
					.replaceAll("\\r?\\n", "<br />\n");
			try {
				formatter.out().append(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class FormattableColor implements Formattable {
		private Color color;

		public FormattableColor(Color color) {
			this.color = color;
		}

		public void formatTo(Formatter formatter, int flags, int width,
				int precision) {
			try {
				formatter.out().append("#").append(
						Integer.toHexString(color.getRGB()).substring(2)
								.toUpperCase());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static final Map<String, Color> fContextToStyleMap = new HashMap<String, Color>();

	private static final String classTemplate = ".css_%s";
	private static final String colorTemplate = "color:%s;";
	private static final String tokenTemplate = "<span class=\"css_%s\">%s</span>";
	
	private static final Map<String, String> tokenClasses = new HashMap<String, String>();
	
	private Map<String, String> defaultCss  = null;
	
	static {
		//eclipse source code copied
		fContextToStyleMap.put(CSSRegionContexts.CSS_COMMENT, IStyleConstantsCSS.COMMENT);
		fContextToStyleMap.put(CSSRegionContexts.CSS_CDO, IStyleConstantsCSS.COMMENT);
		fContextToStyleMap.put(CSSRegionContexts.CSS_CDC, IStyleConstantsCSS.COMMENT);
		fContextToStyleMap.put(CSSRegionContexts.CSS_S, IStyleConstantsCSS.NORMAL);

		fContextToStyleMap.put(CSSRegionContexts.CSS_DELIMITER, IStyleConstantsCSS.SEMI_COLON);
		fContextToStyleMap.put(CSSRegionContexts.CSS_LBRACE, IStyleConstantsCSS.CURLY_BRACE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_RBRACE, IStyleConstantsCSS.CURLY_BRACE);

		fContextToStyleMap.put(CSSRegionContexts.CSS_IMPORT, IStyleConstantsCSS.ATMARK_RULE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_PAGE, IStyleConstantsCSS.ATMARK_RULE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_MEDIA, IStyleConstantsCSS.ATMARK_RULE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_FONT_FACE, IStyleConstantsCSS.ATMARK_RULE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_CHARSET, IStyleConstantsCSS.ATMARK_RULE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_ATKEYWORD, IStyleConstantsCSS.ATMARK_RULE);

		fContextToStyleMap.put(CSSRegionContexts.CSS_STRING, IStyleConstantsCSS.STRING);
		fContextToStyleMap.put(CSSRegionContexts.CSS_URI, IStyleConstantsCSS.URI);
		fContextToStyleMap.put(CSSRegionContexts.CSS_MEDIUM, IStyleConstantsCSS.MEDIA);
		fContextToStyleMap.put(CSSRegionContexts.CSS_MEDIA_SEPARATOR, IStyleConstantsCSS.MEDIA);

		fContextToStyleMap.put(CSSRegionContexts.CSS_CHARSET_NAME, IStyleConstantsCSS.STRING);

		fContextToStyleMap.put(CSSRegionContexts.CSS_PAGE_SELECTOR, IStyleConstantsCSS.MEDIA);

		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_ELEMENT_NAME, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_UNIVERSAL, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_PSEUDO, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_CLASS, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_ID, IStyleConstantsCSS.SELECTOR);

		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_COMBINATOR, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_SEPARATOR, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_ATTRIBUTE_START, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_ATTRIBUTE_END, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_ATTRIBUTE_NAME, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_ATTRIBUTE_VALUE, IStyleConstantsCSS.SELECTOR);
		fContextToStyleMap.put(CSSRegionContexts.CSS_SELECTOR_ATTRIBUTE_OPERATOR, IStyleConstantsCSS.SELECTOR);

		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_PROPERTY, IStyleConstantsCSS.PROPERTY_NAME);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_IDENT, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_DIMENSION, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_PERCENTAGE, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_NUMBER, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_FUNCTION, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_PARENTHESIS_CLOSE, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_STRING, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_URI, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_HASH, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_UNICODE_RANGE, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_IMPORTANT, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_OPERATOR, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_VALUE_S, IStyleConstantsCSS.PROPERTY_VALUE);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_SEPARATOR, IStyleConstantsCSS.COLON);
		fContextToStyleMap.put(CSSRegionContexts.CSS_DECLARATION_DELIMITER, IStyleConstantsCSS.SEMI_COLON);

		fContextToStyleMap.put(CSSRegionContexts.CSS_UNKNOWN, IStyleConstantsCSS.ERROR);
	}
	
	public static void main(String[] args) throws Exception {
		//new CssXhtmlRenderer().generateCSSTemplate(new File("D:\\eclipse\\rf\\rf3\\docs\\userguide\\en\\src\\main\\resources\\css\\css.css"));
		//new CssXhtmlRenderer().generateClassesForTokens();
		
		//System.exit(0);
		
		String path = "D:\\eclipse\\rf\\rf3\\samples\\richfaces-demo\\src\\main\\webapp\\css\\common.css";
		
		if (args.length > 0) {
			path = args[0];
		}
		
		FileReader fileReader = new FileReader(path);
		
		
		try {
			
			new CssXhtmlRenderer().highlight("aaaa", new FileInputStream(path), new FileOutputStream("css.html"), null, false);
			
		} finally {
			fileReader.close();
		}
	}
	
	
	private void generateClassesForTokens() {
		Map<WrapperBean<Color>, String> fieldMap = new HashMap<WrapperBean<Color>, String>();
		
		try {
			Field[] fields = IStyleConstantsCSS.class.getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				int modifiers = field.getModifiers();
				
				if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)) {
					String name = field.getName().toLowerCase();
					Object object = field.get(null);
					if (object instanceof Color) {
						fieldMap.put(new WrapperBean<Color>((Color) object), name);
					}
					
					
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Iterator<Entry<String, Color>> iterator = fContextToStyleMap.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Color> entry = iterator.next();
			String token = entry.getKey();
			Color color = entry.getValue();
			WrapperBean<Color> bean = new WrapperBean<Color>(color);
			
			String string = fieldMap.get(bean);
			tokenClasses.put(token, string);
			
		}
		
		
	}
	
	public Map<String, String> generateCSSTemplate() {
		Map<String, String> m = new HashMap<String, String>();
		Field[] fields = IStyleConstantsCSS.class.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			int modifiers = field.getModifiers();
			
			if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)) {
				String name = field.getName().toLowerCase();
				try {
					Object object = field.get(null);
					if (object instanceof Color) {
						FormattableColor color = new FormattableColor((Color) object);
						m.put(String.format(classTemplate, name), String.format(colorTemplate, color));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return m;
	}
	
	
	
	public String getClassForToken(String token) {
		synchronized (tokenClasses) {
			if (tokenClasses.isEmpty()) {
				generateClassesForTokens();
			}
		}
		
		return tokenClasses.get(token);
	}

	protected String getCssClass(int arg0) {
		return null;
	}

	@Override
	protected synchronized Map<String,String> getDefaultCssStyles() {
		
		if (defaultCss == null) {
			defaultCss = generateCSSTemplate();
		}
		return defaultCss;
	}

	protected ExplicitStateHighlighter getHighlighter() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.uwyn.jhighlight.renderer.XhtmlRenderer#highlight(java.lang.String, java.io.InputStream, java.io.OutputStream, java.lang.String, boolean)
	 */
	public void highlight(String name, InputStream in, OutputStream out,
			String encoding, boolean fragment) throws IOException {
		
		InputStreamReader reader = encoding == null ? 
				new InputStreamReader(in) : 
					new InputStreamReader(in, encoding);
		
		StringBuffer b = new StringBuffer();
		char [] buffer = new char[1024];
		int read;
		
		while((read = reader.read(buffer)) != -1) {
			b.append(buffer, 0, read);
		}
		
		highlight(name, b.toString(), out, encoding, fragment);
	}
	
	private void highlight(String name, String in, OutputStream output,
			String encoding, boolean fragment) throws IOException{
		
		String string = StringUtils.convertTabsToSpaces(in, 4);
		PrintWriter out = new PrintWriter(output);

		if (fragment)
		{
			out.write(getXhtmlHeaderFragment(name));
		}
		else
		{
			out.write(getXhtmlHeader(name));
		}
		
		CSSTokenizer tokenizer = new CSSTokenizer(new StringReader(string));
		while (!tokenizer.isEOF()) {
			String token = tokenizer.primGetNextToken();
			if (token != null) {
				String text = tokenizer.yytext();
				out.printf(tokenTemplate, new FormattableToken(token), new XHTMLString(text));
			} else {
				if (tokenizer.yylength() > 0) {
					throw new IllegalStateException();
				}
			}
		}
		if (!fragment) {
			out.write(getXhtmlFooter());
		}
		
		out.flush();
		out.close();
	}
	
	public String highlight(String name, String in, String encoding,
			boolean fragment) throws IOException {
		
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		highlight(name, in, s, encoding, fragment);
		
		s.flush();
		s.close();
		
		return (encoding == null) ? s.toString() : s.toString(encoding);
	}
	
}

