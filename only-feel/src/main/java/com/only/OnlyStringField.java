package com.only;

import java.io.Serializable;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class OnlyStringField extends OnlyTextField implements Serializable {

    private static final long serialVersionUID = 7379430556435445023L;
    private int maxLength;

    public OnlyStringField() {
        this(0);
    }

    public OnlyStringField(int maxLength) {
        super();
        this.maxLength = maxLength;
        setDocument(new StringDocument());
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    private class StringDocument extends PlainDocument {

        private static final long serialVersionUID = -2645957214215338331L;
        private int oldLength;
        private int newLength;
        private int length;

        @Override
        public void insertString(int offset, String input, AttributeSet a) throws BadLocationException {
            oldLength = getLength();
            newLength = input.length();
            length = oldLength + newLength;

            if (maxLength > 0 && length > maxLength) {
                newLength = maxLength - oldLength;

                if (newLength > 0) {
                    super.insertString(offset, input.substring(0, newLength), a);
                }
            } else {
                super.insertString(offset, input, a);
            }
        }
    }
}