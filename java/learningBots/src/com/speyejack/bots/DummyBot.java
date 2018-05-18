package com.speyejack.bots;

import java.util.List;

import javafx.geometry.Point2D;

public class DummyBot extends Bot {
	private static final double SIZE = 0.04;
	/*
	 * 1: Velocity 2: Turn Clockwise 3: Turn Counter-Clockwise 4: Increase View
	 * 5: Decrease View 6: Shoot
	 */

	public DummyBot() {
		this.setSize(SIZE);
		this.setDirection(-Math.PI/2);
		this.setInputs(new double[] { 1, 1, 0, 0, 0, 1 });
		// this.setVelocity(0.025);
		// this.setAngleSpeed(0.1);
		this.setVelocity(0.005);
		this.setTurnSpeed(0.02);
		// this.setAngleSpeed(0.001);
		this.setPosition(new Point2D(0.5, 0.9));
	}

	@Override
	public void updateInputs(List<Entity> entities) {
		// if (Math.cos(this.getDirection()) > 0 &&
		// Math.sin(this.getDirection()) > 0){
		// this.setInputs(new double[] {1,0,1,0,0,0});
		// this.setVelocity(0.01);
		// this.setTurnSpeed(0.02);
		// }
	}

}
