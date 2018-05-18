package com.speyejack.bots;

import java.util.List;

import javafx.geometry.Point2D;

public class Entity implements Destroyable{

	private double dir = -Math.PI/2;
	private Point2D pos;
	private Point2D vel;
	private double size;
	private boolean destroy = false;

	public Entity() {
		pos = new Point2D(0.5, 0.5);
		vel = new Point2D(0, 0);
	}

	public Entity(double x, double y) {
		pos = new Point2D(x, y);
		vel = new Point2D(0, 0);
	}

	public Entity(Point2D pos, Point2D vel, double dir) {
		this.pos = pos;
		this.vel = vel;
		this.dir = dir;
	}
	
	public void destroy(){
		destroy = true;
	}
	
	public boolean getDestroyFlag(){
		return destroy;
	}
	

	public void turn(double rad) {
		dir += rad;
	}

	public void setPosition(Point2D pos) {
		this.pos = pos;
	}

	public void setVelocity(Point2D vel) {
		this.vel = vel;
	}

	public void updatePos() {
		pos = pos.add(vel);
	}

	public Point2D getPosition() {
		return new Point2D(pos.getX(), pos.getY());
	}

	public Point2D getVelocity() {
		return new Point2D(vel.getX(), vel.getY());
	}

	public double getDirection() {
		return dir;
	}

	protected void setDirection(double angle){
		dir = angle;
	}

	protected void setSize(double size) {
		this.size = size;
	}

	public double getSize() {
		return size;
	}
	
	public boolean hasCollision(Entity entity){
		double dist = this.getPosition().subtract(entity.getPosition()).magnitude();
		double minDist = this.getSize() / 2 + entity.getSize() / 2;
		return (dist < minDist);
	}

	public void checkCollisions(List<? extends Entity> entities) {
		return;
	}
	
	public void updateInputs(List<Entity> entities){
		return;
	}

}
