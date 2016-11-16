package com.only;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.border.EmptyBorder;

import com.only.box.UIBox;
import com.only.component.ImagePane;
import com.only.layout.LineLayout;

public class OnlyMessageDialog extends OnlyBorderDialog implements ActionListener {

    private static final long serialVersionUID = 3983953036367048891L;
    public static final int CLOSE_OPTION = 0;
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 1 << 1;
    public static final int YES_OPTION = 1 << 2;
    public static final int NO_OPTION = 1 << 3;

    public static enum MessageType {
        ERROR, INFORMATION, WARNING, QUESTION
    }
    private OnlyButton btnOK, btnCancel, btnYes, btnNo;
    private OnlyLabel lbMessage;
    private Map<OnlyMessageDialog.MessageType, Icon> iconMap;
    private int option;

    private OnlyMessageDialog(Window owner, String title, String message, OnlyMessageDialog.MessageType messageType, int option) {
        super(owner, title, Dialog.ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init(message, messageType, option);
    }

    public static OnlyMessageDialog createMessageBox(Window parent, String title, String message, OnlyMessageDialog.MessageType messageType, int option) {
        return new OnlyMessageDialog(parent, title, message, messageType, option);
    }

    public static OnlyMessageDialog createErrorMessageBox(Window parent, String title, String message) {
        return createErrorMessageBox(parent, title, message, OK_OPTION);
    }

    public static OnlyMessageDialog createErrorMessageBox(Window parent, String title, String message, int option) {
        return createMessageBox(parent, title, message, OnlyMessageDialog.MessageType.ERROR, option);
    }

    public static OnlyMessageDialog createInformationMessageBox(Window parent, String title, String message) {
        return createInformationMessageBox(parent, title, message, OK_OPTION);
    }

    public static OnlyMessageDialog createInformationMessageBox(Window parent, String title, String message, int option) {
        return createMessageBox(parent, title, message, OnlyMessageDialog.MessageType.INFORMATION, option);
    }

    public static OnlyMessageDialog createWarningMessageBox(Window parent, String title, String message) {
        return createWarningMessageBox(parent, title, message, OK_OPTION | CANCEL_OPTION);
    }

    public static OnlyMessageDialog createWarningMessageBox(Window parent, String title, String message, int option) {
        return createMessageBox(parent, title, message, OnlyMessageDialog.MessageType.WARNING, option);
    }

    public static OnlyMessageDialog createQuestionMessageBox(Window parent, String title, String message) {
        return createQuestionMessageBox(parent, title, message, YES_OPTION | NO_OPTION);
    }

    public static OnlyMessageDialog createQuestionMessageBox(Window parent, String title, String message, int option) {
        return createMessageBox(parent, title, message, OnlyMessageDialog.MessageType.QUESTION, option);
    }

    private void init(String message, OnlyMessageDialog.MessageType messageType, int option) {
        iconMap = new HashMap<OnlyMessageDialog.MessageType, Icon>();
       
        iconMap.put(OnlyMessageDialog.MessageType.ERROR, UIBox.getIcon(UIBox.key_icon_message_box_error));
        iconMap.put(OnlyMessageDialog.MessageType.INFORMATION, UIBox.getIcon(UIBox.key_icon_message_box_information));
        iconMap.put(OnlyMessageDialog.MessageType.QUESTION, UIBox.getIcon(UIBox.key_icon_message_box_question));
        iconMap.put(OnlyMessageDialog.MessageType.WARNING, UIBox.getIcon(UIBox.key_icon_message_box_warning));

        ImagePane buttonPane = new ImagePane();
        lbMessage = new OnlyLabel(message, iconMap.get(messageType), OnlyLabel.LEFT);
        btnOK = new OnlyButton("确定");
        btnCancel = new OnlyButton("取消");
        btnYes = new OnlyButton("是");
        btnNo = new OnlyButton("否");
        OnlyButton[] buttons = {btnOK, btnYes, btnNo, btnCancel};
        int[] options = {OK_OPTION, YES_OPTION, NO_OPTION, CANCEL_OPTION};
        final Dimension buttonSize = new Dimension(69, 21);
        int index = 0;
        boolean hasDefaultButton = false;

        lbMessage.setBackground(UIBox.getWhiteColor());
        lbMessage.setBackgroundAlpha(0.9f);
        lbMessage.setIconTextGap(16);
        lbMessage.setHorizontalAlignment(OnlyLabel.LEFT);
        lbMessage.setVerticalAlignment(OnlyLabel.TOP);
        lbMessage.setVerticalTextPosition(OnlyLabel.TOP);
        lbMessage.setBorder(new EmptyBorder(15, 25, 15, 25));
        lbMessage.setDeltaY(5);
        buttonPane.setLayout(new LineLayout(6, 0, 0, 0, 0, LineLayout.LEADING, LineLayout.LEADING, LineLayout.HORIZONTAL));
        buttonPane.setPreferredSize(new Dimension(-1, 33));
        buttonPane.setBorder(new EmptyBorder(5, 9, 0, 9));
        buttonPane.setBackground(new Color(255, 255, 255, 170));
        buttonPane.setCornerSizeAt(3, 2);
        buttonPane.setCornerSizeAt(4, 2);

        for (OnlyButton button : buttons) {
            button.setActionCommand(String.valueOf(options[index]));
            button.setPreferredSize(buttonSize);
            button.setVisible((option & options[index]) != 0);
            button.addActionListener(this);
            buttonPane.add(button, LineLayout.END);
            index++;

            if (!hasDefaultButton && button.isVisible()) {
                getRootPane().setDefaultButton(button);
                hasDefaultButton = true;
            }
        }

        getContentPane().setLayout(new LineLayout(0, 1, 1, 3, 1, LineLayout.LEADING, LineLayout.LEADING, LineLayout.VERTICAL));
        getContentPane().add(lbMessage, LineLayout.MIDDLE_FILL);
        getContentPane().add(buttonPane, LineLayout.END_FILL);
    }

    public int open() {
        option = CLOSE_OPTION;
        this.setSize(this.getPreferredSize());
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setVisible(true);
        return option;
    }

    @Override
    public Dimension getPreferredSize() {
        int width = Math.max(315, lbMessage.getPreferredSize().width) + 15;
        int height = Math.max(150, lbMessage.getPreferredSize().height + 15 + 28 + 34);
        return new Dimension(width, height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OnlyButton button = (OnlyButton) e.getSource();
        option = Integer.parseInt(button.getActionCommand());
        this.dispose();
    }
}