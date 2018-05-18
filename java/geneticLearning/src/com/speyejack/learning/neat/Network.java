package com.speyejack.learning.neat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Network {

	private double fitness = 0;
	private List<Neuron> inputs;
	private List<Neuron> neurons;
	private List<Neuron> outputs;

	public Network(List<Neuron> neurons) {
		this.neurons = neurons;
		inputs = new ArrayList<Neuron>();
		outputs = new ArrayList<Neuron>();
		for (Iterator<Neuron> nIter = neurons.iterator(); nIter.hasNext();) {
			Neuron n = nIter.next();
			if (n.getType() == Neuron.OUTPUT_NODE)
				outputs.add(n);
			else if (n.getType() == Neuron.INPUT_NODE)
				inputs.add(n);
		}
	}

	public List<Neuron> getInputs() {
		return inputs;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return fitness;
	}
	
	public void addFitness(double fitness){
		this.fitness += fitness;
	}
	
	public List<Neuron> getNeurons(){
		return neurons;
	}
	
	public boolean debug(){
		for (Iterator<Neuron> nIter = neurons.iterator(); nIter.hasNext();)
			if (nIter.next().getRank() < 0)
				return true;
		return false;
	}

	public List<Neuron> evalutate(List<Double> inputs) {
		Iterator<Double> inputIter = inputs.iterator();
		Iterator<Neuron> neuronIter = neurons.iterator();
		while (inputIter.hasNext()) {
			neuronIter.next().updateValue(inputIter.next());
		}
		while (neuronIter.hasNext()) {
			neuronIter.next().updateValue();
		}
		return outputs;
	}
	
	public List<Neuron> evalutate(double[] inputs) {
		Iterator<Neuron> neuronIter = neurons.iterator();
		for (int i = 0; i < inputs.length; i++) {
			neuronIter.next().updateValue(inputs[i]);
		}
		while (neuronIter.hasNext()) {
			neuronIter.next().updateValue();
		}
		return outputs;
	}

}
