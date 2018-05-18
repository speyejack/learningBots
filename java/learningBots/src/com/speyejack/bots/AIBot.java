package com.speyejack.bots;

import java.util.List;

import com.speyejack.learning.neat.Organisum;

public class AIBot extends Bot {

	private Organisum org;

	public AIBot(Organisum org) {
		super();
		this.org = org;
	}

	@Override
	public void updateInputs(List<Entity> entities) {
		double[] outputs = new double[] { 0, 0, 0, 1.0 }; // Bias
		boolean pellet_found = false;
		boolean bot_found = false;
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this && this.getViewField().checkInView(e)) {
				if (!bot_found && (e.getClass() == AIBot.class || e.getClass() == DummyBot.class)) {
					bot_found = true;
					outputs[2] = 1 - e.getPosition().subtract(this.getPosition()).magnitude();
					outputs[0] = 1;
//					this.incrementPoints(0.01);
				}
				if (!pellet_found && e.getClass() == Pellet.class && !((Pellet) e).isOwner(this)) {
					pellet_found = true;
//					this.decrementPoints(0.01);
				}
			}
		}

		if (bot_found)
			outputs[0] = 1;
		if (pellet_found)
			outputs[1] = 1;

		double[] inputs = this.org.evalutate(outputs);
		// double[] inputs = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0 };
		this.setInputs(inputs);
	}

	public void updateFitness() {
		if (this.getPoints() < 0)
			setPoints(0);
		org.addFitness(this.getPoints());
	}

}
