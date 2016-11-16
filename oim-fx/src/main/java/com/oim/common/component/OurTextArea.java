package com.oim.common.component;

import javax.swing.text.Document;

import com.only.OnlyTextArea;

@SuppressWarnings("serial")
public class OurTextArea extends OnlyTextArea {
	
	public OurTextArea() {
		super();
	}

	public OurTextArea(String text) {
		super(text);
	}

	public OurTextArea(int rows, int columns) {
		super(rows, columns);
	}

	public OurTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
	}

	public OurTextArea(Document doc) {
		super(doc);
	}

	public OurTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}
}
