package com.over;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.only.common.WindowMove;

public class OverLoadingDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = -6555677237581881143L;
    /**
     * 内容面板
     */
    private Container contentPane;
    /**
     * 进度条
     */
    private JProgressBar progressBar;
    /**
     * 显示提示信息的Label
     */
    private JLabel lbInfo;
    /**
     * 取消按钮
     */
    private JButton btnCancel;
    /**
     * 父窗体
     */
    private Window parent;
    /**
     * 间接实现布局管理的监听器
     */
    private ComponentListener listener;
    /**
     * 需要关联进度条的动作
     */
    private Runnable command;
    /**
     * 动作的提示信息
     */
    private String statusInfo;
    /**
     * 是否显示“取消”按钮
     */
    private boolean cancelButtonVisible;
    /**
     * 动作是否被取消
     */
    private boolean cancelled;
    /**
     * 动作完成时执行
     */
    private Action finishedAction;
    /**
     * 动作被取消时执行
     */
    private Action cancelAction;
    /**
     * 线程管理对象
     */
    private ScheduledThreadPoolExecutor executor;
    /**
     * 执行任务的Future
     */
    private ScheduledFuture<?> future;
    /**
     * 鼠标托动窗口的实现
     */
    private WindowMove move;

    /**
     * 构造方法
     *
     * @param parent 父窗体
     */
    public OverLoadingDialog(Window parent) {
        this(parent, null, null);
    }

    /**
     * 构造方法
     *
     * @param parent 父窗体
     * @param command 需要关联进度条的动作
     * @param statusInfo 当前线程动作提示信息
     */
    public OverLoadingDialog(Window parent, Runnable command, String statusInfo) {
        this(parent, command, statusInfo, true);
    }

    /**
     * 构造方法
     *
     * @param parent 父窗体
     * @param command 需要关联进度条的动作
     * @param statusInfo 当前线程动作提示信息
     * @param cancelButtonVisible 是否显示“取消”按钮
     */
    public OverLoadingDialog(Window parent, Runnable command, String statusInfo, boolean cancelButtonVisible) {
        super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
        this.move = new WindowMove(this);
        this.parent = parent;
        this.command = command;
        this.statusInfo = statusInfo;
        this.cancelButtonVisible = cancelButtonVisible;
        initUI();
    }

    private void initUI() {
        contentPane = getContentPane();
        progressBar = new OverRound3DProgressBar();
        lbInfo = new JLabel("<html>" + statusInfo);
        btnCancel = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
        listener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                reLayout();
            }
        };

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        progressBar.setPreferredSize(new Dimension(-1, 15));
        progressBar.setIndeterminate(true);
        btnCancel.setPreferredSize(new Dimension(75, 22));
        btnCancel.addActionListener(this);
        btnCancel.setVisible(cancelButtonVisible);
        lbInfo.setVerticalAlignment(JLabel.TOP);
        contentPane.setLayout(null);
        contentPane.add(progressBar);
        contentPane.add(lbInfo);
        contentPane.add(btnCancel);
        contentPane.addComponentListener(listener);
        setUndecorated(true);
        setResizable(false);
        setBorder(new LineBorder(new Color(200, 230, 230), 2, false));
        setSize(390, 100);
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                actionPerformed(null);
            }
        });
    }

    private void reLayout() {
        int width = contentPane.getWidth();
        int height = contentPane.getHeight();
        Dimension buttonSize = btnCancel.getPreferredSize();
        Dimension barSize = progressBar.getPreferredSize();
        int labelY = 30 + barSize.height;
        progressBar.setBounds(15, 20, width - 30, barSize.height);
        lbInfo.setBounds(15, labelY, width - 30, height - 10 - labelY);

        if (btnCancel.isVisible()) {
            btnCancel.setBounds(width - buttonSize.width - 10, height - buttonSize.height - 10, buttonSize.width, buttonSize.height);
        }
    }

    public void changeContentPane(Container newContentPane) {
        contentPane.removeComponentListener(listener);
        newContentPane.removeAll();
        newContentPane.setLayout(null);
        newContentPane.addComponentListener(listener);

        for (Component c : contentPane.getComponents()) {
            newContentPane.add(c);
        }

        setContentPane(newContentPane);
        getRootPane().doLayout();
        this.contentPane = newContentPane;
    }

    public void runCommand(Runnable command) {
        this.command = command;
        runCommand();
    }

    public void runCommand() {
        cancelled = false;
        OverLoadingDialog.Task<?> task = new OverLoadingDialog.Task<Object>(command, null);
        executor = new ScheduledThreadPoolExecutor(1);
        future = executor.schedule(task, 0, TimeUnit.MILLISECONDS);
        setVisible(true);
    }

    public void stopCommand() {
        future.cancel(true);
        cancelled = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (cancelButtonVisible) {
            if (cancelAction != null) {
                cancelAction.actionPerformed(null);
            } else {
                stopCommand();
            }
        }
    }

    public void setBorder(Border border) {
        getRootPane().setBorder(border);
    }

    public Border getBorder() {
        return getRootPane().getBorder();
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getInfoLabel() {
        return lbInfo;
    }

    public JButton getCancelButton() {
        return btnCancel;
    }

    @Override
    public Window getParent() {
        return parent;
    }

    public Runnable getCommand() {
        return command;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
        lbInfo.setText("<html>" + statusInfo);
    }

    public ActionListener getCancelAction() {
        return cancelAction;
    }

    public void setCancelAction(Action cancelAction) {
        this.cancelAction = cancelAction;
    }

    public Action getFinishedAction() {
        return finishedAction;
    }

    public void setFinishedAction(Action finishedAction) {
        this.finishedAction = finishedAction;
    }

    public boolean isCancelButtonVisible() {
        return cancelButtonVisible;
    }

    public void setCancelButtonVisible(boolean cancelButtonVisible) {
        this.cancelButtonVisible = cancelButtonVisible;
        btnCancel.setVisible(cancelButtonVisible);
    }

    public boolean isMoveable() {
        return move.isMoveable();
    }

    public void setMoveable(boolean moveable) {
        move.setMoveable(moveable);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    private class Task<V> extends FutureTask<V> {

        public Task(Runnable runnable, V result) {
            super(runnable, result);
        }

        @Override
        protected void done() {
            executor.shutdown();
            executor = null;
            future = null;
            dispose();

            if (!cancelled && finishedAction != null) {
                finishedAction.actionPerformed(null);
            }
        }
    }
}