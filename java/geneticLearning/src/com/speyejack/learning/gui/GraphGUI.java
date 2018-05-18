package com.speyejack.learning.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.speyejack.learning.neat.Genome;
import com.speyejack.learning.neat.Innovation;
import com.speyejack.learning.neat.RoutineF;

public class GraphGUI extends JFrame {
	private static final long serialVersionUID = 1270752383010766374L;
	private static final String FILENAME = "genome.gnm";
	private GenomeGraph graph;
	private Genome g;
	private long seed;

	public GraphGUI() {
		super();
		JPanel pane = new JPanel(new GridBagLayout());

		graph = new GenomeGraph(new Dimension(900, 900));
		g = getMutatedGenome(0);
		graph.setGenome(g);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 7;
		pane.add(graph, c);

		JTextField text = new JTextField();
		text.setPreferredSize(new Dimension(50, 20));
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		pane.add(text, c);

		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File file = new File(FILENAME);
					ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
					Genome g = (Genome) is.readObject();
					is.close();
					graph.setGenome(g);

				} catch (FileNotFoundException e1) {
					System.out.println("File not found");
				} catch (IOException e1) {
					System.out.println("IO error");
				} catch (ClassNotFoundException e1) {
					System.out.println("Class not found");
				} catch (Exception e1){
					System.out.println("Unknown Error");
				}

			}
		});
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		pane.add(loadButton, c);

		JButton debugButton = new JButton("Debug");
		debugButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				g = getMutatedGenome(0);
				int mutations = 0;
				do {
					if (mutations > 20)
						g = getMutatedGenome(0);
					g.forceMutation();
					mutations += 1;
					graph.setGenome(g);
				} while (!graph.debug());
			}
		});
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		pane.add(debugButton, c);

		JButton reMutButton = new JButton("Re-Mutate");
		reMutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int mutations = 1;
				try {
					mutations = Integer.parseInt(text.getText());
				} catch (NumberFormatException ex) {
				}
				g = getMutatedGenome(mutations, seed);
				graph.setGenome(g);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		pane.add(reMutButton, c);

		JButton mutButton = new JButton("Mutate");
		mutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				g.forceMutation();
				graph.setGenome(g);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		pane.add(mutButton, c);

		JButton regButton = new JButton("New Genome");
		regButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int mutations = 1;
				try {
					mutations = Integer.parseInt(text.getText());
				} catch (NumberFormatException ex) {
				}
				g = getMutatedGenome(mutations);
				graph.setGenome(g);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		pane.add(regButton, c);

		JButton refButton = new JButton("Refresh");
		refButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.setGenome(g);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		pane.add(refButton, c);

		this.add(pane);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	public Genome getMutatedGenome(int mutations) {
		seed = System.currentTimeMillis();
		RoutineF.setRandomSeed(seed);
		// System.out.println("-------");
		Genome g = new Genome(3, 6, new Innovation());
		for (int i = 0; i < mutations; i++) {
			g.forceMutation();
		}
		return g;
	}

	public Genome getMutatedGenome(int mutations, long seed) {
		RoutineF.setRandomSeed(seed);
		// System.out.println("---------");
		Genome g = new Genome(3, 6, new Innovation());
		for (int i = 0; i < mutations; i++) {
			g.forceMutation();
		}
		return g;
	}

	public static void main(String[] args) {
		new GraphGUI();
	}
}
