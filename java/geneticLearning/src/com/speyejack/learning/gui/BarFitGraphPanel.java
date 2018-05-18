package com.speyejack.learning.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.speyejack.learning.neat.Pool;

public class BarFitGraphPanel extends JPanel {
	private static final long serialVersionUID = -5251059032624186130L;
	private static final double Y_SCALAR = 15;
	List<int[]> dataCache;
	int gen = 0;
	int maxFit = 0;
	Dimension dim;

	public BarFitGraphPanel(Dimension dim, Pool pool) {
		dataCache = new ArrayList<int[]>();
		pool.addFitPanel(this);
		this.dim = dim;
		this.setPreferredSize(dim);
	}

	public void addData(int[] data) {
		dataCache.add(data);
		if (gen < data[0])
			gen = data[0];
		if (maxFit < data[3])
			maxFit = data[3];
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (dataCache.size() < 1)
			return;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
		for (int i = 1; i < dataCache.size(); i++) {

			g.setColor(Color.gray);
			g.drawLine(adjustWidth(dataCache.get(i)[0]), adjustHeight(dataCache.get(i)[1]),
					adjustWidth(dataCache.get(i)[0]), adjustHeight(dataCache.get(i)[3]));
			g.setColor(Color.MAGENTA);
			g.drawLine(adjustWidth(dataCache.get(i - 1)[0]), adjustHeight(dataCache.get(i - 1)[2]),
					adjustWidth(dataCache.get(i)[0]), adjustHeight(dataCache.get(i)[2]));
		}
		g.setColor(Color.white);
		for (int i = 1; i < Y_SCALAR; i++) {
			double pos = 1.0 - i / Y_SCALAR;
			int height = (int) (dim.getHeight() - dim.getHeight() * pos);
			g.drawLine(0, height, (int) (0.01 * dim.getWidth()), height);
			g.drawString(Integer.toString((int) (maxFit * 1.1 - (i * maxFit * 1.1 / Y_SCALAR))),
					(int) (0.011 * dim.getWidth()), height + 5);
		}

		// System.out.println(gen + ":" + (int) (maxFit * 1.1));
	}

	private int adjustWidth(double width) {
		return (int) (dim.getWidth() * width / gen);
	}

	private int adjustHeight(double height) {
		return (int) (dim.getHeight() - (dim.getHeight() * height / (maxFit * 1.1)));
	}
}
