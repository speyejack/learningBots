package com.speyejack.bots;

public abstract class StaticEntity implements Destroyable{
	private boolean destroy = false;
	
	@Override
	public void destroy() {
		destroy = true;
	}

	@Override
	public boolean getDestroyFlag() {
		return destroy;
	}

}
