package com.speyejack.bots;

import java.util.ArrayList;
import java.util.List;

import com.speyejack.gui.Controller;
import com.speyejack.gui.DebugGUI;
import com.speyejack.learning.neat.Organisum;
import com.speyejack.learning.neat.Pool;

import javafx.geometry.Point2D;

public class Arena implements Runnable {
	private static final int BOT_AMOUNT = 2;
	private static final int DUMMY_AMOUNT = 0;
	private List<Pool> pools = new ArrayList<Pool>();
	private List<Organisum> orgs = new ArrayList<Organisum>();
	private Fight fight;
	private boolean running;
	private Controller control;

	public Arena() {
		for (int i = 0; i < BOT_AMOUNT; i++) {
			pools.add(new Pool(4, 6));
		}
		startNewFight();
	}

	public Arena(DebugGUI debug) {
		for (int i = 0; i < BOT_AMOUNT; i++) {
			pools.add(new Pool(4, 6));
			debug.addPool("Bot " + (i + 1), pools.get(i));
		}
		startNewFight();
	}
	
	private void setupOrgs(){
		orgs.clear();
		for (int i = 0; i < BOT_AMOUNT; i++){
			orgs.add(pools.get(i).nextOrganisum());
		}
	}
	// Make this better, it currently has a band-aid on it.
	private void startNewFight() {
		List<Bot> bots = new ArrayList<Bot>();
		boolean poolComplete = false;
		if (orgs.size() > 0){
			for (int i = 0; i < BOT_AMOUNT; i++) {
				if (pools.get(i).hasNextOrganisum()){
					orgs.set(i, pools.get(i).nextOrganisum());
					break;
				} else {
					pools.get(i).resetPool();
					orgs.set(i, pools.get(i).nextOrganisum());
				}
				if (i == BOT_AMOUNT - 1)
					poolComplete = true;
			}
		}else{
			setupOrgs();
		}
		
		if (poolComplete){
			for (int i = 0; i < BOT_AMOUNT; i++){
				pools.get(i).advancePool();
			}
			setupOrgs();
		}

		for (int i = 0; i < BOT_AMOUNT; i++) {
			Bot bot = new AIBot(orgs.get(i));
			bot.setPosition(new Point2D(1.0 / (BOT_AMOUNT + 1) * (i + 1), 0.5));
			bots.add(bot);
		}
		for (int i = 0; i < DUMMY_AMOUNT; i++) {
			DummyBot dummy = new DummyBot();
			dummy.setPosition(new Point2D(1.0 / (DUMMY_AMOUNT + 1) * (i + 1), 0.75));
			// dummy.setPosition(new Point2D(0.51, 0.25));
			bots.add(dummy);
		}
		fight = new Fight(bots);
	}

	public void update() {
		fight.update();
	}

	public String getGenNumberString() {
		String s = "";
		return s += pools.get(0).getGeneration().getGenNumber();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		running = true;
		while (running) {
			if (fight.isFinished())
				startNewFight();
			update();
		}
	}

	public Fight getTopFight() {
		List<Bot> bots = new ArrayList<Bot>();
		for (int i = 0; i < BOT_AMOUNT; i++) {
			Bot bot = new AIBot(pools.get(i).getTopOrganisum());
			bot.setPosition(new Point2D(1.0 / (BOT_AMOUNT + 1) * (i + 1), 0.5));
			bots.add(bot);
		}
		for (int i = 0; i < DUMMY_AMOUNT; i++) {
			DummyBot dummy = new DummyBot();
			dummy.setPosition(new Point2D(1.0 / (DUMMY_AMOUNT + 1) * (i + 1), 0.75));
			bots.add(dummy);
		}
		return new Fight(bots);
	}

}
