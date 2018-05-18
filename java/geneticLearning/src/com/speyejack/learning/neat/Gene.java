package com.speyejack.learning.neat;

import java.io.Serializable;

public class Gene implements Serializable{
	private static final long serialVersionUID = 8354014945616973851L;
	private int into;
	private int out;
	private double weight;
	private int innovation;
	private boolean enabled;

	public Gene(int into, int out, double weight, int innovation) {
		this.into = into;
		this.out = out;
		this.weight = weight;
		this.innovation = innovation;
		this.enabled = true;
	}

	public void setInnovation(int inno) {
		this.innovation = inno;
	}
	
	public int getInnovation(){
		return innovation;
	}

	public int getInto() {
		return into;
	}

	public int getOut() {
		return out;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void disable(){
		enabled = false;
	}
	
	public void enable(){
		enabled = true;
	}
	
	private void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public static Gene copyGene(Gene g){
		Gene ng = new Gene(g.getInto(), g.getOut(), g.getWeight(), g.innovation);
		ng.setEnabled(g.isEnabled());
				
		return ng;
	}
	
	@Override
	public String toString() {
		return getInto() + " - " + getOut() + " : " + getWeight();
	}

}
