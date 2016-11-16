/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.component;

/**
 *
 * @author XiaHui
 */
import javax.swing.*;

public class TestHyperLinkFLabel {

    public static void main(String[] args) {
        JFrame frame = new JFrame("测试HyperLinkFLabel");
        JPanel panel = new JPanel();
        HyperLinkFLabel label = new HyperLinkFLabel("百度", "http://www.baidu.com");
        label.addMouseListener(label);
        panel.add(label);
        frame.add(panel);
        frame.setSize(400, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);
    }
}
