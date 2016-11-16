package com.oim.test.ui;

import java.awt.EventQueue;

import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.view.GroupDataView;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月29日 下午10:09:43
 * @version 0.0.1
 */
public class GroupDataViewTest {
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
					GroupDataView g=new GroupDataView(null);
					g.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
