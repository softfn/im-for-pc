package com.oim.fx.ui.chat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;

import javafx.scene.effect.BlendMode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class ShowPanel {

	WebView webView = new WebView();
	WebPage webPage;
	WebEngine webEngine;

	public ShowPanel() {
		initComponent();
		iniEvent();
	}

	private void initComponent() {
		webEngine = webView.getEngine();
		webPage = Accessor.getPageFor(webEngine);
		webPage.setEditable(false);
		webPage.setContextMenuEnabled(false);
		webView.setFocusTraversable(true);
		// webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/resources/common/css/webview.css").toExternalForm());
		// webPage.setBackgroundColor(255);
		// webView.setOpacity(0.92);
		// Field f;
		// try {
		// f = webEngine.getClass().getDeclaredField("page");
		// f.setAccessible(true);
		// } catch (NoSuchFieldException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		webView.setBlendMode(BlendMode.DARKEN);// 透明
		// webView.setBlendMode(BlendMode.LIGHTEN);
		initializeHtml();
		initWeb(webEngine);
	}

	private void iniEvent() {
		// TODO Auto-generated method stub

	}

	public WebView getWebView() {
		return webView;
	}

	public StringBuilder createStyle(String fontName, int fontSize, String color, boolean bold, boolean underline,
			boolean italic) {
		StringBuilder style = new StringBuilder();
		style.append("style=\"");
		style.append(createStyleValue(fontName, fontSize, color, bold, underline, italic));
		style.append("\"");
		return style;
	}

	public StringBuilder createStyleValue(String fontName, int fontSize, String color, boolean bold, boolean underline,
			boolean italic) {
		StringBuilder style = new StringBuilder();
		if (null != fontName && !"".equals(fontName)) {
			style.append("font-family:").append(fontName).append(";");
		}
		style.append("font-size:").append(fontSize).append("px;");
		if (underline) {
			style.append("margin-top:0;text-decoration:underline;");
		} else {
			style.append("margin-top:0;");
		}
		if (italic) {
			style.append("font-style:italic;");
		}
		if (bold) {
			style.append("font-weight:bold;");
		}
		if (null != color && !"".equals(color)) {
			style.append("color:#");
			style.append(color);
			style.append(";");
		}
		return style;
	}

	public void initializeHtml() {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("	<head>");
		html.append("	</head>");
		html.append("	<body style=\"word-wrap:break-word;\" >");
		html.append("	</body>");
		html.append("</html>");
		webPage.load(webPage.getMainFrame(), html.toString(), "text/html");
		// webEngine.load(this.getClass().getResource("/resources/chat/html/index.html").toString());
		// webEngine.getLoadWorker().
	}

	public String getHtml() {
		String html = webPage.getHtml(webPage.getMainFrame());
		return html;
	}

	public void insertLast(String text) {
		insertLast(text, "", 12, null, false, false, false);
	}

	public void insertLast(String text, String fontName, int fontSize, String color, boolean bold, boolean underline,
			boolean italic) {
		Document doc = webPage.getDocument(webPage.getMainFrame());
		if (doc instanceof HTMLDocument) {
			HTMLDocument htmlDocument = (HTMLDocument) doc;
			HTMLElement htmlDocumentElement = (HTMLElement) htmlDocument.getDocumentElement();
			HTMLElement htmlBodyElement = (HTMLElement) htmlDocumentElement.getElementsByTagName("body").item(0);

			Element element = htmlDocument.createElement("div");
			element.setTextContent(text);
			element.setAttribute("style",
					createStyleValue(fontName, fontSize, color, bold, underline, italic).toString());
			htmlBodyElement.appendChild(element);

		}
	}

	public void insertSelectionHtml(String html) {
		String js = createSelectionJavaScript(escapeJavaStyleString(html, true, true));
		webEngine.executeScript(js);
	}

	public void insertLastHtml(String html) {
		String h = escapeJavaStyleString(html, true, true);
		StringBuilder js = new StringBuilder();
		js.append(createLastJavaScript());
		js.append("insertLastHtml('");
		js.append(h);
		js.append("');");
		webEngine.executeScript(js.toString());
	}

	private StringBuilder createLastJavaScript() {
		StringBuilder js = new StringBuilder();
		js.append("			function insertLastHtml(html) {");
		js.append("				var element = document.createElement('div');");
		js.append("				element.innerHTML = html;");
		js.append("				document.body.appendChild(element);");
		js.append("			}");
		return js;
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

	private void initWeb(WebEngine webEngine) {
		JSObject window = (JSObject) webEngine.executeScript("window");
		window.setMember("app", new JavaApplication());
	}

}

class JavaApplication {
	public void show(String text) {
		System.out.println(text);
	}
}