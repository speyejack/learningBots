package com.speyejack.learning.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DebugTimer extends JFrame {
	private static final long serialVersionUID = 7398083764402047449L;

	JPanel panel;
	List<JTextField> textFields;
	List<String> secNames;
	String starter;
	int index;
	double startTime;
	double lastTime;

	public DebugTimer(String starter) {
		this.setTitle("DebugTimer");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		textFields = new ArrayList<JTextField>();

		this.add(panel);

		this.setVisible(true);
		this.pack();
	}
	
	
	public DebugTimer(List<String> sections) {
		this.setTitle("DebugTimer");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		textFields = new ArrayList<JTextField>();
		secNames = sections;
		int maxSize = 0;
		for (int i = 0; i < sections.size(); i++)
			maxSize = Math.max(maxSize, sections.get(i).length());
		for (int i = 0; i < sections.size(); i++) {
			secNames.set(i, String.format("%-" + maxSize + "s", sections.get(i)+":") + "\t");
			JTextField textF = new JTextField(
					secNames.get(i) + String.format("%.3f", 0.0) + ":" + String.format("%.3f", 0.0));
			textF.setEditable(false);
			panel.add(textF);
			textFields.add(textF);
		}

		this.add(panel);

		this.setVisible(true);
		this.pack();
	}

	public void start() {
		for (int i = 0; i < textFields.size(); i++) {
			textFields.get(i).setText(secNames.get(i) + "---:---");
		}
		index = 0;
		startTime = System.nanoTime();
		lastTime = 0;
	}

	public void mark() {
		double time = System.nanoTime() - startTime;
		textFields.get(index).setText(secNames.get(index) + String.format("%.3f", time / 1000000000) + ":"
				+ String.format("%.3f", (time - lastTime) / 1000000000));
		lastTime = time;
		index++;
	}

	public void finish() {
		double time = System.nanoTime() - startTime;
		for (int i = index; i < textFields.size(); i++) {
			textFields.get(index).setText(secNames.get(index) + String.format("%.3f", time / 1000000000) + ":"
					+ String.format("%.3f", (time - lastTime) / 1000000000));
			lastTime = time;
			index++;
		}
	}

}
