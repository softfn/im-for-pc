package com.oim.test.component;

/**
 * @author XiaHui
 * @date 2015年2月27日 下午6:21:01
 */
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 控制JScrollPane自动滚动到某组件位置
 * 
 * @author 五斗米 <如转载请保留作者和出处>
 * @blog http://blog.csdn.net/mq612
 */

public class ScrollPanePoint extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JScrollPane sp = null;

	private JPanel pane = null;

	private JButton[] button = null;
	
	JPanel[] panes=null;

	public ScrollPanePoint() {
		pane = new JPanel(new GridLayout(20, 1));
		
		button = new JButton[20];
		
		for (int i = 0; i < button.length; i++) { // 将所有按钮添加到panel容器中
			//button[i] = new JButton("[ " + i + " ]");
			button[i] = new JButton();
			button[i].setLayout(new CardLayout());
			JPanel	p = new JPanel();
			if(i%2>0){
				p.setBackground(Color.blue);
			}else{
				p.setBackground(Color.red);
			}
			button[i].add(p);
			
			pane.add(button[i]);
		}
//		panes=new JPanel[20];
//		for (int i = 0; i < panes.length; i++) { // 将所有按钮添加到panel容器中
//			panes[i] = new JPanel();
//			if(i%2>0){
//				panes[i].setBackground(Color.blue);
//			}else{
//				panes[i].setBackground(Color.red);
//			}
//			pane.add(panes[i]);
//		}
		sp = new JScrollPane(pane); // 将panel放到JScrollPane中
		this.getContentPane().add(sp);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(350, 200);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		// 现在确定button[12]的位置，如果是JTree则需要确定某个节点的位置
//		Point p = button[12].getLocation();
//
//		// 获取JScrollPane中的纵向JScrollBar
//		JScrollBar sBar = sp.getVerticalScrollBar();
//
//		// 设置滚动到button[12]所在位置
//		sBar.setValue(p.y);
	}

	public static void main(String[] arg) {
		new ScrollPanePoint();
	}

}
