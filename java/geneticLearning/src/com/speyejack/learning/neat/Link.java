package com.speyejack.learning.neat;

public class Link {
	private Neuron into;
	private Neuron out;
	private double weight;
	private int id = -1;

	public Link(Neuron into, Neuron out, double weight) {
		this.into = into;
		this.out = out;
		this.weight = weight;

		this.out.addIncomingLink(this);
	}
	
	public void setId(int id){
		this.id = id;
	}

	public Neuron getInto() {
		return into;
	}

	public Neuron getOut() {
		return out;
	}

	public double getWeight() {
		return weight;
	}

}
