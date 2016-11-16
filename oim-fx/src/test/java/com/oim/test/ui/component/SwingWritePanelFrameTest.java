/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.test.ui.component;

import javax.swing.UIManager;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Only
 */
public class SwingWritePanelFrameTest extends Application {
	SwingWritePanelFrame swingWritePanelFrame=new SwingWritePanelFrame();
    @Override
    public void start(Stage primaryStage) {
    	swingWritePanelFrame.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	try {
			//			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(SwingWritePanelFrameTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
        launch(args);
    }

}
