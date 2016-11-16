package com.oim.test.ui.sound;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.common.sound.SoundHandler;
import com.oim.common.task.ExecuteTask;
import com.oim.common.task.QueueTaskThread;
import com.oim.ui.chat.FacePanel;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class SoundPlayTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	FacePanel facePanel=FacePanel.getFacePanel();
	private JButton grayButton = new JButton();
	SoundHandler sh=new SoundHandler();
	int i=1;
	private QueueTaskThread queueTaskThread = new QueueTaskThread();
	public SoundPlayTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		grayButton.setText("变灰");
		
		grayButton.setBounds(20, 50, 120, 25);
		
		grayButton.setIcon(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));
		grayButton.setSelectedIcon(new ImageIcon("Resources/Images/Face/1.gif "));
		
		add(grayButton);
	}

	private void initEvent() {
		grayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gray();
			}
		});
	}
	private void gray(){
		ExecuteTask executeTask =new ExecuteTask(){

			@Override
			public void execute() {
				sh.play(i);
				i++;
				if(i>=6){i=1;}
				
			}};
		
		
		queueTaskThread.add(executeTask);
	}
	
	class ExecuteRunnable extends Thread {
		private Map<Integer, Integer> typeMap = new HashMap<Integer, Integer>();

		@Override
		public void run() {
			while (true) {
				handleTask();// 处理任务
				threadSleep(200);
			}
		}

		private void threadSleep(long time) {
			try {
				sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 处理任务队列，检查其中是否有任务
		 */
		private void handleTask() {
			if(typeMap.isEmpty()){
				
			}
		}
		
		public void add(int type){
			typeMap.put(type, type);
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SoundPlayTest frame = new SoundPlayTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
