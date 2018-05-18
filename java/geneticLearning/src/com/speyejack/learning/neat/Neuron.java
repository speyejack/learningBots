package com.speyejack.learning.neat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Neuron {

	private List<Link> incoming = new ArrayList<Link>();
	private int id = -1;
	private int type;
	private int rank = -1;
	private double value = 0;
	public final static int INPUT_NODE = 1;
	public final static int HIDDEN_NODE = 2;
	public final static int OUTPUT_NODE = 3;
	
	public Neuron(int id, int type) {
		this.id = id;
		this.type = type;
		if (type == INPUT_NODE)
			rank = 0;
		else if(type == OUTPUT_NODE)
			rank = Integer.MAX_VALUE;
	}

	public void addIncomingLink(Link link) {
		incoming.add(link);
	}
	
	public List<Link> getLinks(){
		return incoming;
	}

	private void setValue(double value) {
		this.value = value;
	}
	
	public void setRank(int rank){
		this.rank = rank;
	}
	
	private int determineRank(List<Neuron> visited){
		if (visited.contains(this))
			rank = 0;
		else if (incoming.size() == 0)
			rank = 1;
		if (rank < 0){
			visited.add(this);
			for (Iterator<Link> lIter = incoming.iterator(); lIter.hasNext();){
				rank = Math.max(rank, lIter.next().getInto().determineRank(visited) + 1);
			}
		}
		return rank;
	}
	
	public int getRank(){
		if (rank < 0)
			determineRank(new ArrayList<Neuron>());
		return rank;
	}

	public double getValue() {
		return value;
	}
	
	public int getType(){
		return type;
	}
	
	public int getId(){
		return id;
	}

	public void updateValue() {
		if (type == Neuron.HIDDEN_NODE || type == Neuron.OUTPUT_NODE) {
			Iterator<Link> iter = incoming.iterator();
			double total = 0;
			while (iter.hasNext()){
				Link l = iter.next();
				total += l.getInto().getValue() * l.getWeight();
			}
			setValue(RoutineF.sigmoid(total));
		}
	}

	public void updateValue(double value) {
		if (type == Neuron.INPUT_NODE) {
			setValue(value);
		}
	}
	
	@Override
	public String toString() {
		return "Id: " + this.id + " T: " + this.type;
	}
}
