package com.oim.fx.ui.chat;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import com.sun.webkit.dom.HTMLBodyElementImpl;

import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WritePanel extends VBox {

	private String fontName = "Microsoft YaHei";
	private int fontSize = 12;
	private Color color = Color.BLACK;
	private boolean bold = false;
	private boolean underline = false;
	private boolean italic = false;

	WebView webView = new WebView();
	WebPage webPage;
	WebEngine webEngine;

	public WritePanel() {
		initComponent();
		iniEvent();
	}

	private void initComponent() {
		this.getChildren().add(webView);
		webEngine = webView.getEngine();
		webPage = Accessor.getPageFor(webEngine);
		webPage.setEditable(true);
		webView.setFocusTraversable(true);
		//webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/resources/common/css/webview.css").toExternalForm());
		//webPage.setBackgroundColor(255);
		webView.setPrefWidth(300);
		//webView.setOpacity(0.92);
		webView.setBlendMode(BlendMode.DARKEN);
		initializeHtml();
	}

	private void iniEvent() {
		// TODO Auto-generated method stub

	}

	private void updateStyle() {
		Document doc = webPage.getDocument(webPage.getMainFrame());
		if (doc instanceof HTMLDocument) {
			HTMLDocument htmlDocument = (HTMLDocument) doc;
			HTMLElement htmlDocumentElement = (HTMLElement) htmlDocument.getDocumentElement();
			HTMLElement htmlBodyElement = (HTMLElement) htmlDocumentElement.getElementsByTagName("body").item(0);
			htmlBodyElement.setAttribute("style", createStyleValue().toString());
		}
	}

	private StringBuilder createStyle() {
		StringBuilder style = new StringBuilder();
		style.append("style=\"");
		style.append(createStyleValue());
		style.append("\"");
		return style;
	}

	private StringBuilder createStyleValue() {
		StringBuilder style = new StringBuilder();

		// style.append("background-color:rgba(0,152,50,0.7);");
		style.append("word-wrap:break-word;");// word-wrap: break-word;
		style.append("font-family:").append(fontName).append(";");
		style.append("font-size:").append(fontSize).append("px;");
		if (underline) {
			style.append("margin-top:0;text-decoration:underline;");
		} else {
			style.append("margin-top:0;");
		}
		if (italic) {
			style.append("font-style:italic;");
		}
		// if (italic) {
		// style.append("font-style:oblique;");
		// }
		if (bold) {
			style.append("font-weight:bold;");
		}
		if (null != color) {
			String c = colorValueToHex(color);
			style.append("color:#");
			style.append(c);
			style.append(";");
		}
		return style;
	}

	private String colorValueToHex(Color c) {
		return String.format((Locale) null, "%02x%02x%02x", Math.round(c.getRed() * 255),
				Math.round(c.getGreen() * 255), Math.round(c.getBlue() * 255));
	}

	public void initializeHtml() {
		initializeHtml(true);
	}

	public void initializeHtml(boolean createStyle) {
		StringBuilder html = new StringBuilder();
		
		html.append("<!doctype html>");
		html.append("<html>");
		html.append("	<head>");
		html.append("		<meta charset=\"utf-8\">");
		html.append("		<title></title>");
		html.append("	</head>");
		html.append("	<body contenteditable=\"true\" ").append((createStyle ? createStyle() : "")).append("></body>");
		html.append("</html>");
		webPage.load(webPage.getMainFrame(), html.toString(), "text/html");
	}

	public String getHtml() {
		String html = webPage.getHtml(webPage.getMainFrame());
		return html;
	}

	public void insert(String text) {
		Document doc = webPage.getDocument(webPage.getMainFrame());
		if (doc instanceof HTMLDocument) {
			HTMLDocument htmlDocument = (HTMLDocument) doc;
			HTMLElement htmlDocumentElement = (HTMLElement) htmlDocument.getDocumentElement();
			HTMLElement htmlBodyElement = (HTMLElement) htmlDocumentElement.getElementsByTagName("body").item(0);

			if (htmlBodyElement instanceof HTMLBodyElementImpl) {
				//HTMLBodyElementImpl htmlBodyElementImpl = (HTMLBodyElementImpl) htmlBodyElement;
				//Element e = htmlBodyElementImpl.getOffsetParent();
				// htmlBodyElementImpl.insertBefore(newChild, refChild)
				// htmlDocument.
			}

			Element e = htmlDocument.createElement("div");
			e.setTextContent(text);

			int positionOffset = webPage.getClientInsertPositionOffset();
			int index = (positionOffset + 1);
			NodeList nodeList = htmlBodyElement.getChildNodes();
			int length = nodeList.getLength();
			if (index < 0) {
				htmlBodyElement.appendChild(e);
			} else if (index < length) {
				org.w3c.dom.Node node = nodeList.item(index);
				// htmlBodyElement.
				htmlBodyElement.insertBefore(e, node);
			} else {
				htmlBodyElement.appendChild(e);
			}

			webPage.getClientInsertPositionOffset();

			// webView.set
			// System.out.println(htmlDocument.);

			//
			// org.w3c.dom.Node n = htmlBodyElement.getLastChild();
			//
			//
			//
			// webPage.getClientInsertPositionOffset();
			// // htmlBodyElement.ge
			// // hd.appendChild(e);
			// htmlDocument.getp
			//
		}
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
		updateStyle();
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		updateStyle();
	}

	public Color getColor() {
		return color;
	}
	
	public String getWebColor(){
		return colorValueToHex(color);
	}

	public void setColor(Color color) {
		this.color = color;
		updateStyle();
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
		updateStyle();
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
		updateStyle();
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
		updateStyle();
	}

	public void insertSelectionHtml(String html) {
		// String js=getJavaScript(escapeJavaStyleString(html, true, true));
		String js = createSelectionJavaScript(html);
		webEngine.executeScript(js);
	}

	public void insertLastHtml(String html) {
		// String js=getJavaScript(escapeJavaStyleString(html, true, true));
		String js = createLastJavaScript(html);
		webEngine.executeScript(js);
	}

	private String createSelectionJavaScript(String html) {
		StringBuilder js = new StringBuilder();
		js.append("function insertHtmlAtCursor(html) {\n");
		js.append("    var range, node;\n");
		js.append("    var hasSelection = window.getSelection;\n");
		js.append("    var selection = window.getSelection();\n");
		js.append("    if (hasSelection &&selection.rangeCount>0&& selection.getRangeAt)  {\n");
		js.append("        range = window.getSelection().getRangeAt(0);\n");
		js.append("        node = range.createContextualFragment(html);\n");
		js.append("        range.insertNode(node);\n");
		js.append("    } else if (document.selection && document.selection.createRange) {\n");
		js.append("        document.selection.createRange().pasteHTML(html);\n");
		js.append("    } else {\n");
		js.append("        var element = document.createElement('div');\n");
		js.append("        element.innerHTML = html;");
		js.append("        node = element.childNodes[0];");
		js.append("        document.body.appendChild(node);\n");
		js.append("    }");
		js.append("}");
		js.append("insertHtmlAtCursor('");
		js.append(html);
		js.append("');");
		String jsInsertHtml = js.toString();
		return jsInsertHtml;
	}

	private String createLastJavaScript(String html) {
		StringBuilder js = new StringBuilder();
		js.append("function insertHtmlAtCursor(html) {\n");
		js.append("    var range, node;\n");
		js.append("    var element = document.createElement('div');\n");
		js.append("    element.innerHTML = html;");
		js.append("    node = element.childNodes[0];");
		js.append("    document.body.appendChild(node);\n");
		js.append("}");
		js.append("insertHtmlAtCursor('");
		js.append(html);
		js.append("');");
		String jsInsertHtml = js.toString();
		return jsInsertHtml;
	}

	private String hex(int i) {
		return Integer.toHexString(i);
	}

	public String escapeJavaStyleString(String str, boolean escapeSingleQuote, boolean escapeForwardSlash) {
		StringBuilder out = new StringBuilder("");
		if (str == null) {
			return null;
		}
		int sz;
		sz = str.length();
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);

			// handle unicode
			if (ch > 0xfff) {
				out.append("\\u").append(hex(ch));
			} else if (ch > 0xff) {
				out.append("\\u0").append(hex(ch));
			} else if (ch > 0x7f) {
				out.append("\\u00").append(hex(ch));
			} else if (ch < 32) {
				switch (ch) {
				case '\b':
					out.append('\\');
					out.append('b');
					break;
				case '\n':
					out.append('\\');
					out.append('n');
					break;
				case '\t':
					out.append('\\');
					out.append('t');
					break;
				case '\f':
					out.append('\\');
					out.append('f');
					break;
				case '\r':
					out.append('\\');
					out.append('r');
					break;
				default:
					if (ch > 0xf) {
						out.append("\\u00").append(hex(ch));
					} else {
						out.append("\\u000").append(hex(ch));
					}
					break;
				}
			} else {
				switch (ch) {
				case '\'':
					if (escapeSingleQuote) {
						out.append('\\');
					}
					out.append('\'');
					break;
				case '"':
					out.append('\\');
					out.append('"');
					break;
				case '\\':
					out.append('\\');
					out.append('\\');
					break;
				case '/':
					if (escapeForwardSlash) {
						out.append('\\');
					}
					out.append('/');
					break;
				default:
					out.append(ch);
					break;
				}
			}
		}
		return out.toString();
	}
}
