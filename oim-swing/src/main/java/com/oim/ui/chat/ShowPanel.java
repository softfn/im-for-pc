package com.oim.ui.chat;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import com.oim.common.component.OurPanel;
import com.oim.common.util.ColorUtil;
import com.only.OnlyScrollPane;
import com.only.component.TextExtender;
import com.only.laf.OnlyEditorPaneUI;

/**
 * 聊天窗口的显示内容组件
 * 
 * @author XiaHui
 * @date 2015年2月10日 下午1:35:52
 */
public class ShowPanel extends OurPanel {

	private static final long serialVersionUID = 1L;

	private JEditorPane showEditorPane = new JEditorPane();// 文本编辑窗口
	private OnlyScrollPane showScrollPane = new OnlyScrollPane();// 滚动窗口
	private HTMLDocument showDocument;

	public ShowPanel() {
		initComponents();
		initEditorPane();
	}

	/**
	 * 初始化界面各组件
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	private void initComponents() {
		this.setLayout(new CardLayout());
		this.setOpaque(false);

		showEditorPane.setContentType("text/html"); // 设置显示内容为html
		showEditorPane.setOpaque(false);// 设置为透明
		showEditorPane.setSelectionColor(new java.awt.Color(80, 180, 210));
		showEditorPane.setUI(new OnlyEditorPaneUI());
		showEditorPane.setEditable(false);// 作为显示信息窗口，设置其为不可编辑

		new TextExtender(showEditorPane);// 添加右键菜单，有复制，选择等功能

		showScrollPane.setBorder(null);
		showScrollPane.getViewport().setOpaque(false);
		showScrollPane.setHeaderVisible(false);
		showScrollPane.setAlpha(0.0f);

		showScrollPane.setViewportView(showEditorPane);

		showDocument = (HTMLDocument) showEditorPane.getDocument();

		add(showScrollPane);
	}

	/**
	 * 初始化显示文档组件的样式，如字体、字体大小、颜色等
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	private void initEditorPane() {
		String fontName = "微软雅黑";
		int fontSize = 12;
		Color color = Color.black;
		boolean bold = false;
		boolean underline = false;
		boolean italic = false;

		StringBuilder style = getStyle(fontName, fontSize, color.getRed(), color.getGreen(), color.getBlue(), bold, underline, italic);
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head>");
		html.append("</head>");
		html.append("<body>");
		html.append("<div id=\"main\">");
		html.append("<p ");
		html.append(style);
		html.append(">");
		html.append("</p>");
		html.append("</div>");
		html.append("</body>");
		html.append("</html>");
		showEditorPane.setText(html.toString());
	}

	public StringBuilder getStyle(String fontName, int fontSize, String color, boolean bold, boolean underline, boolean italic) {
		StringBuilder style = new StringBuilder();
		style.append("style=\"");
		style.append(getStyleValue(fontName, fontSize, color, bold, underline, italic));
		style.append("\"");
		return style;
	}

	public StringBuilder getStyle(String fontName, int fontSize, int r, int g, int b, boolean bold, boolean underline, boolean italic) {
		String color = ColorUtil.getColorInHexFromRGB(r, g, b);
		return getStyleValue(fontName, fontSize, color, bold, underline, italic);
	}

	public StringBuilder getStyleValue(String fontName, int fontSize, String color, boolean bold, boolean underline, boolean italic) {
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

		style.append("color:#");
		style.append(color);
		style.append(";");
		return style;
	}

	public String getText() {
		return showEditorPane.getText();
	}

	public void insertImageFilePath(String path) {
		if (null != showDocument && null != path && !"".equals(path)) {
			StringBuilder image = new StringBuilder();
			image.append("<img  src=\"file:/");
			image.append(path);
			image.append("\" />");
			Element mainElement = showDocument.getElement("main");
			Element element = mainElement.getElement(mainElement.getElementCount() - 1);
			try {
				showDocument.insertBeforeEnd(element, image.toString());
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertImage(String path) {
		if (null != showDocument && null != path && !"".equals(path)) {
			StringBuilder image = new StringBuilder();
			image.append("<img  src=\"");
			image.append(path);
			image.append("\" />");
			Element mainElement = showDocument.getElement("main");
			Element element = mainElement.getElement(mainElement.getElementCount() - 1);
			try {
				showDocument.insertBeforeEnd(element, image.toString());
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertText(String text, String fontName, int fontSize, String color, boolean bold, boolean underline, boolean italic) {
		if (null != showDocument) {
			StringBuilder style = getStyle(fontName, fontSize, color, bold, underline, italic);
			StringBuilder p = new StringBuilder();
			p.append("<p ");
			p.append(style);
			p.append(">");
			p.append(text);
			p.append("</p>");
			insertHtmlText(p.toString());
		}
	}

	public void insertText(String text, String fontName, int fontSize, int r, int g, int b, boolean bold, boolean underline, boolean italic) {
		String color = ColorUtil.getColorInHexFromRGB(r, g, b);
		insertText(text, fontName, fontSize, color, bold, underline, italic);
	}

	public void insertHtmlText(String htmlText) {
		if (null != showDocument) {
			try {
				Element mainElement = showDocument.getElement("main");
				if (null != mainElement) {
					showDocument.insertBeforeEnd(mainElement, htmlText);
					JScrollBar verticalScrollBar = showScrollPane.getVerticalScrollBar();
					int maximumValue = verticalScrollBar.getMaximum();
					int currentValue = verticalScrollBar.getValue() + verticalScrollBar.getHeight();
					// System.out.println(maximumValue);
					// System.out.println(currentValue);
					// System.out.println((maximumValue == currentValue));
					if ((maximumValue - 30) <= currentValue) {// 这里是让滚动条在最下面的时候，保持最新的信息能显示出来
						showEditorPane.select(showDocument.getLength(), showDocument.getLength());
						// showEditorPane.setCaretPosition(showDocument.getLength());
						// verticalScrollBar.setValue(maximumValue);
						// showEditorPane.setCaretPosition(showDocument.getLength());
					}
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setText(String text) {
		showEditorPane.setText(text);
	}
}
