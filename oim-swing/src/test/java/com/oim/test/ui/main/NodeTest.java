package com.oim.test.ui.main;

import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.component.list.HeadLabel;
import com.oim.ui.component.list.Node;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class NodeTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private JPanel chatPanel = new JPanel();
	ImageIcon icon = new ImageIcon("Resources/Images/List/big.png");
	private JScrollPane scrollPane = new JScrollPane();
	/**
	 * Create the frame.
	 */
	public NodeTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(320, 240);
		//this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		//chatPanel.setBackground(Color.blue);
		
		
		
		//h2.addBusinessAttribute(new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));

		
//		NodeTemp n1=new NodeTemp(chatPanel);
//		NodeTemp n2=new NodeTemp(chatPanel);
//		
//		n1.setRootText("ggg");
//		
//		chatPanel.add(n1);
//		chatPanel.add(n2);
		
		
		Node[] node = new Node[5];
	        for (int j = 0; j < 5; j++) {
	            node[j] = new Node();
	            node[j].setTitleText("我的好友" + j);
	            for (int i = 0; i < 5; i++) {
	            	
	            	HeadLabel head=new HeadLabel();
	        		head.setIcon(icon);
	        		
	        		head.setStatusIcon(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));

	        		head.setHeadIcon(new ImageIcon("Resources/Images/Head/User/1.png"));
	        		
	        		head.setRemark("女神经");
	        		head.setNickname("(也的JJ刚刚)");
	        		head.setStatus("[2G]");
	        		head.setShowText("哈哈哈，又有新闻了");
	        		head.addBusinessAttribute("s",new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));
	        		head.addBusinessAttribute("s",new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));

	        		
	              
	                
	                node[j].addNode(head);
	            }
	            chatPanel.add(node[j]);
	        }

		
		
		
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		scrollPane.setViewportView(chatPanel);
		
		add(scrollPane);

	}

	private void initEvent() {
		
	}

	

	public void formComponentResized() {
		scrollPane.setBounds(3, 50, this.getWidth() - 6, this.getHeight() - 100);
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
					NodeTest frame = new NodeTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
