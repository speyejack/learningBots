package com.speyejack.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.speyejack.learning.gui.FitGraphPanel;
import com.speyejack.learning.gui.GenomeGraph;
import com.speyejack.learning.neat.Genome;
import com.speyejack.learning.neat.Pool;

public class DebugGUI extends JFrame {
	private static final long serialVersionUID = -9222928324830586875L;

	Dimension dim = new Dimension(500, 500);
	List<Pool> pools;
	JTabbedPane fitPanes;
	JTabbedPane genomePanes;

	public DebugGUI() {
		super();
		this.setLayout(new CardLayout());
		pools = new ArrayList<Pool>();
		fitPanes = new JTabbedPane();
		genomePanes = new JTabbedPane();
		JTabbedPane tools = new JTabbedPane();
		tools.addTab("Fitness Graphs", fitPanes);
		tools.addTab("Genome Graphs", genomePanes);
		// tools.addChangeListener(new ChangeListener() {
		//
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// pack();
		// }
		// });

		this.setTitle("Debugger");
		this.setIconImage(new ImageIcon("Fighter.png").getImage());
		this.add(tools);
		this.setVisible(true);
		this.setResizable(false);
		this.pack();
	}

	public void addPool(String title, Pool pool) {
		FitGraphPanel fitGraph = new FitGraphPanel(dim, pool);
		fitPanes.add(title, fitGraph);

		JPanel jpanel = new JPanel(new GridBagLayout());
		GenomeGraph genomeGraph = new GenomeGraph(dim);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 1;
		genomeGraph.setGenome(pool.getGenome(0));
		jpanel.add(genomeGraph, c);
		JTextField text = new JTextField();
		text.setPreferredSize(new Dimension(200, 20));
		c = new GridBagConstraints();
		jpanel.add(text, c);
		
		JButton maxButton = new JButton("Get Max");
		maxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Genome g = pool.getTopGenome();
				if (g == null)
					return;
				genomeGraph.setGenome(g);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		jpanel.add(maxButton, c);
		
		JButton button = new JButton("Go");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = 0;
				try {
					index = Integer.parseInt(text.getText());
				} catch (NumberFormatException ex) {
					text.setText("Bad Num");
				}
				Genome g = pool.getGenome(index);
				if (g == null)
					return;
				genomeGraph.setGenome(g);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		jpanel.add(button, c);
		
		int size = (int) (dim.getHeight()
				- Math.max(text.getPreferredSize().getHeight(), button.getPreferredSize().getHeight()));
		genomeGraph.setPreferredSize(new Dimension(size, size));
		genomePanes.add(title, jpanel);
		this.repaint();
		this.pack();
	}
}
