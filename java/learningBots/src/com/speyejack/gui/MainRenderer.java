package com.speyejack.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.speyejack.bots.AIBot;
import com.speyejack.bots.DummyBot;
import com.speyejack.bots.Entity;
import com.speyejack.bots.Pellet;

public class MainRenderer {
	private Map<Class<? extends Entity>, EntityRender<? extends Entity>> renderers;
	private List<Entity> entities;

	public MainRenderer(Dimension dim) {
		renderers = new HashMap<Class<? extends Entity>, EntityRender<? extends Entity>>();
		renderers.put(AIBot.class, new AIBotRenderer(dim));
		renderers.put(Pellet.class, new PelletRenderer(dim));
		renderers.put(DummyBot.class, new DummyRenderer(dim));
	}

	public void setEntityList(List<Entity> entities) {
		this.entities = entities;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void draw(Graphics g) {
		if (entities == null)
			return;
		synchronized (entities) {
			for (Iterator<Entity> eIter = entities.iterator(); eIter.hasNext();) {
				Entity e = eIter.next();
				EntityRender render = renderers.get(e.getClass());
				if (render != null) {
					render.draw(g, e);
				}
			}
		}
	}

}
