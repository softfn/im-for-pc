package com.over;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.UIManager;

import com.only.box.UIBox;

public class OverLoadingGlassPane extends JComponent implements ActionListener {
	
	private static final long serialVersionUID = -2161086706185983717L;

	private JProgressBar progressBar;
	private JLabel lbInfo;
	private JButton btnCancel;
	private String info;
	private boolean cancelButtonVisible;
	private boolean cancelled;
	private ScheduledThreadPoolExecutor executor;
	private ScheduledFuture<?> future;
	private Action finishedAction, cancelAction;
	private float alpha;
	private byte[] lock;

	public OverLoadingGlassPane() {
		this(null);
	}

	public OverLoadingGlassPane(JRootPane owner) {
		this(owner, true, true);
	}

	public OverLoadingGlassPane(JRootPane owner, boolean indeterminate, boolean cancelButtonVisible) {
		this(owner, indeterminate, cancelButtonVisible, null);
	}

	public OverLoadingGlassPane(JRootPane owner, boolean indeterminate, boolean cancelButtonVisible, String info) {
		if (owner != null) {
			owner.setGlassPane(this);
		}

		init();
		progressBar.setIndeterminate(indeterminate);
		setCancelButtonVisible(cancelButtonVisible);
		setInfo(info);
	}

	private void init() {
		progressBar = new OverRound3DProgressBar();
		lbInfo = new JLabel();
		btnCancel = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
		lock = new byte[0];
		alpha = 0.65f;

		lbInfo.setVerticalAlignment(JLabel.BOTTOM);
		lbInfo.setHorizontalAlignment(JLabel.LEFT);
		progressBar.setPreferredSize(new Dimension(-1, 12));
		btnCancel.setPreferredSize(new Dimension(-1, 22));
		btnCancel.addActionListener(this);
		this.setOpaque(false);
		this.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		this.setVisible(false);
		this.setBackground(UIBox.getWhiteColor());
		this.setForeground(Color.BLUE);
		this.setFocusTraversalKeysEnabled(false);
		this.setLayout(null);
		this.add(lbInfo);
		this.add(progressBar);
		this.add(btnCancel);
		this.addMouseListener(new MouseAdapter() {
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
		});
		this.addKeyListener(new KeyAdapter() {
		});
		this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent evt) {
				requestFocusInWindow();
			}

			public void componentResized(ComponentEvent e) {
				reLayout();
			}
		});
	}

	private void reLayout() {
		int width = this.getWidth();
		int height = this.getHeight();
		int buttonHeight = btnCancel.getPreferredSize().height;
		int barWidth = width * 2 / 3;
		int barHeight = progressBar.getPreferredSize().height;
		int barX = (width - barWidth) / 2;
		int barY = (height - barHeight) / 2;

		progressBar.setBounds(barX, barY, barWidth, barHeight);
		lbInfo.setBounds(barX, 0, barWidth, barY - 10);

		if (cancelButtonVisible) {
			btnCancel.setBounds(0, height - buttonHeight, width, buttonHeight);
		}
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();
		Rectangle clip = g.getClipBounds();
		g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
		g2d.setColor(getBackground());
		g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
		g2d.setComposite(oldComposite);
	}

	public void runCommand(Runnable command) {
		runCommand(command, true);
	}

	public void runCommandWithoutLock(Runnable command) {
		runCommand(command, false);
	}

	private void runCommand(Runnable command, boolean wait) {
		cancelled = false;
		Task<?> task = new Task<Object>(command, null);
		executor = new ScheduledThreadPoolExecutor(1);
		future = executor.schedule(task, 0, TimeUnit.MILLISECONDS);
		setVisible(true);

		if (wait) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stopCommand() {
		future.cancel(true);
		cancelled = true;
	}

	public void actionPerformed(ActionEvent e) {
		if (cancelAction != null) {
			cancelAction.actionPerformed(null);
		} else {
			stopCommand();
		}
	}

	public void setFont(Font font) {
		super.setFont(font);
		lbInfo.setFont(font);
	}

	public void setForeground(Color color) {
		super.setForeground(color);
		lbInfo.setForeground(color);
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		if (alpha >= 0.0f && alpha <= 1.0f) {
			this.alpha = alpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid alpha:" + alpha);
		}
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

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JButton getCancelButton() {
		return btnCancel;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
		lbInfo.setText("<html>" + info);
	}

	public boolean isCancelButtonVisible() {
		return cancelButtonVisible;
	}

	public void setCancelButtonVisible(boolean cancelButtonVisible) {
		this.cancelButtonVisible = cancelButtonVisible;
		btnCancel.setVisible(cancelButtonVisible);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	private class Task<V> extends FutureTask<V> {
		public Task(Runnable runnable, V result) {
			super(runnable, result);
		}

		protected void done() {
			executor.shutdown();
			executor = null;
			future = null;
			setVisible(false);

			if (!cancelled && finishedAction != null) {
				finishedAction.actionPerformed(null);
			}

			synchronized (lock) {
				lock.notifyAll();
			}
		}
	}
}