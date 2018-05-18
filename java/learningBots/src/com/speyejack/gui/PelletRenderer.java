package com.speyejack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import com.speyejack.bots.Bot;
import com.speyejack.bots.Pellet;

import javafx.geometry.Point2D;

public class PelletRenderer implements EntityRender<Pellet> {
	private Dimension dim;

	public PelletRenderer(Dimension dim) {
		this.dim = dim;
	}

	@Override
	public synchronized void draw(Graphics g, Pellet pellet) {
		g.setColor(Color.YELLOW);
		Point2D pos = pellet.getPosition();
		g.fillOval((int) ((pos.getX() - pellet.getSize() / 2) * dim.getWidth()),
				(int) ((pos.getY() - pellet.getSize() / 2) * dim.getHeight()), (int) (pellet.getSize() * dim.getWidth()),
				(int) (pellet.getSize() * dim.getHeight()));

	}
}
