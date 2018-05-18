package com.speyejack.bots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fight {
	private static final int UPDATE_LIMIT = 1000;
	private boolean finished = false;
	private int updateCounter = 0;
	private List<Entity> entities;
	private List<Bot> bots;

	public Fight(List<Bot> bots) {
		this.bots = bots;
		entities = Collections.synchronizedList(new ArrayList<Entity>());
		entities.addAll(bots);
	}

	public boolean isFinished() {
		return finished;
	}
	
	public List<Entity> getEntities(){
		return entities;
	}

	public void update() {
		if (updateCounter >= UPDATE_LIMIT) {
			finished = true;
			for (int i = 0; i < bots.size();i++)
				bots.get(i).updateFitness();
			return;
		}
		List<Entity> toDestroy = new ArrayList<Entity>();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getDestroyFlag())
				toDestroy.add(entities.get(i));
			else {
				entities.get(i).updateInputs(entities);
				entities.get(i).updatePos();
				entities.get(i).checkCollisions(bots);
			}
		}
		synchronized (entities) {
			entities.removeAll(toDestroy);
			String score = "";
			for (int i = 0; i < bots.size(); i++) {
				if (bots.get(i).shootPellet())
					entities.add(new Pellet(bots.get(i)));
				score += bots.get(i).getPoints() + ":";

			}
			score = score.substring(0, score.length());
		}
		updateCounter++;
	}

}
