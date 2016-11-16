package com.over;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import com.only.util.OnlyFeelUtil;

public class OverIPV4AddressField extends JTextField implements Serializable
{
    private static final long serialVersionUID = -2754807884601930339L;

    private static final String IP_BLOCK_REGEX = "(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)";
    
    private static final String IP_REGEX = "(" + IP_BLOCK_REGEX + "\\.){3}" + IP_BLOCK_REGEX;
    
    private static final String CHAR_AREA = "0123456789";
    
    private static final int BLOCK_LENGTH = 4;

    private static final String LEFT_PRESS = "Left";

    private static final String RIGHT_PRESS = "Right";
    
    private static final String BACK_SPACE_PRESS = "BackSpace";
    
    private static final String DELETE_PRESS = "Delete";
    
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(); 
    
    private JIPV4Field[] ipFields;
    
    private Dot[] dots;
    
    private KeyAdapter keyListener = new KeyAdapter()
    {
        public void keyPressed(KeyEvent e)
        {
            if(!isEditable() || !isEnabled())
            {
                return;
            }
            
            JTextComponent field = (JTextComponent)e.getComponent();
            int keyCode = e.getKeyCode();
            char keyChar = e.getKeyChar();
            String text = field.getText();
            String selText = field.getSelectedText();
            int caretPos = field.getCaretPosition();
            int textLength = text.length();
            
            if(keyCode == KeyEvent.VK_LEFT && caretPos == 0 && selText == null)
            {
                field.firePropertyChange(LEFT_PRESS, 0, 1);
            }
            else if((keyCode == KeyEvent.VK_RIGHT && caretPos == textLength && selText == null)
                            || (keyChar == '.' && !text.isEmpty() && selText == null))
            {
                field.firePropertyChange(RIGHT_PRESS, 0, 1);
            }
            else if(keyCode == KeyEvent.VK_BACK_SPACE && caretPos == 0 && selText == null)
            {
                field.firePropertyChange(BACK_SPACE_PRESS, 0, 1);
            }
            else if(keyCode == KeyEvent.VK_DELETE && caretPos == textLength && selText == null)
            {
                field.firePropertyChange(DELETE_PRESS, 0, 1);
            }
            else if(keyCode == KeyEvent.VK_HOME)
            {
                ipFields[0].unSelectAllWhenFocusGained();
                ipFields[0].requestFocus();
                ipFields[0].setCaretPosition(0);
            }
            else if(keyCode == KeyEvent.VK_END)
            {
                int last = ipFields.length - 1;
                textLength = ipFields[last].getText().length();
                ipFields[last].unSelectAllWhenFocusGained();
                ipFields[last].requestFocus();
                ipFields[last].setCaretPosition(textLength);
            }
            else if((CHAR_AREA.indexOf(keyChar) >= 0)
                            && ((selText == null && caretPos == 2)
                                            || (selText != null && field.getSelectionStart() == 2 && field.getSelectionEnd() == 3)))
            {
                field.firePropertyChange(RIGHT_PRESS, 0, 1);
            }
        }
    };

    public OverIPV4AddressField()
    {
        this(null);
    }
    
    public OverIPV4AddressField(String ipAddress)
    {
        this.setLayout(new GridLayout(1, BLOCK_LENGTH, 0, 0));
        this.setFocusable(false);
        createIPFields();
        setIpAddress(ipAddress);
    }
    
    public void updateUI()
    {
        Component[] children = this.getComponents();
        
        for(Component child: children)
        {
            this.remove(child);
        }
        
        super.updateUI();
        
        for(Component child: children)
        {
            this.add(child);
        }
        
        if(this.getCaret() != null)
        {
            this.getCaret().deinstall(this);
        }
    }
    
    private void createIPFields()
    {
        ipFields = new JIPV4Field[BLOCK_LENGTH];
        dots = new Dot[BLOCK_LENGTH - 1];
        JPanel[] fieldPanes = new JPanel[BLOCK_LENGTH];
        
        for(int i = 0; i < BLOCK_LENGTH; i++)
        {
            ipFields[i] = new JIPV4Field();
            fieldPanes[i] = new JPanel();
            
            ipFields[i].addKeyListener(keyListener);
            fieldPanes[i].setOpaque(false);
            fieldPanes[i].setLayout(new BorderLayout());
            fieldPanes[i].add(ipFields[i], BorderLayout.CENTER);
            
            if(i != BLOCK_LENGTH - 1)
            {
                fieldPanes[i].add(dots[i] = new Dot(), BorderLayout.EAST);
            }
            
            this.add(fieldPanes[i]);
        }
        
        for(int i = 0; i < BLOCK_LENGTH; i++)
        {
            if(i == 0)
            {
                ipFields[i].addPropertyChangeListener(new KeyPressListener(null, ipFields[i + 1]));
            }
            else if(i == BLOCK_LENGTH - 1)
            {
                ipFields[i].addPropertyChangeListener(new KeyPressListener(ipFields[i - 1], null));
            }
            else
            {
                ipFields[i].addPropertyChangeListener(new KeyPressListener(ipFields[i - 1], ipFields[i + 1]));
            }
        }
    }

    public void setIpAddress(String ipAddress)
    {
        if(ipAddress != null && !ipAddress.isEmpty())
        {
            if(!Pattern.matches(IP_REGEX, ipAddress))
            {
                throw new IllegalArgumentException("Invalid IP Address:" + ipAddress);
            }
            
            String ipBit[] = ipAddress.split("\\.");
            int index = 0;
            
            for(JIPV4Field field: ipFields)
            {
                field.setText(Integer.parseInt(ipBit[index++]) + "");
            }
        }
        else
        {
            for(JIPV4Field field: ipFields)
            {
                field.setText("");
            }
        }
    }
    
    public void setText(String text)
    {
        setIpAddress(text);
    }
    
    public void setFont(Font font)
    {
        super.setFont(font);
        
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setFont(font);
            }
        }
        
        if(dots != null)
        {
            for(Dot dot: dots)
            {
                dot.setFont(font);
            }
        }
    }
    
    public void setSelectionColor(Color color)
    {
        super.setSelectionColor(color);
        
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setSelectionColor(color);
            }
        }
    }
    
    public void setSelectedTextColor(Color color)
    {
        super.setSelectedTextColor(color);
        
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setSelectedTextColor(color);
            }
        }
    }
    
    public void setDisabledTextColor(Color color)
    {
        super.setDisabledTextColor(color);
        
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setDisabledTextColor(color);
                
                if(!field.isEnabled())
                {
                    field.repaint();
                }
            }
        }
        
        if(dots != null && !this.isEnabled())
        {
            for(Dot dot: dots)
            {
                dot.setForeground(color);
            }
        }
    }
    
    public void setCaretColor(Color color)
    {
        super.setCaretColor(color);
        
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setCaretColor(color);
            }
        }
    }
    
    public void setForeground(Color color)
    {
        super.setForeground(color);
        
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setForeground(color);
            }
        }
        
        if(dots != null && this.isEnabled())
        {
            for(Dot dot: dots)
            {
                dot.setForeground(color);
            }
        }
    }
    
    public void setEnabled(boolean isEnable)
    {
        super.setEnabled(isEnable);

        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setEnabled(isEnable);
            }
        }
        
        if(dots != null)
        {
            for(Dot dot: dots)
            {
                dot.setForeground(isEnable? this.getForeground(): this.getDisabledTextColor());
            }
        }
    }

    public void setEditable(boolean isEditable)
    {
        super.setEditable(isEditable);
        
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.setEditable(isEditable);
            }
        }
    }

    public String getIpAddress()
    {
        StringBuilder ip = new StringBuilder();
        int emptyCount = 0;
        
        if(ipFields != null)
        {
            String str;
            
            for(JIPV4Field field: ipFields)
            {
                str = field.getText();
                
                if(str.isEmpty())
                {
                    emptyCount++;
                    str = "0";
                }
                
                ip.append('.');
                ip.append(str);
            }
            
            ip.deleteCharAt(0);
        }
        
        return emptyCount == BLOCK_LENGTH? "": ip.toString();
    }

    public String getText()
    {
        return getIpAddress();
    }
    
    public JTextField[] getFieldComponents()
    {
        return ipFields;
    }
    
    public void addFocusListener(FocusListener listener)
    {
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.addFocusListener(listener);
            }
        }
    }
    
    public void removeFocusListener(FocusListener listener)
    {
        if(ipFields != null)
        {
            for(JIPV4Field field: ipFields)
            {
                field.removeFocusListener(listener);
            }
        }
    }
    
    private class KeyPressListener implements PropertyChangeListener
    {
        private JIPV4Field leftField, rightField;

        public KeyPressListener(JIPV4Field leftField, JIPV4Field rightField)
        {
            this.leftField = leftField;
            this.rightField = rightField;
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            String name = evt.getPropertyName();
            
            if(name == LEFT_PRESS && leftField != null)
            {
                leftField.requestFocus();
            }
            else if(name == RIGHT_PRESS && rightField != null)
            {
                rightField.requestFocus();
            }
            else if(name == BACK_SPACE_PRESS && leftField != null)
            {
                leftField.unSelectAllWhenFocusGained();
                leftField.requestFocus();
                leftField.setCaretPosition(leftField.getText().length());
            }
            else if(name == DELETE_PRESS && rightField != null)
            {
                rightField.unSelectAllWhenFocusGained();
                rightField.requestFocus();
                rightField.setCaretPosition(0);
            }
        }
    }
    
    private class JIPV4Field extends JTextField implements ActionListener, FocusListener, Serializable
    {
        private static final long serialVersionUID = 1411564647463716520L;
        
        private boolean selectAll;

        public JIPV4Field()
        {
            super();
            selectAll = true;
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(EMPTY_BORDER);
            setOpaque(false);
            setMargin(new Insets(0, 0, 0, 0));
            setDocument(new IPBlockDocument());
            setFont(OverIPV4AddressField.this.getFont());
            setSelectionColor(OverIPV4AddressField.this.getSelectionColor());
            setSelectedTextColor(OverIPV4AddressField.this.getSelectedTextColor());
            setEditable(OverIPV4AddressField.this.isEditable());
            setDisabledTextColor(OverIPV4AddressField.this.getDisabledTextColor());
            setCaretColor(OverIPV4AddressField.this.getCaretColor());
            setForeground(OverIPV4AddressField.this.getForeground());
            setEnabled(OverIPV4AddressField.this.isEnabled());
            addActionListener(this);
            addFocusListener(this);
        }
        
        public void unSelectAllWhenFocusGained()
        {
            if(!this.isFocusOwner())
            {
                selectAll = false;
            }
        }
        
        public void paste()
        {
            String clipboardText, text, selectedText;
            
            if(isEditable() && isEnabled() && (clipboardText = OnlyFeelUtil.getSystemClipboardText()) != null && !clipboardText.isEmpty())
            {
                if(Pattern.matches(IP_REGEX, clipboardText))
                {
                    setIpAddress(clipboardText);
                }
                else if((text = this.getText()) != null && !text.isEmpty()
                                && (selectedText = this.getSelectedText()) != null && !selectedText.isEmpty())
                {
                    int selStart = this.getSelectionStart();
                    int selEnd = this.getSelectionEnd();
                    String newText = text.substring(0, selStart) + clipboardText + text.substring(selEnd);
                    
                    if(Pattern.matches(IP_BLOCK_REGEX, newText))
                    {
                        this.setText(newText);
                    }
                }
                else
                {
                    super.paste();
                }
            }
        }
        
        public void actionPerformed(ActionEvent e)
        {
            transferFocus();
        }
        
        public void focusGained(FocusEvent e)
        {
            if(selectAll)
            {
                selectAll();
            }
            else
            {
                selectAll = true;
            }
        }

        public void focusLost(FocusEvent e)
        {}

        private class IPBlockDocument extends PlainDocument
        {
            private static final long serialVersionUID = -2645957214215338331L;
            
            private final String INT_REGEX = "0|([1-9]\\d*)?";
            
            private final Pattern INT_PATTERN = Pattern.compile(INT_REGEX);
            
            private StringBuilder text = new StringBuilder();
            
            private Matcher matcher;
            
            private int oldLength;
            
            private int newLength;
            
            private int length;
            
            private int ipBlockInt;
            
            public void insertString(int offset, String input, AttributeSet a) throws BadLocationException
            {
                oldLength = getLength();
                newLength = input.length();
                length = oldLength + newLength;
                text.delete(0, text.length());
                text.append(getText(0, oldLength));
                text.insert(offset, input);
                matcher = INT_PATTERN.matcher(text);
                
                if(length > 3 || !matcher.matches())
                {
                    return;
                }
                
                ipBlockInt = text.length() == 0? 0: Integer.parseInt(text.toString());
                
                if(ipBlockInt > 255)
                {
                    return;
                }
                
                super.insertString(offset, input, a);
            }
        }
    }
    
    private class Dot extends JLabel
    {
        private static final long serialVersionUID = -2704811830155290868L;

        public Dot()
        {
            super(".");
            setOpaque(false);
            setBorder(EMPTY_BORDER);
            setForeground(OverIPV4AddressField.this.getForeground());
            setEnabled(OverIPV4AddressField.this.isEnabled());
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        public void setEnabled(boolean enabled)
        {
            super.setEnabled(enabled);
            setForeground(enabled? OverIPV4AddressField.this.getForeground(): OverIPV4AddressField.this.getDisabledTextColor());
        }
        
        public void updateUI()
        {
            super.updateUI();
            setFont(OverIPV4AddressField.this.getFont());
        }
    }
}