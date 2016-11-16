package com.oim.ui.chat;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLDocument.BlockElement;
import javax.swing.text.html.HTMLDocument.RunElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.oim.common.component.OurPanel;
import com.oim.common.util.ColorUtil;
import com.only.OnlyScrollPane;
import com.only.component.TextExtender;
import com.only.laf.OnlyEditorPaneUI;
import com.only.util.OnlyUIUtil;

/**
 * 输入框组件
 * 
 * @author XiaHui
 * @date 2015年2月10日 下午1:35:52
 */
public class WritePanel extends OurPanel {

	private static final long serialVersionUID = 1L;

	private String fontName = "微软雅黑";
	private int fontSize = 12;
	private Color color = Color.black;
	private boolean bold = false;
	private boolean underline = false;
	private boolean italic = false;

	private JEditorPane writeEditorPane = new JEditorPane();;
	private OnlyScrollPane writeScrollPane = new OnlyScrollPane();
	private HTMLDocument writeDocument;

	public WritePanel() {
		initComponents();
		initEditorPane();
	}

	/**
	 * 初始化个组件
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	private void initComponents() {
		this.setLayout(new CardLayout());
		this.setOpaque(false);

		writeEditorPane.setUI(new OnlyEditorPaneUI());
		writeEditorPane.setFont(OnlyUIUtil.getDefaultFont());
		writeEditorPane.setContentType("text/html"); // NOI18N
		writeEditorPane.setOpaque(false);
		writeEditorPane.setSelectionColor(new java.awt.Color(80, 180, 210));

		new TextExtender(writeEditorPane);

		writeScrollPane.setBorder(null);
		writeScrollPane.getViewport().setOpaque(false);
		writeScrollPane.setHeaderVisible(false);
		writeScrollPane.setAlpha(0.0f);

		writeScrollPane.setViewportView(writeEditorPane);

		writeDocument = (HTMLDocument) writeEditorPane.getDocument();

		add(writeScrollPane);
	}

	private void initEditorPane() {
		StringBuilder style = getStyle();
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head>");
		html.append("</head>");
		html.append("<body>");
		html.append("<div id=\"main\">");
		html.append("<p ");
		html.append(style);
		html.append("></p>");
		html.append("</div>");
		html.append("</body>");
		html.append("</html>");
		writeEditorPane.setText(html.toString());
	}

	private StringBuilder getStyle() {
		StringBuilder style = new StringBuilder();
		style.append("style=\"");
		style.append(getStyleValue());
		style.append("\"");
		return style;
	}

	private StringBuilder getStyleValue() {

		StringBuilder style = new StringBuilder();

		style.append("font-family:'").append(fontName).append("';");
		style.append("font-size:'").append(fontSize).append("px';");
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
		if (null != color) {
			String c = ColorUtil.getColorInHexFromRGB(color);
			style.append("color:#");
			style.append(c);
			style.append(";");
		}
		return style;
	}

	public String getText() {
		return writeEditorPane.getText();
	}

	public void initText() {
		initEditorPane();
	}

	public void insertImage(String id, String name, String value, String path) {
		if (null != writeDocument && null != path && !"".equals(path)) {

			StringBuilder image = new StringBuilder();
			image.append("<img ");
			if (null != id && !"".equals(id)) {
				image.append(" id=\"");
				image.append(id);
				image.append("\"");
			}
			if (null != name && !"".equals(name)) {
				image.append(" name=\"");
				image.append(name);
				image.append("\"");
			}
			if (null != value && !"".equals(value)) {
				image.append(" value=\"");
				image.append(value);
				image.append("\"");
			}
			image.append(" src=\"file:/");
			image.append(path);
			image.append("\" />");

			StringBuilder style = getStyle();

			StringBuilder p = new StringBuilder();
			p.append("<p ");
			p.append(style);

			p.append(">");
			p.append(image);
			p.append("</p>");
			try {

				int index = writeEditorPane.getCaretPosition();// 这里是获取鼠标变成那个可以输入的一闪一闪的那玩意的位置，
				System.out.println(index);
				Element element = null;
				if (index > -1) {
					// element = writeDocument.getParagraphElement(index);
					element = writeDocument.getCharacterElement(index);// 根据鼠标的位置，获取那个位置的元素
					// element=element.getElement((element.getElementCount()-1));
					if (element instanceof BlockElement) {
						// BlockElement be=(BlockElement)element;

					}
					if (element instanceof RunElement) {
						// RunElement be=(RunElement)element;
						// be.toString();
					}

					// System.out.println(writeDocument.getCharacterElement(index).toString());
				}
				if (null == element) {
					element = writeDocument.getElement("main");
					if (null != element) {
						writeDocument.insertBeforeEnd(element, image.toString());
					}
				} else {// 在被选中的位置前插入，这就是为了把图片插到那个一闪一闪的位置
					writeDocument.insertBeforeStart(element, image.toString());
				}

				// Element mainElement = writeDocument.getElement("main");
				// // int i=writeEditorPane.getCaretPosition();
				// // writeDocument.insertAfterStart(elem, htmlText);
				// if (null != mainElement) {
				// // Element element =
				// // mainElement.getElement(mainElement.getElementCount() -
				// // 1);
				// // int i = writeDocument.getAsynchronousLoadPriority();
				// writeDocument.insertBeforeEnd(mainElement, p.toString());
				// // writeDocument.insertBeforeEnd(element, image.toString());
				// }
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Element mainElement =
			// writeDocument.getParagraphElement(writeEditorPane.getCaretPosition());
			// System.out.println(mainElement.toString());
			// System.out.println(writeDocument.getEndPosition());
			// System.out.println(writeEditorPane.getCaretPosition());
		}
	}

	public void insertImage(String path) {
		insertImage("", "", path, path);
	}

	public void insertImage(String name, String value, String path) {
		insertImage("", name, value, path);
	}

	public void setFontStyle(String fontName, int fontSize, Color color, boolean bold, boolean underline, boolean italic) {

		if (null != fontName && !fontName.equals(this.fontName)) {
			this.fontName = fontName;
		}
		if (this.fontSize != fontSize) {
			this.fontSize = fontSize;
		}
		this.color = color;
		if (this.bold != bold) {
			this.bold = bold;
		}
		if (this.underline != underline) {
			this.underline = underline;
		}
		if (this.italic != italic) {
			this.italic = italic;
		}
		changeFontAttribute();
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		if (this.underline != underline) {
			this.underline = underline;
			changeFontAttribute();
		}
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		if (this.bold != bold) {
			this.bold = bold;
			changeFontAttribute();
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		changeFontAttribute();
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		if (this.italic != italic) {
			this.italic = italic;
			changeFontAttribute();
		}

	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		if (null != fontName && !fontName.equals(this.fontName)) {
			this.fontName = fontName;
			changeFontAttribute();
		}
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		if (this.fontSize != fontSize) {
			this.fontSize = fontSize;
			changeFontAttribute();
		}
	}

	private void changeFontAttribute() {
		Document htmlDocument = Jsoup.parse(writeEditorPane.getText());
		if (null != htmlDocument) {
			List<org.jsoup.nodes.Element> elementList = htmlDocument.getElementsByTag("p");
			if (elementList.isEmpty()) {
				initEditorPane();
			} else {
				StringBuilder style = getStyleValue();
				for (org.jsoup.nodes.Element e : elementList) {
					e.removeAttr("style");
					e.attr("style", style.toString());
				}
				writeEditorPane.setText(htmlDocument.html());
			}
		}
	}
}
