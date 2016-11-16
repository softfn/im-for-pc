package com.oim.test.ui.login;

import java.awt.EventQueue;

import com.oim.ui.LoginFrame;
import com.oim.ui.login.ComboBoxAction;

/**
 * 描述：
 * @author XiaHui 
 * @date 2016年1月6日 下午11:04:34
 * @version 0.0.1
 */
public class LoginFrameTest {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				long time = System.currentTimeMillis();
				try {
					LoginFrame frame = new LoginFrame();
					frame.addAccount("1233333");
					frame.addAccount("2222222");
					frame.addAccount("3344333");
					frame.addAccount("2344222");
					frame.addAccount("2344272");
					frame.addAccount("2346622");
					frame.addAccount("2394222");
					frame.addAccount("2442222");
					frame.add(new ComboBoxAction(){

						@Override
						public void itemChange(String text) {
							System.out.println("itemChange"+text);
						}

						@Override
						public void delete(Object o) {
							System.out.println("delete"+o);
						}

						@Override
						public void select(Object o) {
							System.out.println("select"+o);
							
						}});
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(System.currentTimeMillis() - time);
			}
		});
	}
}
