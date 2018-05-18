package com.speyejack.bots;

import java.util.List;

import javafx.geometry.Point2D;

public class Pellet extends CircularEntity {
	private static final double PELLET_SIZE = 0.01;
	private static final double PELLET_VELOCITY = 0.01;
	private Bot bot;

	public Pellet(Bot bot) {
		super();
		this.bot = bot;
		this.setSize(PELLET_SIZE);
		bot.decrementPoints(5);
		this.setDirection(bot.getDirection());
		this.setPosition(new Point2D(bot.getPosition().getX() + Math.cos(getDirection()) * bot.getSize() / 2,
				bot.getPosition().getY() + Math.sin(getDirection()) * bot.getSize() / 2));
		this.setVelocity(new Point2D(Math.cos(this.getDirection()), Math.sin(this.getDirection())).normalize()
				.multiply(PELLET_VELOCITY));
	}
	
	public boolean isOwner(Bot bot){
		return bot == this.bot;
	}

	@Override
	public void updatePos() {
		super.updatePos();
		if (getPosition().getX() < 0 || getPosition().getX() > 1 || getPosition().getY() < 0
				|| getPosition().getY() > 1)
			destroy();
	}

	@Override
	public void checkCollisions(List<? extends Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != bot && hasCollision(entities.get(i))) {
				bot.incrementPoints(105);
				((Bot) entities.get(i)).decrementPoints(100);
				this.destroy();
			}
		}
	}

}
