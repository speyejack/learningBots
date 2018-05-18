package com.speyejack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import com.speyejack.bots.Bot;

import javafx.geometry.Point2D;

public class AIBotRenderer implements EntityRender<Bot> {
	private Dimension dim;

	public AIBotRenderer(Dimension dim) {
		this.dim = dim;
	}

	@Override
	public void draw(Graphics g, Bot bot) {
		Point2D pos = bot.getPosition();
		if (bot.legsVisible()) {
			g.setColor(Color.white);
			List<Point2D> points = bot.getViewLegs();

			for (int i = 1; i < points.size(); i++) {
				g.drawLine((int) (points.get(0).getX() * dim.getWidth()),
						(int) (points.get(0).getY() * dim.getHeight()), (int) (points.get(i).getX() * dim.getWidth()),
						(int) (points.get(i).getY() * dim.getHeight()));
			}
		}
		
		g.setColor(Color.RED);
		g.fillOval((int) ((pos.getX() - bot.getSize() / 2) * dim.getWidth()),
				(int) ((pos.getY() - bot.getSize() / 2) * dim.getHeight()), (int) (bot.getSize() * dim.getWidth()),
				(int) (bot.getSize() * dim.getHeight()));
		g.setColor(Color.WHITE);
		g.fillOval(
				(int) ((pos.getX() + Math.cos(bot.getDirection()) * bot.getSize() / 4 - bot.getSize() / 4)
						* dim.getWidth()),
				(int) ((pos.getY() + Math.sin(bot.getDirection()) * bot.getSize() / 4 - bot.getSize() / 4)
						* dim.getHeight()),
				(int) (bot.getSize() / 2 * dim.getWidth()), (int) (bot.getSize() / 2 * dim.getHeight()));
		
		float points = Math.round(bot.getPoints() * 100) / 100;
		String banner = String.valueOf(points);
		
		g.drawString(banner,(int) (pos.getX()*dim.getWidth() - g.getFontMetrics().stringWidth(banner)/2), (int) ((pos.getY() - bot.getSize()* 0.75 )*dim.getHeight()));
	}
}
