/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.calendar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.only.OnlyTextField;

/**
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class CalendarTextField extends OnlyTextField {

    private String errorMessage;
    Pattern numberPattern;
    Matcher matcher;

    public CalendarTextField() {
        this.numberPattern = Pattern.compile("[0-9]*");
    }

    @Override
    public String getText() {
        String text = super.getText();
        if (null != text && isNumber(text) && text.length() == 8) {
            text = text.substring(0, 4) + "-" + text.substring(4, 6) + "-" + text.substring(6, 8);
        }
        if (null != text && !"".equals(text) && isDate(text)) {
            this.setText(text);
            errorMessage = null;
            return text;
        }
//		else if (null == text||"".equals(text)) {!"".equals(text)&&
//			errorMessage = null;
//		} else {
//			errorMessage = "您输入的日期格式不对!!!";
//		}
        return null;
    }

    public boolean isDate(String text) {
        int first = text.indexOf('-');
        int second = text.lastIndexOf('-');

        if (first == second) {
            errorMessage = "您输入的日期格式不对!!!";
//			errorMessage = "您输入的日期缺少\"-\"符号!";
            return false;
        } else {
            String year = text.substring(0, first);
            String month = text.substring(first + 1, second);
            String day = text.substring(second + 1, text.length());
            int maxDays = 31;
            if (isNumber(year) == false || isNumber(month) == false || isNumber(day) == false) {
                errorMessage = "您输入的日期包含不可用字符!";
                return false;
            } else if (year.length() < 4) {
                errorMessage = "您输入的年份少于四位!";
                return false;
            }
            int y = Integer.parseInt(year);
            int m = Integer.parseInt(month);
            int d = Integer.parseInt(day);
            if (m > 12 || m < 1) {
                errorMessage = "您输入的月份不在规定范围内!";
                return false;
            } else if (m == 4 || m == 6 || m == 9 || m == 11) {
                maxDays = 30;
            } else if (m == 2) {
                if (y % 4 > 0) {
                    maxDays = 28;
                } else if (y % 100 == 0 && y % 400 > 0) {
                    maxDays = 28;
                } else {
                    maxDays = 29;
                }
            }
            if (d < 1 || d > maxDays) {
                errorMessage = "您输入的日期不在规定范围内!";
                return false;
            }
        }
        return true;
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.setCaretPosition(null != text ? text.length() : 0);
    }

    private boolean isNumber(String text) {
        matcher = numberPattern.matcher(text);
        return matcher.matches() && !"".equals(text);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static void main(String[] args) {
        CalendarTextField calendarTextField = new CalendarTextField();
        System.out.println(calendarTextField.isNumber("2."));
    }
}
