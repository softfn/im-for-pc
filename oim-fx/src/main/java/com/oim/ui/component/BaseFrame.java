package com.oim.ui.component;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.oim.swing.UIBox;
import com.oim.swing.common.box.ImageBox;
import com.oim.ui.validation.DataValidation;
import com.only.OnlyBorderFrame;
import com.only.common.ImageDisplayMode;

/**
 * @author XiaHui
 * @date 2015年3月9日 下午1:50:28
 */
public class BaseFrame extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;
	private PromptPopup promptPopup = new PromptPopup();
	private TextPromptPopup textPromptPopup = new TextPromptPopup();
	private Set<DataValidation> dvSet = new HashSet<DataValidation>();

	public BaseFrame() {
		initComponents();
	}

	private void initComponents() {
		this.setBorderPainted(false);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconImage(ImageBox.getImage("Resources/Images/Logo/logo_64.png"));
		this.setImageDisplayMode(ImageDisplayMode.fill);
		UIBox.add(this);
	}

	public void showPromptMessage(String message) {
		int x = 0;
		int y = 0;
		int w = this.getWidth() - promptPopup.getWidth();
		int h = this.getHeight() - promptPopup.getHeight();
		// if (w > 0) {
		x = w / 2;
		// }
		// if (h > 0) {
		y = h / 2;
		// }
		showPromptMessage(this, x, y, "提示信息", message);
	}

	public void showPromptMessage(Component invoker, String message) {
		showPromptMessage(invoker, 0, invoker.getHeight(), "", message);
	}

	public void showPromptMessage(Component invoker, int x, int y, String message) {
		showPromptMessage(invoker, x, y, "", message);
	}

	public void showPromptMessage(Component invoker, int x, int y, String title, String message) {
		if (invoker.isShowing()) {
			promptPopup.setTitle(title);
			promptPopup.setText(message);
			promptPopup.show(invoker, x, y);
		}
	}

	public void showMessage(Component invoker, int x, int y, String message) {
		if (invoker.isShowing()) {
			textPromptPopup.setText(message);
			textPromptPopup.show(invoker, x, y);
		}
	}

	public void addDataValidation(DataValidation dataValidation) {
		dvSet.add(dataValidation);
	}

	public boolean validation(){
		boolean mark=true;
		for(DataValidation dv:dvSet){
			Object value=null;
			boolean required= dv.isRequired();
			JComponent c=dv.getComponent();
			boolean support=true;
			if(c instanceof JLabel){
				value=((JLabel)c).getText();
			}else if(c instanceof JTextField){
				value=((JTextField)c).getText();
			}else if(c instanceof JPasswordField){
				value=new String(((JPasswordField)c).getPassword());
			}else if(c instanceof JTextArea){
				value=((JTextArea)c).getText();
			}else if(c instanceof JTextPane){
				value=((JTextPane)c).getText();
			}else if(c instanceof JEditorPane){
				value=((JEditorPane)c).getText();
			}else if(c instanceof JComboBox<?>){
				value=((JComboBox<?>)c).getSelectedItem();
			}else if(c instanceof JRadioButton){
				value=((JRadioButton)c).isSelected();
			}else if(c instanceof JCheckBox){
				value=((JCheckBox)c).isSelected();
			}else{
				support=false;
			}
			
			if(required){
				if(null==value||"".equals(value)){
					
					//this.showMessage(c, x, y, "");
					if(mark){
						return mark=false;
					}
				}
			}
			
			if(support){
				
			}
		}
		return mark;
	}
}
