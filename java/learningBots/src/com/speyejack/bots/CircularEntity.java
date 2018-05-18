package com.speyejack.bots;

import javafx.geometry.Point2D;

public class CircularEntity extends Entity{
	
	public CircularEntity() {
		super();
	}
	
	public CircularEntity(Point2D pos, Point2D vel, double dir) {
		super(pos, vel, dir);
	}
	
	public CircularEntity(double x, double y) {
		super(x,y);
	}

}
