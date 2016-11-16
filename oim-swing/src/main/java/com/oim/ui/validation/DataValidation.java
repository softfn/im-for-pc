package com.oim.ui.validation;

import javax.swing.JComponent;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月6日 下午9:31:32
 * @version 0.0.1
 */
public class DataValidation {

	private JComponent component;
	private boolean required = false;
	private String errorMessage;

	public DataValidation(JComponent component, boolean required, String errorMessage) {
		super();
		this.component = component;
		this.required = required;
		this.errorMessage = errorMessage;
	}

	public JComponent getComponent() {
		return component;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
