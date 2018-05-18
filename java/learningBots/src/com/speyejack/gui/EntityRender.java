package com.speyejack.gui;

import java.awt.Graphics;

import com.speyejack.bots.Entity;

public interface EntityRender<T extends Entity> {
	
	public void draw(Graphics g, T e);

}
