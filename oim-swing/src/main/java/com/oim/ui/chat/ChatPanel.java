/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.chat;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import com.oim.common.AppConstant;
import com.oim.common.util.FileUtil;
import com.oim.ui.component.BasePanel;
import com.oim.ui.component.GenericFileFilter;
import com.oim.ui.component.ImagePreviewPanel;
import com.oim.ui.component.event.ExecuteAction;
import com.only.OnlyBorderButton;
import com.only.OnlyButton;
import com.only.OnlyComboBox;
import com.only.OnlyLabel;
import com.only.OnlyPanel;
import com.only.OnlySplitPane;

/**
 * date 2012-9-16 23:25:08
 * 
 * @author XiaHui
 */
public class ChatPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
	FacePanel facePanel = FacePanel.getFacePanel();
	ScreenShotWindow ssw=ScreenShotWindow.getScreenShotWindow();
	private ChatHeadPanel chatHeadPanel = new ChatHeadPanel();
	private OnlyPanel basePanel = new OnlyPanel();

	private OnlySplitPane splitPane = new OnlySplitPane();
	private WritePanel writePanel = new WritePanel();
	private ShowPanel showPanel = new ShowPanel();

	private JPanel fontPanel = new JPanel();
	private JPanel writeIconPanel = new JPanel();
	private JPanel functionPanel = new JPanel();

	private OnlyPanel bottomPanel = new OnlyPanel();

	private OnlyButton sendButton = new OnlyButton();
	private OnlyButton closeButton = new OnlyButton();

	private OnlyButton fontSettingIconButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/aio_quickbar_font.png"));
	private OnlyButton sendFaceIconButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/aio_quickbar_face.png"));
	private OnlyButton sendImageIconButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/aio_quickbar_sendpic.png"));
	private OnlyButton cutImageIconButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/aio_quickbar_cut.png"));
	private OnlyButton registerIcon = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/aio_quickbar_register.png"));

	private OnlyLabel fontButton = new OnlyLabel(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/MidToolbarExtUp_Font/aio_quickbar_sysfont_tab_button.png"));
	private OnlyButton boldButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/MidToolbarExtUp_Font/Bold.png"));
	private OnlyButton colorButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/MidToolbarExtUp_Font/color.png"));
	private OnlyButton italicButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/MidToolbarExtUp_Font/Italic.png"));
	private OnlyButton underlineButton = createIconButton(new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/MidToolbarExtUp_Font/underline.png"));
	private JComboBox<String> fontComboBox;
	private JComboBox<Integer> fontSizeComboBox;

	private JPanel rightPanel = new JPanel();

	private Set<ExecuteAction> closeExecuteActionSet = new HashSet<ExecuteAction>();
	private Set<ExecuteAction> sendExecuteActionSet = new HashSet<ExecuteAction>();

	private JFileChooser fileChooser = new JFileChooser();
	private ImagePreviewPanel preview = new ImagePreviewPanel();
	private String[] imageTypes = new String[] { "jpg", "gif", "png", "bmp", "jpeg" };
	private GenericFileFilter filter = new GenericFileFilter(imageTypes, "图像文件");

	private boolean underline = false;
	private boolean bold = false;
	private Color color = Color.black;
	private boolean italic = false;
	private String fontName = "微软雅黑";
	private int fontSize = 12;
	ExecuteAction faceAction;
	ScreenShotAction screenShotAction;
	public ChatPanel() {
		initComponents();
		initEvent();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponents() {
		this.setLayout(new java.awt.BorderLayout());
		this.setOpaque(false);

		fileChooser.setAccessory(preview);
		fileChooser.addPropertyChangeListener(preview);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);

		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(280);
		splitPane.setBackground(Color.red);
		splitPane.setOrientation(OnlySplitPane.VERTICAL_SPLIT);

		functionPanel.setOpaque(false);
		functionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 1));

		fontPanel.setOpaque(false);
		fontPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 1));

		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

		bottomPanel.add(closeButton);
		bottomPanel.add(sendButton);

		closeButton.setText("关闭");
		sendButton.setText("发送");

		closeButton.setPreferredSize(new Dimension(100, 25));
		sendButton.setPreferredSize(new Dimension(100, 25));

		registerIcon.setText("消息记录");

		String[] fontArray = new String[] { "宋体", "小篆", "微软雅黑", "Helvetica", "TimesRoman", "Courier", "Helvetica", "TimesRoman", "Courier" };
		String[] fontSizeArray = new String[15];
		for (int i = 0; i < 15; i++) {
			fontSizeArray[i] = (i + 8) + "";
		}
		fontComboBox = new OnlyComboBox<String>(new javax.swing.DefaultComboBoxModel(fontArray));
		fontSizeComboBox = new OnlyComboBox<Integer>(new javax.swing.DefaultComboBoxModel(fontSizeArray));
		fontSizeComboBox.setSelectedIndex(5);
		fontComboBox.setOpaque(false);
		fontSizeComboBox.setOpaque(false);

		fontComboBox.setFont(new java.awt.Font("微软雅黑", 0, 12));

		fontPanel.add(fontButton);
		fontPanel.add(fontComboBox);
		fontPanel.add(fontSizeComboBox);
		fontPanel.add(boldButton);
		fontPanel.add(colorButton);
		fontPanel.add(italicButton);
		fontPanel.add(underlineButton);
		fontPanel.setVisible(false);

		JPanel baseShowPanel = new JPanel();
		baseShowPanel.setLayout(new java.awt.BorderLayout());
		baseShowPanel.setOpaque(false);

		baseShowPanel.add(showPanel, java.awt.BorderLayout.CENTER);
		baseShowPanel.add(fontPanel, java.awt.BorderLayout.PAGE_END);

		functionPanel.add(fontSettingIconButton);
		functionPanel.add(sendFaceIconButton);
		functionPanel.add(sendImageIconButton);
		functionPanel.add(cutImageIconButton);

		writeIconPanel.setOpaque(false);
		writeIconPanel.setLayout(new BoxLayout(writeIconPanel, BoxLayout.X_AXIS));

		writeIconPanel.add(functionPanel);
		writeIconPanel.add(registerIcon);

		// OnlySeparator separator = new OnlySeparator();

		JPanel baseWriteIconPanel = new JPanel();
		baseWriteIconPanel.setLayout(new BoxLayout(baseWriteIconPanel, BoxLayout.Y_AXIS));
		baseWriteIconPanel.setOpaque(false);
		// baseWriteIconPanel.add(separator);
		baseWriteIconPanel.add(writeIconPanel);

		JPanel baseWritePanel = new JPanel();
		baseWritePanel.setLayout(new java.awt.BorderLayout());
		baseWritePanel.setOpaque(false);

		baseWritePanel.add(baseWriteIconPanel, java.awt.BorderLayout.PAGE_START);
		baseWritePanel.add(writePanel, java.awt.BorderLayout.CENTER);

		splitPane.setLeftComponent(baseShowPanel);
		splitPane.setRightComponent(baseWritePanel);
		splitPane.setDrawDividerLine(true);
		splitPane.setDividerLineColor(new Color(180, 180, 180));

		basePanel.setOpaque(false);
		basePanel.setLayout(new java.awt.BorderLayout());
		basePanel.add(splitPane, java.awt.BorderLayout.CENTER);
		basePanel.add(bottomPanel, java.awt.BorderLayout.PAGE_END);

		this.add(chatHeadPanel, java.awt.BorderLayout.PAGE_START);
		this.add(basePanel, java.awt.BorderLayout.CENTER);
		this.add(rightPanel, java.awt.BorderLayout.EAST);

		rightPanel.setLayout(new CardLayout());
		rightPanel.setOpaque(false);
	}

	private void initEvent() {
		fontSettingIconButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				fontMouseClicked();
			}
		});
		fontComboBox.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				fontComboBoxItemStateChanged(evt);
			}
		});
		fontSizeComboBox.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				fontComboBoxItemStateChanged(evt);
			}
		});

		boldButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (bold) {
					bold = false;// 是否粗体
				} else {
					bold = true;// 是否粗体
				}
				changeWriteFontSetting();
			}
		});
		colorButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				Color selectColor = JColorChooser.showDialog(null, "请选择字体颜色", Color.BLUE);
				if (null != selectColor) {
					color = selectColor;
					changeWriteFontSetting();
				}
			}
		});
		italicButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (italic) {
					italic = false;// 是否斜体
				} else {
					italic = true;// 是否斜体
				}
				changeWriteFontSetting();
			}
		});
		underlineButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {

				if (underline) {
					underline = false;// 是否带下划线
				} else {
					underline = true;// 是否带下划线
				}
				changeWriteFontSetting();

			}
		});

		sendImageIconButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					sendPicture();
				} catch (BadLocationException ex) {
					Logger.getLogger(ChatPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		sendFaceIconButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				showFace(evt);
			}
		});
		this.cutImageIconButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showCut();
			}
		});
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (ExecuteAction executeAction : sendExecuteActionSet) {
					executeAction.execute(ChatPanel.this);
				}
			}
		});
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (ExecuteAction executeAction : closeExecuteActionSet) {
					executeAction.execute(ChatPanel.this);
				}
			}
		});
		faceAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (null != value) {
					insertFace(value.toString());
				}
				return null;
			}
		};
		screenShotAction=new ScreenShotAction() {
			
			@Override
			public void saveImage(BufferedImage image) {
				if(null!=image){
					saveBufferedImage( image);
				}
			}
		};
	}

	private OnlyButton createIconButton(Icon icon) {
		OnlyButton button = new OnlyBorderButton(icon);
		button.setFocusable(false);
		button.setNormalImage(new ImageIcon("Resources/Images/Default/Common/panel.png").getImage());
		return button;
	}

	public void addFunctionButton(Icon icon,final ExecuteAction  a) {
		OnlyButton button = createIconButton(icon);
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				a.execute(ChatPanel.this);
			}
		});
		functionPanel.add(button);
	}

	private void insertFace(String value) {
		String path = "Resources/Images/Face/" + value + ".gif ";
		File file = new File(path);
		if (file.exists()) {
			String fullPath=file.getAbsolutePath();
			writePanel.insertImage("face", value,fullPath);
		}
	}

	public void sendPicture() throws BadLocationException {

		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File icon = fileChooser.getSelectedFile();
			File[] icons = fileChooser.getSelectedFiles();

			if (null != icon && icon.exists()) {
				writePanel.insertImage(icon.getAbsolutePath());
			}
			if (null != icons && icons.length > 0) {
				for (File file : icons) {
					if (file.exists()) {
						writePanel.insertImage(file.getAbsolutePath());
					}
				}
			}
		}
	}

	private void fontComboBoxItemStateChanged(ItemEvent evt) {
		changeWriteFontSetting();
	}

	public void fontMouseClicked() {
		fontPanel.setVisible(!fontPanel.isVisible());
	}

	private void updateAttribute() {
		String fontType = fontComboBox.getSelectedItem().toString();
		int size = Integer.parseInt(fontSizeComboBox.getSelectedItem().toString());
		if (!"".equals(fontType)) {
			fontName = fontType;
		}
		if (0 != size) {
			fontSize = size;
		}
	}

	private void showFace(java.awt.event.MouseEvent evt) {
		facePanel.setSelectAction(faceAction);
		facePanel.show( evt.getXOnScreen()-240, evt.getYOnScreen());
	}
	
	private void showCut() {
		ssw.setAction(screenShotAction);
		ssw.setVisible(true);
	}

	private void changeWriteFontSetting() {
		updateAttribute();
		writePanel.setFontStyle(fontName, fontSize, color, bold, underline, italic);
	}

	public void saveBufferedImage(BufferedImage image){
		String fileName = dateFormat.format(new Date()).toString() + ".jpg";
		String path = AppConstant.userHome + "/" + AppConstant.app_home_path + "/image/" + fileName;
		try {
			FileUtil.checkOrCreateFolder(path);
			ImageIO.write(image, "jpg", new File(path));
			writePanel.insertImage(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setShowText(String text) {
		showPanel.setText(text);
	}

	public void insertShowText(String text, String fontName, int fontSize, int r, int g, int b, boolean bold, boolean underline, boolean italic) {
		showPanel.insertText(text, fontName, fontSize, r, g, b, bold, underline, italic);
	}

	public void insertShowText(String text, String fontName, int fontSize, String color, boolean bold, boolean underline, boolean italic) {
		showPanel.insertText(text, fontName, fontSize, color, bold, underline, italic);
	}

	public void insertHtmlText(String htmlText){
		showPanel.insertHtmlText(htmlText);
	} 
	
	public void insertImage(String path) {
		showPanel.insertImage(path);
	}

	public void setUserListPanel(JPanel panel) {

		JLabel node = new JLabel();
		node.setPreferredSize(new Dimension(150, 2));
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setOpaque(false);
		root.add(node);
		root.add(panel);
		rightPanel.removeAll();
		rightPanel.add(root);
	}

	public String getWriteText() {
		return writePanel.getText();
	}

	public void initWriteText() {
		writePanel.initText();
	}

	public boolean isUnderline() {
		return writePanel.isUnderline();
	}

	public boolean isBold() {
		return writePanel.isBold();
	}

	public Color getColor() {
		return writePanel.getColor();
	}

	public boolean isItalic() {
		return writePanel.isItalic();
	}

	public String getFontName() {
		return writePanel.getFontName();
	}

	public int getFontSize() {
		return writePanel.getFontSize();
	}

	public void setText(String text) {
		chatHeadPanel.setText(text);
	}

	public void setName(String text) {
		chatHeadPanel.setName(text);
	}

	public void setIcon(Icon icon) {
		chatHeadPanel.setIcon(icon);
	}

	public void addCloseExecuteAction(ExecuteAction executeAction) {
		closeExecuteActionSet.add(executeAction);
	}

	public boolean removeCloseExecuteAction(ExecuteAction executeAction) {
		return closeExecuteActionSet.remove(executeAction);
	}

	public void addSendExecuteAction(ExecuteAction executeAction) {
		sendExecuteActionSet.add(executeAction);
	}

	public boolean removeSendExecuteAction(ExecuteAction executeAction) {
		return sendExecuteActionSet.remove(executeAction);
	}
}
