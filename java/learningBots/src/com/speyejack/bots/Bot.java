package com.speyejack.bots;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;

public abstract class Bot extends CircularEntity {
	private double size = 0.04;
	private double turnSpeed = 0.1;
	private double angleSpeed = 0.05;
	private double velocity = 0.02;

	private static final int SHOOT_COOLDOWN = 40;
	private double[] inputs;
	private List<Pellet> pellets;
	private int shootTimer = 0;
	private double points = 0;
	private ViewField view;
	private boolean shoot = false;

	public Bot() {
		super(0.5, 0.5);
		this.setSize(size);
		this.pellets = new ArrayList<Pellet>();
		this.view = new ViewField(this);
	}

	protected void setVelocity(double vel) {
		velocity = vel;
	}
	
	protected void setAngleSpeed(double angleSpeed) {
		this.angleSpeed = angleSpeed;
	}
	
	public void setTurnSpeed(double turnSpeed) {
		this.turnSpeed = turnSpeed;
	}
	

	public void incrementPoints(double amount) {
		points += amount;
	}

	public void decrementPoints(double amount) {
		points -= amount;
	}

	public double getPoints() {
		return points;
	}
	
	protected void setPoints(double points){
		this.points = points;
	}

	public boolean shootPellet() {
		if (shoot) {
			shootTimer = SHOOT_COOLDOWN;
			shoot = false;
			return true;
		}
		shootTimer--;
		return false;
	}

	public void increaseViewAngle(double angle) {
		view.increaseViewAngle(angle);
	}

	public void decreaseViewAngle(double angle) {
		view.decreaseViewAngle(angle);
	}

	public double getViewAngle() {
		return view.getViewAngle();
	}

	public List<Pellet> getPellets() {
		return pellets;
	}

	protected ViewField getViewField() {
		return view;
	}

	public List<Point2D> getViewLegs() {
		List<Point2D> list = new ArrayList<Point2D>();
		list.add(getPosition());
		list.add(view.getPosFieldLeg());
		list.add(view.getNegFieldLeg());
		return list;
	}

	public boolean legsVisible() {
		return view.isVisible();
	}

	@Override
	public abstract void updateInputs(List<Entity> entities);

	protected void setInputs(double[] inputs) {
		this.inputs = inputs;
	}

	// TEMP
	public double[] getInputs() {
		return inputs;
	}

	@Override
	public void updatePos() {
		super.updatePos();
		if (inputs[0] > 0)
			this.setVelocity(
					new Point2D(Math.cos(this.getDirection()), Math.sin(this.getDirection())).multiply(velocity));
		else
			this.setVelocity(new Point2D(0, 0));
		if (inputs[1] > 0 && inputs[2] <= 0)
			this.turn(turnSpeed);
		if (inputs[2] > 0 && inputs[1] <= 0)
			this.turn(-turnSpeed);
		if (inputs[3] > 0 && inputs[4] <= 0)
			this.increaseViewAngle(angleSpeed);
		if (inputs[4] > 0 && inputs[3] <= 0)
			this.decreaseViewAngle(angleSpeed);
		if (inputs[5] > 0 && shootTimer <= 0)
			shoot = true;
		double x = getPosition().getX();
		double y = getPosition().getY();
		if (x - getSize() / 2 < 0) {
			x = 0 + getSize() / 2;
		}
		if (x + getSize() / 2 > 1) {
			x = 1 - getSize() / 2;
		}
		if (y - getSize() / 2 < 0) {
			y = 0 + getSize() / 2;
		}
		if (y + getSize() / 2 > 1) {
			y = 1 - getSize() / 2;
		}
		this.setPosition(new Point2D(x, y));
	}

	@Override
	public void checkCollisions(List<? extends Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			// double dist = this.getPosition().distance(entity.getPosition());

			// double minDist = 0.04;
			if (entity != this && hasCollision(entity)) {
				double dist = this.getPosition().subtract(entity.getPosition()).magnitude();
				double minDist = this.getSize() / 2 + entity.getSize() / 2;
				this.setPosition(
						this.getPosition().add(this.getVelocity().normalize().multiply((minDist - dist) * -1)));

				dist = this.getPosition().subtract(entity.getPosition()).magnitude();
				this.setPosition(this.getPosition()
						.add(this.getPosition().subtract(entity.getPosition()).normalize().multiply((minDist - dist))));

				this.decrementPoints(200);
				((Bot) entity).decrementPoints(200);
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		view.destroy();
	}

	public void updateFitness() {
	}

}
