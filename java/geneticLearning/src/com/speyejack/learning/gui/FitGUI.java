package com.speyejack.learning.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.speyejack.learning.neat.Pool;

public class FitGUI extends JFrame {
	private static final long serialVersionUID = -8851193935352263415L;
	List<FitGraphPanel> graphs;
	Dimension dim = new Dimension(500, 500);
	JTabbedPane tabpane;
	List<Pool> pools;

	public FitGUI() {
		super();
		this.setLayout(new CardLayout());
		pools = new ArrayList<Pool>();
		tabpane = new JTabbedPane();
		this.add(tabpane);
		this.setVisible(true);
		this.setResizable(false);
		this.pack();
	}

	public void addPool(String title, Pool pool) {
		FitGraphPanel graph = new FitGraphPanel(dim, pool);
		tabpane.addTab(title, graph);
		this.repaint();
		this.pack();
	}
}
