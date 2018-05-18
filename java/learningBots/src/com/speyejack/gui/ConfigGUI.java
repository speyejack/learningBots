package com.speyejack.gui;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.speyejack.bots.Arena;

public class ConfigGUI extends JFrame {
	private static final long serialVersionUID = -4435718791950687952L;

	private JPanel panel;
	private JTextField genText;
	private JCheckBox renderCB;
	private JCheckBox maxSpeedCB;

	public ConfigGUI(JFrame main) {
		this.setTitle("Config");
		this.setIconImage(new ImageIcon("Fighter.png").getImage());
		panel = new JPanel();
		genText = new JTextField("Gen:     ");
		renderCB = new JCheckBox("Renderer");
		maxSpeedCB = new JCheckBox("Max Speed");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.setLocationRelativeTo(main);
		// this.setLocation((int) (main.getLocation().getX() +
		// main.getWidth()),(int) main.getLocation().getY());
		genText.setEditable(false);

		this.add(panel);

		panel.add(genText);
		panel.add(renderCB);
		panel.add(maxSpeedCB);

		renderCB.setSelected(true);
		maxSpeedCB.setSelected(true);

		this.setVisible(true);
		this.pack();
	}

	public void updateText(Arena arena) {
		genText.setText("Gen: " + arena.getGenNumberString());
	}

	public boolean isRendering() {
		return renderCB.isSelected();
	}

	public boolean isMaxSpeed() {
		return maxSpeedCB.isSelected();
	}
}
