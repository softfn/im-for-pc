package com.over;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class OverNumberField extends JTextField implements Serializable {

    private static final long serialVersionUID = -7025550739233386707L;
    private static final String MINUS = "-";
    private static final String POINT = ".";
    private Pattern pattern;
    private int decimalLength;
    private int maxLength;
    private boolean nonNegative;
    private double maxRange;

    public OverNumberField() {
        this(0);
    }

    public OverNumberField(int maxLength) {
        this(maxLength, 0);
    }

    public OverNumberField(int maxLength, int decimalLength) {
        this(maxLength, decimalLength, Double.MAX_VALUE);
    }

    public OverNumberField(int maxLength, int decimalLength, double maxRange) {
        super();
        this.maxLength = maxLength;
        this.maxRange = maxRange;
        setDecimalLength(decimalLength);
        setDocument(new NumberDocument());
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    public Number getNumber() {
        try {
            return getNumber(getText(), 0);
        } catch (Exception e) {
            return null;
        }
    }

    private Number getNumber(String numStr, Number defaultNum) throws Exception {
        if (numStr.endsWith(POINT)) {
            numStr = numStr.substring(0, numStr.length() - 1);
        }

        if (numStr.isEmpty() || numStr.equals(MINUS)) {
            return defaultNum;
        }

        if (decimalLength > 0) {
            return Double.parseDouble(numStr);
        } else {
            return Long.parseLong(numStr);
        }
    }

    public int getDecimalLength() {
        return decimalLength;
    }

    public void setDecimalLength(int decimalLength) {
        this.decimalLength = decimalLength;

        if (decimalLength > 0) {
            String decimalRegex = "-?(0|(0\\.\\d{0," + decimalLength + "})|([1-9]\\d*\\.?\\d{0," + decimalLength + "}))?";
            pattern = Pattern.compile(decimalRegex);
        } else {
            pattern = Pattern.compile("0|(-?([1-9]\\d*)?)");
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public boolean isDecimal() {
        return decimalLength > 0;
    }

    public boolean isNonNegative() {
        return nonNegative;
    }

    public void setNonNegative(boolean nonNegative) {
        this.nonNegative = nonNegative;
    }

    private class NumberDocument extends PlainDocument {

        private static final long serialVersionUID = 973277174850216005L;
        private StringBuilder text = new StringBuilder();
        private Matcher matcher;
        private int oldLength;
        private int newLength;
        private int length;
        private String oldText;

        public void insertString(int offset, String input, AttributeSet a) throws BadLocationException {
            oldLength = getLength();
            newLength = input.length();
            length = oldLength + newLength;
            oldText = getText(0, oldLength);
            text.delete(0, text.length());
            text.append(oldText);
            text.insert(offset, input);
            matcher = pattern.matcher(text);

            if ((maxLength > 0 && length > maxLength) || !matcher.matches()) {
                return;
            }

            try {
                double num = getNumber(text.toString(), -Double.MIN_VALUE).doubleValue();

                if (num > maxRange || (nonNegative && num < 0)) {
                    return;
                }
            } catch (Exception e) {
                return;
            }

            super.insertString(offset, input, a);
        }
    }
}