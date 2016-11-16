package com.over;

import java.io.Serializable;

import javax.swing.JPasswordField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class OverLimitedPassword extends JPasswordField implements Serializable
{
    private static final long serialVersionUID = 8114410159759930019L;
    
    private int maxLength;
    
    public OverLimitedPassword()
    {
        this(0);
    }
    
    public OverLimitedPassword(int maxLength)
    {
        super();
        this.maxLength = maxLength;
        setDocument(new PasswordDocument());
    }
    
    public int getMaxLength()
    {
        return maxLength;
    }

    public void setMaxLength(int maxLength)
    {
        this.maxLength = maxLength;
    }
    
    private class PasswordDocument extends PlainDocument
    {
        private static final long serialVersionUID = 5555932492608826780L;

        private int oldLength;
        
        private int newLength;
        
        private int length;

        public void insertString(int offset, String input, AttributeSet a) throws BadLocationException
        {
            oldLength = getLength();
            newLength = input.length();
            length = oldLength + newLength;
            
            if(maxLength > 0 && length > maxLength)
            {
                newLength = maxLength - oldLength;
                
                if(newLength > 0)
                {
                    super.insertString(offset, input.substring(0, newLength), a);
                }
            }
            else
            {
                super.insertString(offset, input, a);
            }
        }
    }
}