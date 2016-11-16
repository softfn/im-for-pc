package com.only;

import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

public class OnlyDateSpinner extends OnlySpinner {
	
	private static final long serialVersionUID = -5461435392396837097L;
	public static final String DEFAULT_FORMAT_PATTERN = "HH:mm:ss";
	private DateEditor editor;
	private JFormattedTextField field;

	public OnlyDateSpinner() {
		this(DEFAULT_FORMAT_PATTERN);
	}

	public OnlyDateSpinner(String dateFormatPattern) {
		this(dateFormatPattern, new Date());
	}

	public OnlyDateSpinner(Date date) {
		this(DEFAULT_FORMAT_PATTERN, date);
	}

	public OnlyDateSpinner(String dateFormatPattern, Date date) {
		super(new SpinnerDateModel());
		this.editor = createEditor(dateFormatPattern);
		this.field = editor.getTextField();
		setEditor(editor);
		field.setEditable(false);
		setHorizontalAlignment(SwingConstants.CENTER);
		setDate(date);
	}

	protected DateEditor createEditor(String dateFormatPattern) {
		DateEditor editor = new OnlySpinner.DateEditor(this, dateFormatPattern);
		changeEditorField(editor);
		return editor;
	}

	public String getText() {
		return field.getText();
	}

	public Date getDate() {
		return getValue();
	}

	public void setDate(Date date) {
		setValue(date);
	}

	public void setValue(Date value) {
		super.setValue(value);
	}

	public Date getValue() {
		return (Date) super.getValue();
	}
}