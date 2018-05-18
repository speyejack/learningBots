package com.speyejack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import com.speyejack.bots.DummyBot;

import javafx.geometry.Point2D;

public class DummyRenderer implements EntityRender<DummyBot> {
	private Dimension dim;

	public DummyRenderer(Dimension dim) {
		this.dim = dim;
	}

	@Override
	public void draw(Graphics g, DummyBot dummy) {
		Point2D pos = dummy.getPosition();
		g.setColor(Color.getHSBColor(0.66F, 0.5F, 1F));
		g.fillOval((int) ((pos.getX() - dummy.getSize() / 2) * dim.getWidth()),
				(int) ((pos.getY() - dummy.getSize() / 2) * dim.getHeight()), (int) (dummy.getSize() * dim.getWidth()),
				(int) (dummy.getSize() * dim.getHeight()));
		g.setColor(Color.GREEN);
		g.fillOval(
				(int) ((pos.getX() + Math.cos(dummy.getDirection()) * dummy.getSize() / 4 - dummy.getSize() / 4)
						* dim.getWidth()),
				(int) ((pos.getY() + Math.sin(dummy.getDirection()) * dummy.getSize() / 4 - dummy.getSize() / 4)
						* dim.getHeight()),
				(int) (dummy.getSize() / 2 * dim.getWidth()), (int) (dummy.getSize() / 2 * dim.getHeight()));
	}

}
