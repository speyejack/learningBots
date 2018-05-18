package com.speyejack.bots;

import javafx.geometry.Point2D;

public class ViewField implements Destroyable {
	private static final double MAX_VIEW_ANGLE = 0.2;
	private static final double MIN_VIEW_ANGLE = 0.01;
	private static final double VIEW_DISTANCE = 0.4;
	private double viewAngle = 0.15;
	private Bot bot;
	private boolean destroy = false;
	private boolean visible = true;

	public ViewField(Bot bot) {
		this.bot = bot;
	}

	public Point2D getPosFieldLeg() {
		return new Point2D(Math.cos(bot.getDirection() + viewAngle), Math.sin(bot.getDirection() + viewAngle))
				.multiply(VIEW_DISTANCE).add(bot.getPosition());
	}

	public Point2D getNegFieldLeg() {
		return new Point2D(Math.cos(bot.getDirection() - viewAngle), Math.sin(bot.getDirection() - viewAngle))
				.multiply(VIEW_DISTANCE).add(bot.getPosition());
	}

	public void increaseViewAngle(double angle) {
		viewAngle += angle;
		if (viewAngle > MAX_VIEW_ANGLE)
			viewAngle = MAX_VIEW_ANGLE;
	}

	public void decreaseViewAngle(double angle) {
		viewAngle -= angle;
		if (viewAngle < MIN_VIEW_ANGLE)
			viewAngle = MIN_VIEW_ANGLE;
	}

	public double getViewAngle() {
		return viewAngle;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	@Override
	public void destroy() {
		destroy = true;
	}

	@Override
	public boolean getDestroyFlag() {
		return destroy;
	}

	public boolean checkInView(Entity e) {

		Point2D pos = bot.getPosition();
		Point2D v0 = getPosFieldLeg().subtract(pos);
		Point2D v1 = getNegFieldLeg().subtract(pos);
		Point2D v2 = e.getPosition().subtract(pos);

		double dot00 = v0.dotProduct(v0);
		double dot01 = v0.dotProduct(v1);
		double dot02 = v0.dotProduct(v2);
		double dot11 = v1.dotProduct(v1);
		double dot12 = v1.dotProduct(v2);

		double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
		
		return (u >= 0) && (v >= 0) && (u + v < 1);

//		if (bot.getPosition().distance(e.getPosition()) > bot.getSize() / 2 + VIEW_DISTANCE)
//			return false;
//		if (getPosFieldLeg().distance(e.getPosition()) > bot.getSize() / 2 + VIEW_DISTANCE)
//			return false;
//		if (getNegFieldLeg().distance(e.getPosition()) > bot.getSize() / 2 + VIEW_DISTANCE)
//			return false;
//		return true;

	}

}
