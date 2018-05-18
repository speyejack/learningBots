package com.speyejack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.speyejack.bots.Entity;
import com.speyejack.bots.Fight;

public class FighterPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = -1803323278203687874L;
	private static final double TARGET_UPS = 60;
	private static final double TARGET_FPS = 60;
	private static final double TARGET_UPDATE_TIME = 1000000000 / TARGET_UPS;
	private static final double TARGET_RENDER_TIME = 1000000000 / TARGET_FPS;

	private Fight fight;
	private MainRenderer render;
	private Dimension d;

	public FighterPanel(Dimension d) {
		super();
		this.setPreferredSize(d);
		this.d = d;
		render = new MainRenderer(d);
		this.setVisible(true);
	}

	public void fightLoop() {
		long lastUpdate = System.nanoTime();
		long lastRender = System.nanoTime();
		long now;
		// try {
		while (!fight.isFinished()) {
			now = System.nanoTime();
			if (TARGET_UPDATE_TIME <= (now - lastUpdate)) {
				fight.update();
				lastUpdate = now;
			}

			if (TARGET_RENDER_TIME <= (now - lastRender)) {
				this.repaint();
				lastRender = now;
			}

			try {
				Thread.sleep((long) 0.01);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println("Done");
		// render.setEntityList(null);9
		// System.exit(0);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// System.exit(0);
		// }
	}

	public void setFight(Fight fight) {
		this.fight = fight;
		render.setEntityList(fight.getEntities());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setSize(d);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) d.getWidth(), (int) d.getHeight());
		
		render.draw(g);
	}

	@Override
	public void run() {
		fightLoop();
	}

}
