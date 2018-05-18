package com.speyejack.gui;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Controller extends JFrame {
	private static final long serialVersionUID = 666097978718417929L;
	JPanel panel;

	public Controller(MainGUI main) {
		this.setTitle("Controller");
		this.setIconImage(new ImageIcon("Fighter.png").getImage());
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.setLocationRelativeTo(main);

		JButton maxButton = new JButton("Get Top");
		maxButton.addActionListener(main);
		
		panel.add(maxButton);
		
		this.add(panel);

		this.setVisible(true);
		this.pack();
	}
}
